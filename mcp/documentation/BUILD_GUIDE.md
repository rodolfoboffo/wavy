# Build & Deployment Guide

## Prerequisites

### System Requirements

- **OS**: Windows 10 / Windows 11 (x64)
- **RAM**: 8GB minimum (16GB recommended)
- **Disk**: 2GB free space for build artifacts

### Required Software

#### For C++ Core

- **Visual Studio 2019 / 2022** with C++ workload
  - Desktop development with C++
  - MSVC v142 or later
- **Windows SDK 10.0.20348** (or latest)

**Installation**:
```powershell
# Via Visual Studio Installer
# Select: Desktop development with C++
```

#### For Java Application

- **JDK 8+** (Eclipse Adoptium / Temurin recommended)
- **Maven 3.9+**

**Installation (Windows)**:
```powershell
# via chocolatey
choco install adoptopenjdk11 maven
# or download manually
# https://adoptium.net/
# https://maven.apache.org/download.cgi
```

**Verification**:
```bash
java -version
mvn -version
```

#### For C# Application

- **.NET 8.0 SDK** (LTS)
- **Visual Studio 2022** or **VS Code** with C# extensions

**Installation**:
```powershell
# via dotnet.microsoft.com
# Download .NET 8.0 SDK

# or via chocolatey
choco install dotnet-sdk
```

**Verification**:
```bash
dotnet --version  # Should show 8.0.x
```

### Environment Variables

Set these for easier command-line building:

```powershell
# C++
[Environment]::SetEnvironmentVariable("VCINSTALLDIR", "C:\Program Files\Microsoft Visual Studio\2022\Community\VC", [EnvironmentVariableTarget]::User)

# Java
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-11.0.x", [EnvironmentVariableTarget]::User)
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\apache-maven-3.9.x", [EnvironmentVariableTarget]::User)

# .NET
[Environment]::SetEnvironmentVariable("DOTNET_ROOT", "C:\Program Files\dotnet", [EnvironmentVariableTarget]::User)

# Add to PATH
$env:PATH += ";$env:MAVEN_HOME\bin;$env:DOTNET_ROOT\bin"
```

---

## Building Components

### 1. C++ Core Library (wavy.dll)

**Location**: `msvc-shared-library/`

#### Command-Line Build

```bash
# Navigate to solution directory
cd msvc-shared-library

# Debug build (x64)
msbuild msvc-shared-library.sln /p:Configuration=Debug /p:Platform=x64

# Release build (x64)
msbuild msvc-shared-library.sln /p:Configuration=Release /p:Platform=x64

# Clean build
msbuild msvc-shared-library.sln /p:Configuration=Debug /p:Platform=x64 /t:Clean
```

#### Build Output

- **Debug**: `msvc-shared-library/x64/Debug/wavy.dll` (~5-10 MB)
- **Release**: `msvc-shared-library/x64/Release/wavy.dll` (~3-5 MB)

#### Visual Studio IDE Build

1. Open `msvc-shared-library/msvc-shared-library.sln`
2. Select configuration: **Debug** or **Release**
3. Select platform: **x64**
4. Right-click solution → **Build Solution** (Ctrl+Shift+B)
5. Output: `Build succeeded` message
6. Find DLL in `x64/{Debug|Release}/`

#### Common Issues

| Issue | Solution |
|-------|----------|
| "vcvars bat not found" | Install MSVC development tools via Visual Studio |
| "Windows SDK not found" | Install Windows SDK 10.0.20348+ |
| Link error with JNA | Ensure C ABI exports are correctly decorated with `extern "C"` |

---

### 2. Java Application (JAR)

**Location**: `java-app/`

#### CLI Build

```bash
# Navigate to java-app directory
cd java-app

# Clean and build (skip tests)
mvn clean package -DskipTests

# Full build with tests
mvn clean package

# Just compile (no packaging)
mvn clean compile

# Install to local Maven repo
mvn install
```

#### Build Output

- **JAR**: `java-app/target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar` (~50-100 MB)
- **Classes**: `java-app/target/classes/`
- **Test Results**: `java-app/target/surefire-reports/`

#### Running the Application

```bash
# Method 1: Direct JAR execution
java -jar target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar

# Method 2: Classpath (if using libraries)
java -cp target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar com.terpomo.wavy.ui.UIApplication
```

**Important**: The C++ DLL (`wavy.dll`) must be in `PATH` or in the same directory for JNA to load it.

#### IDE Build (IntelliJ / Eclipse)

1. Open `java-app/pom.xml` as project
2. Maven view → right-click project → **Reimport**
3. Maven view → right-click project → **Run Maven** → **clean**
4. Maven view → right-click project → **Run Maven** → **package**

#### Common Issues

| Issue | Solution |
|-------|----------|
| "Cannot find JDK" | Set `JAVA_HOME` environment variable |
| "Dependency resolution failed" | Check internet connection, try `mvn dependency:resolve` |
| "JNA cannot load librtlsdr" | Ensure librtlsdr is installed and in PATH (JNA is used for RTL-SDR hardware only, not wavy.dll) |
| "Test failures" | Use `-DskipTests` flag to skip test execution |

---

### 3. C# WPF Application (EXE)

**Location**: `wpf-app/`

#### CLI Build

```bash
# Navigate to wpf-app directory
cd wpf-app

# Debug build
dotnet build wpf-app.csproj -c Debug

# Release build
dotnet build wpf-app.csproj -c Release

# Clean built outputs
dotnet clean wpf-app.csproj
```

#### Build Output

- **Debug EXE**: `wpf-app/bin/Debug/net8.0-windows/wpf-app.exe` (~20-30 MB)
- **Release EXE**: `wpf-app/bin/Release/net8.0-windows/wpf-app.exe` (~15-20 MB)
- **Dependencies**: Runtime libraries copied to output directory

#### Running the Application

```bash
# Method 1: Via dotnet CLI
dotnet run --project wpf-app.csproj --configuration Debug

# Method 2: Direct execution
./bin/Debug/net8.0-windows/wpf-app.exe

# or

./bin/Release/net8.0-windows/wpf-app.exe
```

#### IDE Build (Visual Studio / VS Code)

**Visual Studio 2022**:
1. Open `wpf-app.sln`
2. Select configuration: **Debug** or **Release**
3. Right-click project → **Build** (Ctrl+Shift+B)
4. Output directory populated with EXE

**VS Code**:
1. Open `wpf-app` folder
2. Install "C# Dev Kit" extension
3. Terminal → **Run Build Task** (Ctrl+Shift+B)
4. Select `build` task

#### Common Issues

| Issue | Solution |
|-------|----------|
| ".NET 8.0 not found" | Install .NET 8.0 SDK from dotnet.microsoft.com |
| "P/Invoke DLL not found" | Ensure `wavy.dll` is in same directory or PATH |
| "WPF components missing" | Rebuild NuGet packages: `dotnet restore` |

---

## Multi-Component Build

### Automated Full Recompile (Recommended)

**Quick Command**:
```powershell
.\mcp\scripts\full-recompile.ps1
```

**Features**:
- ✅ Builds C++ DLL in Debug x64
- ✅ Copies wavy.dll to WPF output
- ✅ Builds WPF application
- ✅ Launches WPF executable
- ✅ Supports Release configuration: `-Configuration Release`
- ✅ Supports build-only mode: `-NoRun`

See [FULL_RECOMPILE.md](FULL_RECOMPILE.md) for complete documentation.

### Manual Build All Components (PowerShell)

```powershell
$msbuild = "C:\Program Files\Microsoft Visual Studio\2022\Community\MSBuild\Current\Bin\MSBuild.exe"

# Step 1: Build C++ first (dependency for both Java and C#)
Write-Host "Building C++ Core..."
cd msvc-shared-library
& $msbuild msvc-shared-library.sln /p:Configuration=Release /p:Platform=x64
if ($LASTEXITCODE -ne 0) { Write-Error "C++ build failed"; exit 1 }
cd ..

# Step 2: Copy DLL to known location
Copy-Item "msvc-shared-library/x64/Release/wavy.dll" "./wavy.dll"

# Step 3: Build Java
Write-Host "Building Java Application..."
cd java-app
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) { Write-Error "Java build failed"; exit 1 }
cd ..

# Step 4: Build C#
Write-Host "Building C# WPF Application..."
cd wpf-app
dotnet build wpf-app.csproj -c Release
if ($LASTEXITCODE -ne 0) { Write-Error "C# build failed"; exit 1 }
cd ..

Write-Host "All builds completed successfully!"
```

### Build Outputs Summary

| Component | Output | Size | Location |
|-----------|--------|------|----------|
| **C++** | wavy.dll | 3-10 MB | `msvc-shared-library/x64/{Debug\|Release}/` |
| **Java** | wavy-*.jar | 50-100 MB | `java-app/target/` |
| **C#** | wpf-app.exe | 15-30 MB | `wpf-app/bin/{Debug\|Release}/` |

---

## Deployment

### Standalone Java Application

**Package**:
```bash
mkdir wavy-release
cp java-app/target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar wavy-release/
cp msvc-shared-library/x64/Release/wavy.dll wavy-release/

# Optional: create launcher script
echo "@echo off" > wavy-release/run.bat
echo "java -jar wavy-1.0-SNAPSHOT-jar-with-dependencies.jar" >> wavy-release/run.bat
```

**Distribution**: Zip `wavy-release/` folder

**End-User Installation**:
1. Extract ZIP
2. Place `wavy.dll` in same folder as JAR (or in PATH)
3. Run `run.bat` (Windows) or `java -jar wavy-*.jar` (cross-platform)

### Standalone C# WPF Application

**Package**:
```bash
mkdir wavy-wpf-release
cp wpf-app/bin/Release/net8.0-windows/* wavy-wpf-release/
cp msvc-shared-library/x64/Release/wavy.dll wavy-wpf-release/

# Optional: create shortcut
# (Windows shortcuts can directly launch wpf-app.exe)
```

**Distribution**: Zip `wavy-wpf-release/` folder

**End-User Installation**:
1. Extract ZIP
2. Ensure `.NET 8.0 runtime` is installed
3. Double-click `wpf-app.exe`

### Docker Containment (Optional)

For deployment on Linux/containers:
```dockerfile
FROM mcr.microsoft.com/windows/servercore:ltsc2022

# Install .NET 8.0 runtime
RUN powershell -Command Invoke-WebRequest -Uri https://dot.net/v1/dotnet-install.ps1...

# Copy WPF app
COPY wpf-app/bin/Release/net8.0-windows /app/

# Requires Windows-based Docker hosts
ENTRYPOINT [".\app\wpf-app.exe"]
```

---

## Troubleshooting

### Verify Installation

```bash
# Check C++ DLL
dir msvc-shared-library\x64\Release\wavy.dll

# Check Java build
java -jar java-app\target\wavy-*-jar-with-dependencies.jar --version

# Check .NET build
dotnet wpf-app\wpf-app.dll --help
```

### Clean Rebuild

```bash
# Remove all build artifacts
rm -r msvc-shared-library/x64
rm -r java-app/target
rm -r wpf-app/bin

# Rebuild from scratch
msbuild msvc-shared-library.sln
mvn -U clean package
dotnet build wpf-app.csproj
```

### Debug Build vs Release

| Aspect | Debug | Release |
|--------|-------|---------|
| **Size** | Larger | Smaller |
| **Performance** | Slower | Faster |
| **Symbols** | Included | Stripped |
| **Use Case** | Development | Production |

---

## Continuous Integration (CI)

### GitHub Actions Example

```yaml
name: Build All Components

on: [push, pull_request]

jobs:
  build-cpp:
    runs-on: windows-latest
    steps:
      - uses: actions/checkout@v3
      - name: Build C++
        run: msbuild msvc-shared-library.sln /p:Configuration=Release /p:Platform=x64

  build-java:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
      - name: Build Java
        run: mvn -f java-app/pom.xml clean package -DskipTests

  build-csharp:
    runs-on: windows-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-dotnet@v3
        with:
          dotnet-version: '8.0.x'
      - name: Build C#
        run: dotnet build wpf-app/wpf-app.csproj -c Release
```

---

**Last Updated**: 2026-03-28  
**For Versions**: Wavy 1.0+, .NET 8.0, Maven 3.9+, MSVC 2019+
