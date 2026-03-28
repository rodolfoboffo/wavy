#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Full recompile of Wavy WPF application with C++ DLL
.DESCRIPTION
    Builds C++ DLL in Debug x64, copies it to WPF output, builds WPF app, and runs it
.EXAMPLE
    .\full-recompile.ps1
    .\full-recompile.ps1 -Configuration Release
    .\full-recompile.ps1 -NoRun
#>

param(
    [ValidateSet("Debug", "Release")]
    [string]$Configuration = "Debug",
    
    [switch]$NoRun
)

$ErrorActionPreference = "Stop"
$workspace_root = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)

Write-Host "=" * 80
Write-Host "🔨 Wavy Full Recompile & Run - $Configuration Configuration"
Write-Host "=" * 80

try {
    # Step 1: Build C++ DLL
    Write-Host ""
    Write-Host "📦 Step 1: Building C++ DLL ($Configuration x64)..."
    Push-Location "$workspace_root\msvc-shared-library"
    
    $msbuild = "C:\Program Files\Microsoft Visual Studio\2022\Community\MSBuild\Current\Bin\MSBuild.exe"
    if (-not (Test-Path $msbuild)) {
        throw "MSBuild not found at: $msbuild"
    }
    
    & $msbuild msvc-shared-library.sln /p:Configuration=$Configuration /p:Platform=x64 /verbosity:minimal
    if ($LASTEXITCODE -ne 0) {
        throw "C++ build failed with exit code: $LASTEXITCODE"
    }
    Write-Host "✓ C++ DLL compiled successfully"
    Pop-Location
    
    # Step 2: Copy DLL to WPF output
    Write-Host ""
    Write-Host "📋 Step 2: Copying wavy.dll to WPF output directory..."
    
    $source_dll = "$workspace_root\msvc-shared-library\x64\$Configuration\wavy.dll"
    $target_dir = "$workspace_root\wpf-app\bin\$Configuration\net8.0-windows"
    
    if (-not (Test-Path $source_dll)) {
        throw "DLL not found at: $source_dll"
    }
    
    if (-not (Test-Path $target_dir)) {
        New-Item -ItemType Directory -Path $target_dir -Force | Out-Null
    }
    
    Copy-Item $source_dll $target_dir -Force
    $dll_size = (Get-Item "$target_dir\wavy.dll").Length / 1KB
    Write-Host "✓ Copied wavy.dll to: $target_dir ($([Math]::Round($dll_size, 2)) KB)"
    
    # Step 3: Build WPF Application
    Write-Host ""
    Write-Host "🔧 Step 3: Building WPF Application ($Configuration)..."
    Push-Location "$workspace_root\wpf-app"
    
    dotnet build wpf-app.csproj -c $Configuration
    if ($LASTEXITCODE -ne 0) {
        throw "WPF build failed with exit code: $LASTEXITCODE"
    }
    Write-Host "✓ WPF Application compiled successfully"
    Pop-Location
    
    # Step 4: Run Application
    if (-not $NoRun) {
        Write-Host ""
        Write-Host "🚀 Step 4: Launching Wavy WPF Application..."
        
        $exe_path = "$workspace_root\wpf-app\bin\$Configuration\net8.0-windows\wpf-app.exe"
        if (-not (Test-Path $exe_path)) {
            throw "Executable not found at: $exe_path"
        }
        
        Write-Host "✓ Starting: $exe_path"
        & $exe_path
    }
    
    Write-Host ""
    Write-Host "=" * 80
    Write-Host "✅ Full recompile completed successfully!"
    Write-Host "=" * 80
}
catch {
    Write-Host ""
    Write-Host "=" * 80
    Write-Host "❌ Build failed: $_"
    Write-Host "=" * 80
    exit 1
}
