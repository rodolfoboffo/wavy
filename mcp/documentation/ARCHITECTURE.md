# Wavy Project Architecture

## Overview

**Wavy** is a three-tier signal processing platform with a node-based ("pipe") architecture for audio synthesis, filtering, modulation, and real-time visualization.

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                    │
├──────────────────────────┬──────────────────────────────┤
│  Java Swing UI           │  C# WPF UI (.NET8)          │
│  - UIApplication         │  - App.xaml / App.xaml.cs   │
│  - MainWindow            │  - Workspace                │
│  - ProjectRepr           │  - Project                  │
│  - Graph Editor          │  - Alternative UI           │
└──────────────┬───────────┴────────────────┬─────────────┘
               │ JNA                        │ P/Invoke
               │ (Java Native Access)       │ (.NET Interop)
┌──────────────┴────────────────────────────┴─────────────┐
│               APPLICATION LOGIC LAYER                    │
├────────────────────────────────────────────────────────┤
│  Flow Management  │  Pipe Implementations  │ Marshalling │
│  - Workspace      │  - 15+ Pipe types     │  - JSON I/O │
│  - Project        │  - Signal sources     │  - Serialize│
│  - Port system    │  - Filters            │  - Persist  │
│  - Event model    │  - Operators          │             │
│                  │  - Monitors           │             │
└──────────────┬───────────────────────────┬─────────────┘
               │ C ABI (extern "C")        │
               │ Platform: Windows         │
┌──────────────┴────────────────────────────┴─────────────┐
│                  NATIVE LAYER (C++)                      │
├────────────────────────────────────────────────────────┤
│  Pipe System            │ Math Module                   │
│  - Pipe (base class)    │ - FFT algorithms             │
│  - Port (threaded)      │ - SineTable (lookup)         │
│  - Buffer (thread-safe) │ - ValuedTable (generic)      │
│  - Async worker tasks   │ - Constants (PI, etc)        │
│  - Export: wavy.dll     │                              │
└────────────────────────────────────────────────────────┘
```

## Component Breakdown

### 1. Presentation Layer

#### Java Swing UI
- **Package**: `com.terpomo.wavy.ui`
- **Entry Point**: `UIApplication.java`
- **Key Classes**:
  - `MainWindow`: Main application frame with menu bar and toolbar
  - `ProjectRepr`: Canvas for visualizing pipes and connections
  - `AbstractPipeRepr<T>`: Base class for pipe UI representations
  - `PortRepr`: Clickable ports for creating connections
- **Charting**: JFreeChart 1.5.3 for real-time waveform and FFT displays
- **Responsibilities**:
  - Graph-based editor for drawing pipe networks
  - Drag-and-drop pipe placement
  - Visual connection between ports
  - Real-time oscilloscope and spectrum analysis

#### C# WPF UI
- **Framework**: WPF (.NET8-windows)
- **Entry Point**: `App.xaml.cs`
- **Namespaces**:
  - `Flow`: `Workspace`, `Project` (same concepts as Java)
  - `Pipes`: P/Invoke wrappers for C++ pipes
  - `Core`: `AppController` (singleton)
  - `UI`: WPF UI components
- **Charting**: ScottPlot 5.1.57 for scientific plotting
- **Purpose**: Cross-platform alternative UI, demonstrating UI independence from C++ core

### 2. Application Logic Layer

#### Flow Management (Java)
- **Package**: `com.terpomo.wavy.flow`
- **Hierarchy**: `Workspace` → `Project` → `Pipe` → `Port`
- **Data Types**:
  - `Workspace`: Container for multiple projects
  - `Project`: Collection of pipes with add/remove events
  - `InputPort` / `OutputPort`: Typed connection points
  - `Signal`: Abstract base for all signal types
- **Event Model**: Observer pattern for UI updates

#### Pipe Implementations (Java)
- **Package**: `com.terpomo.wavy.pipes` (15+ pipe types)

| Category | Examples |
|----------|----------|
| **Sources** | ConstantWavePipe, NoisePipe, FileReaderPipe, MicPipe, RTLPipe |
| **Filters** | BandPassFilterPipe |
| **Operators** | CombinationPipe, MultiplicationPipe, ResamplingPipe, FMModulationPipe, IQModulationPipe, IQDemodulationPipe |
| **Monitors** | OscilloscopePipe, FFTPipe |
| **Output** | AudioPlayerPipe, FileWriterPipe |

#### Marshalling (Java)
- **Package**: `com.terpomo.wavy.marshal`
- **Format**: JSON
- **Purpose**: Save/load signal processing graphs to disk
- **Location**: `saved-projects/` directory with examples (filter.json, fm.json, etc.)

#### P/Invoke Wrappers (C#)
- **Path**: `wpf-app/Pipes/`, `wpf-app/Flow/`
- **Purpose**: Managed interfaces to unmanaged C++ DLL
- **Classes**:
  - `Pipe`: Wrapper around native Pipe implementation
  - `Port`: Wrapper around native Port
  - `AppController`: Manages pipe creation and interop

### 3. Native Layer (C++)

#### Pipe Architecture
- **File**: `msvc-shared-library/pipe.h`, `pipe.cpp`
- **Design Pattern**: Base class with virtual worker task
- **Threading**: Each pipe runs async `workerTask()` in its own thread
- **Lifetime**: `init()` → running state → `shutdown()`
- **C ABI Export**: All public methods prefixed with `Pipe_` and wrapped in `extern "C"`

#### Port System
- **File**: `msvc-shared-library/port.h`, `port.cpp`
- **Types**: `InputPort`, `OutputPort`
- **Connection**: Ports link to other ports via `setLinkedPort()` / `getLinkedPort()`
- **Data Transfer**: Via `Buffer` (circular, thread-safe)
- **Thread Safety**: Mutex protection for concurrent reads/writes

#### Buffer Management
- **File**: `msvc-shared-library/pipes/buffer.h`, `buffer.cpp`
- **Type**: Lock-free circular buffer
- **Purpose**: Inter-pipe communication with minimal latency
- **Consumers**: Audio pipes, data analysis pipes

#### Math Module
- **Location**: `msvc-shared-library/math/`
- **Exports**:
  - `constants.h`: PI, PI2, HALF_PI
  - `Point`: 2D coordinate class
  - `SineTable`: Pre-computed sine lookup table with linear interpolation
  - `ValuedTable<T>`: Generic lookup table template

## Intercomponent Communication

### Java ↔ C++ (via JNA)

```
Java Code                    JNA Bridge              C++ DLL
─────────────────────────────────────────────────────────
Pipe pipe = new ConstantWavePipe()
                ────────→  Marshalling  ────────→  Pipe::init()
                                                   Returns: handle (IntPtr)
pipe.setFrequency(1000)
                ────────→  [frequency]  ────────→  Pipe::setFrequency(1000)
                
pipe.getOutputPort()
                ────────→  Port lookup  ←────────  OutputPort* returned
                ←──────────  Wrapping  ←──────────
```

- **Library**: JNA 5.17.0
- **Pattern**: Callback-driven, synchronous method calls
- **Data Types**: Basic types passed by value, objects via native pointers
- **Error Handling**: Exceptions mapped from C++ error codes

### C# ↔ C++ (via P/Invoke)

```
C# Managed Code             P/Invoke Stub           C++ DLL
─────────────────────────────────────────────────────────
var pipe = new Pipe("ConstantWavePipe")
                ────────→  [DllImport]  ────────→  Pipe::init()
                                                   Returns: handle (IntPtr)
pipe.SetFrequency(1000)
                ────────→  Marshal args ────────→  Pipe::setFrequency(1000)
```

- **Mechanism**: P/Invoke (.NET Windows interop)
- **Declaration**: `[DllImport("wavy.dll")]`
- **Marshalling**: Automatic struct/array conversion
- **Thread Safety**: Native locks with managed synchronization

## Data Flow at Runtime

### Signal Processing Pipeline

```
1. User creates pipe graph in UI
   ↓
2. Pipes initialized in C++ (threads start)
   ↓
3. Source pipe generates data → OutputPort → Buffer
   ↓
4. Connected pipe reads from Buffer → InputPort → processes
   ↓
5. Output pipe writes to Buffer or speaker
   ↓
6. Monitor pipes (Oscilloscope, FFT) pull data for visualization
   ↓
7. UI receives updates via event model, redraw charts
```

### Thread Model

- **Main Thread**: UI event loop (Java Swing or WPF)
- **Worker Threads**: One per pipe, launched in `init()`, runs `workerTask()` loop
- **Synchronization**: Mutex-protected buffers between pipes
- **Real-time Capability**: Lock-free buffers minimize latency

## Key Design Patterns

| Pattern | Usage |
|---------|-------|
| **Observer** | UI updates when project/workspace changes |
| **Strategy** | Different pipe types = different processing strategies |
| **Factory** | Pipe instantiation by name (reflection in Java, registry in C#) |
| **Adapter** | JNA/P/Invoke adapters bridge managed/unmanaged boundaries |
| **Repository** | Marshaller persists project to JSON |
| **Singleton** | AppController, Workspace (single workspace per session) |

## Build Outputs

| Component | Output | Size | Type |
|-----------|--------|------|------|
| C++ | `wavy.dll` | ~5-10 MB | Native library |
| Java | `wavy-1.0-SNAPSHOT-jar-with-dependencies.jar` | ~50-100 MB | Executable JAR |
| C# | `wpf-app.exe` | ~20-50 MB | WPF executable |

## Summary

Wavy's architecture separates concerns into managed, high-level UI layers (Java/C#) and a high-performance native core (C++). The pipe-based signal graph pattern enables users to compose complex DSP workflows, while the layered design allows multiple UI implementations using the same core engine.
