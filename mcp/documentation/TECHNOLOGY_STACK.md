# Technology Stack

## Overview

Wavy uses a multi-language technology stack optimized for signal processing, real-time audio, and cross-platform development.

## Languages & Frameworks

### C++ (Native Core)

| Component | Version | Purpose |
|-----------|---------|---------|
| **Language** | C++11/14 | High-performance signal processing |
| **Compiler** | MSVC 2019+ | Windows platform |
| **Build System** | Visual Studio Solution | Project structure & dependencies |
| **Standard Library** | STL | Containers, threading, algorithms |
| **Threading** | `<thread>`, `<mutex>` | Async pipe worker tasks |

**Key Libraries**:
- `<cmath>`: Mathematical operations
- `<algorithm>`: Standard algorithms
- `<vector>`: Dynamic arrays for buffers

### Java (Legacy Standalone App)

> The Java app is a fully self-contained older initiative — a complete signal processing platform with 20+ pipes, Swing UI, audio I/O, and math utilities. It has no dependency on `wavy.dll`.

| Component | Version | Purpose |
|-----------|---------|---------|
| **Language** | Java 8/11+ | Legacy standalone signal processing application |
| **Build Tool** | Maven 3.9+ | Dependency & build management |
| **JVM Target** | Java 8+ | Compatibility |
| **Package Manager** | Apache Maven Central | Artifact resolution |

**Frameworks & Libraries**:
- **UI**: `javax.swing` (Swing), `java.awt` (AWT)
- **Charting**: `org.jfree:jfreechart:1.5.3`
- **Interop**: `net.java.dev.jna:jna:5.17.0` (RTL-SDR hardware access via librtlsdr — not used for wavy.dll)
- **Serialization**: `org.json:json:20250107` (JSON I/O)
- **Utilities**: `com.google.guava:guava:33.4.8-jre` (collections, utilities)
- **Math**: `net.objecthunter:exp4j:0.4.8` (expression evaluation)
- **Testing**: `junit:junit:4.13.2` (unit tests)

### C# / .NET

| Component | Version | Purpose |
|-----------|---------|---------|
| **Language** | C# 11+ | Modern managed UI development |
| **Framework** | .NET 8.0 (LTS) | Windows desktop application |
| **Platform** | WPF (Windows Presentation Foundation) | Modern UI toolkit |
| **Build Tool** | Visual Studio / dotnet CLI | Project management |
| **Package Manager** | NuGet | Third-party packages |

**Frameworks & Libraries**:
- **UI Framework**: WPF (`System.Windows`)
- **Plotting**: `ScottPlot:5.1.57` (scientific plotting)
- **Interop**: P/Invoke (Windows DLL import)

## BUILD SYSTEMS & TOOLING

### C++ Build Chain

```
Source (C++)
    ↓
MSVC Compiler (cl.exe)
    ↓
Linker (link.exe)
    ↓
DLL: wavy.dll
```

**Configuration**:
- **Solution File**: `msvc-shared-library/msvc-shared-library.sln`
- **Project File**: `msvc-shared-library/msvc-shared-library.vcxproj`
- **Platforms**: x64 (primary)
- **Configurations**: Debug, Release
- **Output Directories**:
  - Debug: `x64/Debug/`
  - Release: `x64/Release/`

**Build Command**:
```batch
msbuild msvc-shared-library.sln /p:Configuration=Release /p:Platform=x64
```

### Java Build Chain

```
Source (Java)
    ↓
Maven (pom.xml)
    ↓
Compiler (javac)
    ↓
Packaging (jar)
    ↓
JAR: wavy-1.0-SNAPSHOT-jar-with-dependencies.jar
```

**Configuration**:
- **POM File**: `java-app/pom.xml`
- **Group ID**: `com.terpomo`
- **Artifact ID**: `wavy`
- **Version**: `1.0-SNAPSHOT`
- **Packaging**: `jar`
- **Main Class**: `com.terpomo.wavy.ui.UIApplication`
- **Output Directory**: `target/`

**Build Commands**:
```bash
# Development build (skip tests)
mvn clean package -DskipTests

# Release build
mvn clean package

# Run JAR
java -jar target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar
```

**Dependencies Resolution**: Maven Central Repository

<details>
<summary><strong>Full Dependency Tree</strong></summary>

```
com.terpomo:wavy:1.0-SNAPSHOT
├── org.jfree:jfreechart:1.5.3
│   ├── org.jfree:jcommon:1.0.23
│   └── ... (transitive)
├── net.java.dev.jna:jna:5.17.0
├── org.json:json:20250107
├── com.google.guava:guava:33.4.8-jre
│   ├── com.google.code.findbugs:jsr305:3.0.2
│   ├── org.checkerframework:checker-qual:3.42.0
│   └── ... (transitive)
├── net.objecthunter:exp4j:0.4.8
└── junit:junit:4.13.2
    └── org.hamcrest:hamcrest-core:1.3
```

</details>

### C# Build Chain

```
Source (C#)
    ↓
Visual Studio / dotnet CLI
    ↓
Roslyn Compiler (csc.exe)
    ↓
IL Compiler & JIT
    ↓
EXE: wpf-app.exe
```

**Configuration**:
- **Project File**: `wpf-app/wpf-app.csproj`
- **Framework**: `net8.0-windows`
- **Output Directory**: `bin/{Debug|Release}/net8.0-windows/`
- **Launch Settings**: `.vscode/launch.json` (if configured)

**Build Commands**:
```bash
# Debug build
dotnet build wpf-app/wpf-app.csproj -c Debug

# Release build
dotnet build wpf-app/wpf-app.csproj -c Release

# Run
./bin/Release/net8.0-windows/wpf-app.exe
```

**NuGet Dependencies**:
- `ScottPlot:5.1.57`
- Framework references: `WindowsDesktop.App.WPF`, `WindowsDesktop.App`

## Platform & OS Support

| Component | OS | Architecture | Status |
|-----------|----|----|---|
| **C++ (wavy.dll)** | Windows | x64 | Production |
| **Java UI** | Windows, Linux, macOS | x64 | Cross-platform (JVM) |
| **C# UI** | Windows | x64 | Windows-only (WPF) |

**Target OS**: Windows 10 / Windows 11 (minimum)
**JDK Version**: Java 8 or higher (recommended: Java 11+ LTS)
**.NET Version**: .NET 8.0 LTS (Windows)

## Development Environment

### Required Tooling

```
┌─────────────────────────────────────────────────────────┐
│                  For C++ Development                    │
├─────────────────────────────────────────────────────────┤
│ • Visual Studio 2019+ (MSVC toolchain)                 │
│ • Windows SDK (10.0.20348 recommended)                 │
│ • CMake 3.20+ (optional, for untitled/ project)       │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│               For Java Development                      │
├─────────────────────────────────────────────────────────┤
│ • JDK 8+ (Eclipse Adoptium / Temurin recommended)      │
│ • Maven 3.9+                                           │
│ • IDE: IntelliJ IDEA or Eclipse                        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              For C# Development                         │
├─────────────────────────────────────────────────────────┤
│ • Visual Studio 2022+ or VS Code                       │
│ • .NET 8.0 SDK                                         │
│ • WPF development pack                                 │
└─────────────────────────────────────────────────────────┘
```

### Environment Variables

```
# For C++ builds
VCINSTALLDIR=<MSVC installation path>
WindowsSDKVersion=10.0.20348

# For Java builds
JAVA_HOME=<JDK installation path>
MAVEN_HOME=<Maven installation path>
PATH=%PATH%;%MAVEN_HOME%\bin

# For C# builds
DOTNET_ROOT=<.NET SDK installation path>
PATH=%PATH%;%DOTNET_ROOT%\bin
```

## Interop Technologies

### JNA (Java Native Access)

**Purpose**: RTL-SDR hardware access in the legacy Java app — **not** used to call `wavy.dll`
**Version**: 5.17.0
**Used in**: `java-app/src/main/java/com/terpomo/wavy/rtl/` only
**Mechanism**: Runtime binding to `librtlsdr` (the RTL-SDR C library) for software-defined radio device support

> The Java app is a **standalone** application with no dependency on the C++ core. JNA appears in its `pom.xml` exclusively for RTL-SDR hardware access via `librtlsdr`. It does **not** bridge Java to `wavy.dll`.

**Example (RTL-SDR only)**:
```java
// JNA binding to librtlsdr — NOT to wavy.dll
RTLSDRDevice device = Native.load("rtlsdr", RTLSDRDevice.class);
device.rtlsdr_open(deviceIndex);
```

### P/Invoke (.NET Interop)

**Purpose**: Bridge between C# and C++ DLL
**Mechanism**: 
- Compile-time declarations via `[DllImport]`
- Managed/unmanaged marshalling layer
- Platform-specific calling conventions
**Configuration**: DLL location in PATH or bundled

**Example**:
```csharp
[DllImport("wavy.dll", CallingConvention = CallingConvention.Cdecl)]
public static extern IntPtr Pipe_init(string pipeName);

IntPtr handle = Pipe_init("ConstantWavePipe");
```

## Dependency Versions Summary

| Dependency | Version | Language | Purpose |
|------------|---------|----------|---------|
| JFreeChart | 1.5.3 | Java | Real-time charting |
| JNA | 5.17.0 | Java | RTL-SDR hardware access (librtlsdr) |
| JSON | 20250107 | Java | Serialization |
| Guava | 33.4.8-jre | Java | Utilities |
| exp4j | 0.4.8 | Java | Math expressions |
| JUnit | 4.13.2 | Java | Testing |
| ScottPlot | 5.1.57 | C# | Scientific plotting |
| .NET | 8.0 LTS | C# | Framework |
| MSVC | 2019+ | C++ | Compiler |
| Windows SDK | 10.0.20348+ | C++ | Platform APIs |

## Performance Characteristics

| Layer | Expected Throughput | Latency |
|-------|---------------------|---------|
| **C++ Native Processing** | Real-time audio (>= 44.1 kHz) | < 100 microseconds per frame |
| **JNA/P/Invoke Boundary** | Cross-process communication | ~1-10 milliseconds |
| **Java/C# UI Updates** | 60 FPS refresh (typical) | ~16 milliseconds |

---

**Last Updated**: 2026-03-28  
**For Versions**: Wavy 1.0+, .NET 8.0 LTS, Java 8+
