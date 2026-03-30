# MCP Quick Start Guide

## What is this MCP Setup?

This Model Context Protocol (MCP) configuration enables AI assistants like Claude to understand and navigate the **Wavy** signal processing project efficiently. It includes:

- **Architecture documentation** - How components interact
- **Symbol indexes** - All classes, functions, and methods by language
- **Build information** - Compilation and deployment guides
- **Data models** - JSON schemas for persistence
- **Tool definitions** - AI-friendly interfaces for code navigation

---

## Key Resources

### Start Here

1. **[ARCHITECTURE.md](ARCHITECTURE.md)** - Architecture overview (new C++/WPF stack + legacy Java)
2. **[TECHNOLOGY_STACK.md](TECHNOLOGY_STACK.md)** - Languages, frameworks, versions
3. **[BUILD_GUIDE.md](BUILD_GUIDE.md)** - How to build all components

### For Developers

4. **[DATA_MODELS.md](DATA_MODELS.md)** - JSON schemas and pipe catalog
5. **[../resources/cpp-index.json](../resources/cpp-index.json)** - C++ symbol reference
6. **[../resources/java-index.json](../resources/java-index.json)** - Java symbol reference
7. **[../resources/csharp-index.json](../resources/csharp-index.json)** - C# symbol reference

### Configuration Files

- **[../server.json](../server.json)** - MCP server capabilities and resources
- **[../codebase-schema.json](../codebase-schema.json)** - Project structure and modules
- **[../tools/tool-definitions.json](../tools/tool-definitions.json)** - AI tool schemas

---

## Core Concepts

### The Pipe Architecture

Wavy uses a **node-based signal processing graph**:

```
Signal Generation (Sources)
    ↓
Signal Processing (Filters, Operators)  
    ↓
Analysis & Monitoring (Oscilloscope, FFT)
    ↓
Output (Speaker, File, Network)
```

**Key Classes**:
- `Pipe` - Processing node (base class in C++)
- `Port` - Data connection point (Input/Output)
- `Buffer` - Thread-safe data transfer between pipes
- `Project` - Collection of pipes with connections (Java/C#)

### Two Parallel Tracks

| Track | Technology | Purpose |
|-------|-----------|---------|
| **New — UI** | C# WPF (.NET 8) | Modern user interface |
| **New — Core** | C++ DLL (`wavy.dll`) | Real-time signal processing |
| **Legacy** | Java Swing (standalone) | Older complete implementation, no C++ dependency |

### Interoperability

- **C# ↔ C++**: P/Invoke (.NET Windows interop) via C ABI exports (`extern "C"`)
- **Java app**: Standalone — no connection to `wavy.dll`. JNA used only for RTL-SDR hardware (`librtlsdr`)

---

## Common Tasks

### 1. Adding a New Pipe Type

**Files to modify**:
1. Create implementation in `java-app/src/main/java/com/terpomo/wavy/pipes/`
2. Add UI representation (extends `AbstractPipeRepr<T>`)
3. Register in `PipeFactory`
4. Add marshalling support in `PipeMarshaller`

**Required methods**:
- `getInputPortCount()` and `getOutputPortCount()`
- `process()` or `update()` for signal handling

### 2. Building the Project

### Full Recompile (Recommended)

Automated full build with C++ DLL copy and WPF execution:

```powershell
.\mcp\scripts\full-recompile.ps1               # Build + Run (Debug)
.\mcp\scripts\full-recompile.ps1 -Configuration Release  # Release build
.\mcp\scripts\full-recompile.ps1 -NoRun        # Build without running
```

**Steps executed**:
1. Build C++ DLL (Debug x64)
2. Copy wavy.dll to WPF output directory
3. Build WPF Application
4. Run WPF Application

See [FULL_RECOMPILE.md](FULL_RECOMPILE.md) for details.

### Component-by-Component Build

**C++ only** (dependency for Java/C#):
```powershell
$msbuild = "C:\Program Files\Microsoft Visual Studio\2022\Community\MSBuild\Current\Bin\MSBuild.exe"
& $msbuild msvc-shared-library\msvc-shared-library.sln /p:Configuration=Release /p:Platform=x64
```

**Java**:
```powershell
cd java-app
mvn clean package -DskipTests
```

**C#**:
```powershell
cd wpf-app
dotnet build wpf-app.csproj -c Release
```

### 3. Running the Application

**Java UI**:
```bash
java -jar java-app/target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar
```

**C# WPF UI**:
```bash
./wpf-app/bin/Release/net8.0-windows/wpf-app.exe
```

**Note**: Place `wavy.dll` in same directory or in PATH

### 4. Understanding a Pipe Type

Use the [pipe-catalog.json](../resources/pipe-catalog.json) to find:
- Input/output port counts
- Parameter names and ranges
- Default values
- Common signal chains using that pipe

Example: `ConstantWavePipe` generates sinusoidal waves (FM carrier, test signals)

### 5. Debugging JNA/P/Invoke Issues

**Check**:
1. `wavy.dll` is in process PATH
2. C++ exports match Java/C# declarations
3. Calling conventions match (Cdecl for C++)
4. Pointer marshalling is correct

**Debug**: Enable verbose JNA logging (set `jna.debug` environment variable)

---

## File Organization

```
mcp/
├── server.json                      # MCP server capabilities
├── codebase-schema.json             # Project structure definition
├── documentation/
│   ├── ARCHITECTURE.md              # Component and layer diagram
│   ├── TECHNOLOGY_STACK.md          # Languages and frameworks
│   ├── BUILD_GUIDE.md               # Build instructions
│   ├── DATA_MODELS.md               # JSON schemas
│   └── QUICKSTART.md                # This file
├── resources/
│   ├── cpp-index.json               # C++ class/function index
│   ├── java-index.json              # Java package/class index
│   ├── csharp-index.json            # C# namespace/class index
│   ├── build-dependencies.json      # Build system info
│   └── pipe-catalog.json            # All 15+ pipe types
└── tools/
    └── tool-definitions.json        # AI tool schemas
```

---

## Development Workflow

### For Java Development

1. Open `java-app/` as Maven project
2. Use IDE: IntelliJ IDEA or Eclipse
3. Build: `mvn clean package`
4. Run: `java -jar target/wavy-*.jar`

### For C++ Development

1. Open `msvc-shared-library/msvc-shared-library.sln`
2. Set platform: x64
3. Build: Visual Studio UI or `msbuild` CLI
4. DLL appears in `x64/Debug/` or `x64/Release/`

### For C# Development

1. Open `wpf-app/wpf-app.csproj`
2. Use Visual Studio 2022 or VS Code
3. Build: `dotnet build -c Release`
4. EXE appears in `bin/Release/net8.0-windows/`

### Debugging

- **Java**: Breakpoints in IDE, attach JDWP debugger
- **C++**: MSVC debugger in Visual Studio
- **C#**: Visual Studio debugger, break on CLR exceptions

---

## Troubleshooting Checklist

| Issue | Solution |
|-------|----------|
| "JNA cannot load librtlsdr" | Ensure librtlsdr is installed and in PATH (JNA is for RTL-SDR hardware, not wavy.dll) |
| "MSVC compiler not found" | Install Visual Studio with C++ workload |
| ".NET 8.0 not installed" | Download from dotnet.microsoft.com |
| "Maven dependency resolution failed" | Check internet connection, run `mvn dependency:resolve` |
| "P/Invoke calling convention mismatch" | Ensure `CallingConvention.Cdecl` in C# declarations |
| "Pipe not recognized in project" | Check `PipeFactory` registration in Java/C# |

---

## Resources

- **Official Docs**: [Wavy Project Documentation](../ARCHITECTURE.md)
- **Saved Projects**: [saved-projects/](../../saved-projects/) - Example workflows
- **Build System**: [BUILD_GUIDE.md](BUILD_GUIDE.md) - Detailed build instructions
- **API Reference**: [cpp-index.json](../resources/cpp-index.json), [java-index.json](../resources/java-index.json)

---

## Next Steps

1. **Understand the architecture**: Read [ARCHITECTURE.md](ARCHITECTURE.md)
2. **Set up build environment**: Follow [BUILD_GUIDE.md](BUILD_GUIDE.md)
3. **Explore a saved project**: Open `filter.json` or `fm.json`
4. **Build your first signal chain**: Combine ConstantWavePipe → BandPassFilterPipe → FFTPipe
5. **Modify a pipe**: Add a new parameter or signal processing algorithm

---

**Last Updated**: 2026-03-28  
**For Version**: Wavy 1.0+
