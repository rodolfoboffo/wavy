# Wavy Project MCP (Model Context Protocol) Setup

## Overview

This directory contains a comprehensive Model Context Protocol (MCP) configuration for the **Wavy** signal processing project. The MCP enables AI assistants (like Claude) to efficiently understand and navigate the full-stack codebase across C++, Java, and C# components.

## What's Included

### 📋 Configuration Files

- **`server.json`** - MCP server capabilities, resources, and tools
- **`codebase-schema.json`** - Complete project structure and module definitions

### 📚 Documentation

| File | Purpose |
|------|---------|
| **`documentation/ARCHITECTURE.md`** | Three-tier architecture, component interaction, data flow |
| **`documentation/TECHNOLOGY_STACK.md`** | Languages, frameworks, versions, dependencies |
| **`documentation/BUILD_GUIDE.md`** | Comprehensive build instructions for all components |
| **`documentation/DATA_MODELS.md`** | JSON schemas, data types, project persistence |
| **`documentation/QUICKSTART.md`** | Getting started guide and common tasks |

### 🔍 Symbol Indexes

- **`resources/cpp-index.json`** - C++ classes, functions, and exports (Pipe, Port, etc.)
- **`resources/java-index.json`** - Java packages, classes, and methods (15+ pipe types)
- **`resources/csharp-index.json`** - C# namespaces, wrappers, and P/Invoke declarations

### 📦 Resources

- **`resources/build-dependencies.json`** - Build systems, toolchains, dependencies
- **`resources/pipe-catalog.json`** - All 15+ pipe types with parameters and examples

### 🛠️ Tools

- **`tools/tool-definitions.json`** - AI tool schemas for code navigation and building

### ⚙️ VSCode Integration

- **`.vscode/settings.json`** - Language servers, formatters, MCP references
- **`.vscode/tasks.json`** - Build tasks for C++, Java, C# (debug, release, run)
- **`.vscode/launch.json`** - Debug configurations for all languages
- **`.vscode/extensions.json`** - Recommended extensions

---

## Quick Start

### 1. View the Architecture
```bash
cat documentation/ARCHITECTURE.md
```

### 2. Build All Components
```powershell
# Open tasks palette in VSCode (Ctrl+Shift+B)
# Select: Build All Components
```

### 3. Run Java Application
```bash
java -jar java-app/target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### 4. Run C# Application
```bash
./wpf-app/bin/Debug/net8.0-windows/wpf-app.exe
```

---

## Using the MCP with Claude

The configuration enables Claude to:

✅ **Navigate the codebase** efficiently using symbol indexes by language  
✅ **Understand architecture** with multi-layer diagrams and component relationships  
✅ **Build and compile** with pre-configured build tasks and commands  
✅ **Explore data models** with JSON schemas and serialization formats  
✅ **Debug interop issues** with JNA/P/Invoke documentation  
✅ **Extend functionality** with common patterns for adding new pipes  

### Example Queries Claude Can Handle

- "What's the architecture of Wavy?"
- "How do I add a new pipe type?"
- "Show me the ConstantWavePipe implementation"
- "Build the Java application"
- "What are the inputs/outputs of FMModulationPipe?"
- "How does Java communicate with the C++ DLL?"

---

## Directory Structure

```
mcp/
├── server.json                              # MCP server definition
├── codebase-schema.json                     # Project schema
├── documentation/
│   ├── ARCHITECTURE.md                      # Component architecture
│   ├── TECHNOLOGY_STACK.md                  # Tech stack details
│   ├── BUILD_GUIDE.md                       # Build instructions
│   ├── DATA_MODELS.md                       # Data schemas
│   └── QUICKSTART.md                        # Getting started
├── resources/
│   ├── cpp-index.json                       # C++ symbol index
│   ├── java-index.json                      # Java symbol index
│   ├── csharp-index.json                    # C# symbol index
│   ├── build-dependencies.json              # Build info
│   └── pipe-catalog.json                    # Pipe catalog
└── tools/
    └── tool-definitions.json                # Tool schemas
```

---

## Key Concepts

### The Pipe Architecture

Wavy implements a **node-based signal processing graph**:

```
[Signal Source] → [Processing Nodes] → [Analysis] → [Output]
  (ConstantWave)   (Filters, Operators)  (FFT)      (Speaker)
```

**Core Classes**:
- `Pipe` - Processing node (threaded, C++)
- `Port` - Data connection (Input/Output)
- `Project` - Collection of pipes with events (Java/C#)

### Three-Tier Architecture

| Layer | Technology | Role |
|-------|-----------|------|
| **UI Tier** | Java Swing / C# WPF | User interaction, visualization |
| **Logic Tier** | Java / C# | Workflow, event management, marshalling |
| **Core Tier** | C++ DLL | Real-time signal processing |

### Interoperability

- **Java ↔ C++**: JNA (Java Native Access)
- **C# ↔ C++**: P/Invoke (Windows .NET interop)

---

## Building the Project

### Prerequisites

Ensure you have installed:
- **Visual Studio 2019+** with C++ workload (for C++ DLL)
- **JDK 8+** and **Maven 3.9+** (for Java)
- **.NET 8.0 SDK** (for C#)

### Build All Components

**PowerShell**:
```powershell
# Build C++ (generates wavy.dll)
cd msvc-shared-library
msbuild msvc-shared-library.sln /p:Configuration=Release /p:Platform=x64

# Build Java
cd ../java-app
mvn clean package -DskipTests

# Build C#
cd ../wpf-app
dotnet build wpf-app.csproj -c Release
```

**Or use VSCode tasks**: Ctrl+Shift+B → "Build All Components"

---

## File Reference

### Documentation

| Document | Read Time | Purpose |
|----------|-----------|---------|
| QUICKSTART | 5 min | Overview and common tasks |
| ARCHITECTURE | 10 min | Understand component design |
| TECHNOLOGY_STACK | 10 min | Languages, versions, dependencies |
| BUILD_GUIDE | 15 min | Detailed build and deployment |
| DATA_MODELS | 10 min | JSON serialization, pipe types |

### Indexes

| Index | Type | Size |
|-------|------|------|
| cpp-index.json | C++ classes, functions | 10 KB |
| java-index.json | Java packages, classes | 20 KB |
| csharp-index.json | C# namespaces, classes | 12 KB |
| build-dependencies.json | Build system info | 8 KB |
| pipe-catalog.json | All 15 pipe types | 18 KB |

---

## Integration with AI Assistants

### For Claude / GPT-4 Sonnet

1. **Point to `mcp/server.json`** for capability discovery
2. **Reference `codebase-schema.json`** for project overview
3. **Use symbol indexes** for code location queries
4. **Check `pipe-catalog.json`** for signal processing questions
5. **Follow `BUILD_GUIDE.md`** for compilation help

### Example Claude Prompt

```
I want to understand the Wavy project. 
Please:
1. Summarize the architecture (see mcp/documentation/ARCHITECTURE.md)
2. List all available pipe types (see mcp/resources/pipe-catalog.json)
3. Show me how to build the Java application (see mcp/documentation/BUILD_GUIDE.md)
```

---

## Contributing

When modifying the project:

1. **Update relevant indexes** if adding new classes/functions
2. **Update `pipe-catalog.json`** if adding new pipe types
3. **Update `codebase-schema.json`** if modifying architecture
4. **Update build guides** if dependencies or build process changes

---

## Troubleshooting

### "MCP resources not loading"
- Ensure file paths are correct (use absolute paths if needed)
- Verify JSON files are valid: `jsonlint resources/*.json`

### "Symbol not found in index"
- Check symbol spelling
- Verify the file is listed in the index
- Regenerate indexes if files were added

### "Build tasks not working"
- Ensure environment variables are set (JAVA_HOME, MAVEN_HOME, etc.)
- Verify required tools are installed: `mvn -v`, `dotnet --version`, `msbuild`
- Check `.vscode/tasks.json` paths match your setup

---

## Version Info

- **Wavy Version**: 1.0+
- **MCP Created**: 2026-03-28
- **Target Frameworks**:
  - C++: MSVC 2019+
  - Java: 8+
  - C#: .NET 8.0
  - Build Systems: Maven 3.9+, dotnet CLI, MSVC

---

## Support

For questions about the MCP setup or project structure:
1. Check [QUICKSTART.md](documentation/QUICKSTART.md)
2. Review [ARCHITECTURE.md](documentation/ARCHITECTURE.md)
3. See [BUILD_GUIDE.md](documentation/BUILD_GUIDE.md) for environment issues

---

**Last Updated**: 2026-03-28  
**Maintained By**: Wavy Development Team  
**License**: [Project License]
