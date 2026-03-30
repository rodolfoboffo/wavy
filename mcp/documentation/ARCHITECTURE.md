# Wavy Project Architecture

## Overview

**Wavy** is a signal processing platform with a node-based ("pipe") architecture for audio synthesis, filtering, modulation, and real-time visualization.

The project has **two parallel tracks**:

| Track | Status | Stack |
|-------|--------|-------|
| **New stack** | Active / work in progress | C++ core (`wavy.dll`) + C# WPF frontend |
| **Legacy Java app** | Feature-complete, standalone | Pure Java (Swing UI + 20+ pipe implementations) |

These two tracks are **independent** — there is no connection between the Java app and the C++ DLL.

---

## New Stack: C++ + WPF

```
┌──────────────────────────────────────────────┐
│              C# WPF Frontend (.NET 8)         │
│  - Workspace / Project / AppController        │
│  - Pipe graph editor UI                       │
│  - ConstantValuePipe wrapper                  │
│  - OscilloscopePipe wrapper                   │
│  - ScottPlot charts                           │
└──────────────────┬───────────────────────────┘
                   │ P/Invoke (Windows DLL import)
                   │ [DllImport("wavy.dll")]
┌──────────────────┴───────────────────────────┐
│              C++ Core (wavy.dll)              │
│  - Pipe (base class, threaded worker)         │
│  - InputPort / OutputPort                     │
│  - Buffer<T> (thread-safe circular buffer)    │
│  - ConstantValuePipe                          │
│  - OscilloscopePipe                           │
│  - Math: SineTable, ValuedTable, constants    │
│  Export: C ABI (extern "C")                   │
└──────────────────────────────────────────────┘
```

**Interop**: WPF calls C++ via P/Invoke. C++ exports a flat C ABI (`Pipe_init`, `Pipe_shutdown`, `Port_getName`, etc.) for compatibility with managed runtimes.

### Current C++ Pipes (work in progress)

| Pipe | Description |
|------|-------------|
| `ConstantValuePipe` | Outputs a constant float value |
| `OscilloscopePipe` | Captures waveform samples for visualization |

More pipes are being added to C++ over time to replace or complement the Java legacy implementations.

### C++ Component Breakdown

#### Pipe System (`msvc-shared-library/pipes/`)
- **`Pipe`** (`pipe.h/cpp`) — Base class; each pipe runs `workerTask()` in its own thread
- **`InputPort` / `OutputPort`** (`port.h/cpp`) — Connection points; linked via `setLinkedPort()`
- **`Buffer<T>`** (`buffer.h/cpp`) — Thread-safe circular buffer for inter-pipe communication

#### Math Module (`msvc-shared-library/math/`)
- `constants.h` — PI, PI2, HALF_PI
- `SineTable` — Pre-computed sine lookup table with linear interpolation
- `ValuedTable<T>` — Generic lookup table template
- `Point` — 2D coordinate struct

#### WPF Wrappers (`wpf-app/Pipes/`)
- `Pipe.cs` — Abstract P/Invoke bindings to wavy.dll
- `ConstantValuePipe.cs`, `OscilloscopePipe.cs` — Concrete wrappers
- `Port.cs` — Managed wrapper for native Port

---

## Legacy Track: Java Standalone App

```
┌──────────────────────────────────────────────┐
│           Java Swing UI (JFreeChart)          │
│  - UIApplication (entry point)                │
│  - MainWindow (JFrame)                        │
│  - ProjectRepr (pipe graph canvas)            │
│  - AbstractPipeRepr<T> (per-pipe UI panels)   │
└──────────────────┬───────────────────────────┘
                   │ Pure Java (no native calls)
┌──────────────────┴───────────────────────────┐
│         Java Flow & Pipe Implementations      │
│  - Workspace → Project → Pipe → Port          │
│  - 20+ pipe implementations (sources,         │
│    filters, operators, monitors, output)       │
│  - JSON marshalling (save/load graphs)         │
│  - Audio codecs (LPCM)                        │
│  - Math utilities (FFT, expression eval)       │
└──────────────────────────────────────────────┘
         │ JNA (for RTL-SDR hardware only)
┌────────┴─────────────────────────────────────┐
│         librtlsdr (external C library)        │
│  RTL-SDR software-defined radio hardware      │
└──────────────────────────────────────────────┘
```

> **Note on JNA**: The Java app uses JNA (`net.java.dev.jna:jna:5.17.0`) exclusively to interface with `librtlsdr` for SDR hardware support (`RTLPipe`). JNA is **not** used to call `wavy.dll` — there is no Java ↔ C++ core integration.

### Java Pipe Categories

| Category | Pipes |
|----------|-------|
| **Sources** | ConstantValuePipe, ConstantWavePipe, NoisePipe, FileReaderPipe, MicPipe, RTLPipe |
| **Filters** | BandPassFilterPipe |
| **Operators** | CombinationPipe, MultiplicationPipe, SplitterPipe, ResamplingPipe, FMModulationPipe, FMDemodulationPipe, IQModulationPipe, IQDemodulationPipe |
| **Monitors** | OscilloscopePipe, FFTPipe |
| **Output** | AudioPlayerPipe, FileWriterPipe |

---

## Data Flow at Runtime

### New Stack (C++ / WPF)

```
1. User creates pipe graph in WPF UI
   ↓
2. WPF calls Pipe_init("ConstantValuePipe") via P/Invoke → C++ creates pipe + thread
   ↓
3. Source pipe outputs data → OutputPort → Buffer
   ↓
4. Connected pipe reads Buffer → InputPort → processes
   ↓
5. Monitor pipe (OscilloscopePipe) captures samples → WPF reads them for chart update
```

### Legacy Java App

```
1. User creates pipe graph in Swing UI
   ↓
2. Java instantiates pipe objects directly (no native calls)
   ↓
3. Worker threads call doWork() loop per pipe
   ↓
4. Signals flow through GenericBuffer / SignalBuffer between pipes
   ↓
5. Monitor pipes push data to UI via Observer events → JFreeChart redraws
```

---

## Thread Model

| Layer | Threading |
|-------|-----------|
| **C++ pipes** | One `std::thread` per pipe, mutex-protected `Buffer<T>` |
| **WPF UI** | Main WPF dispatcher thread + P/Invoke calls |
| **Java pipes** | One `Worker` thread per pipe, `synchronized` buffer access |
| **Java UI** | Swing EDT for rendering, Observer events for updates |

---

## Key Design Patterns

| Pattern | Usage |
|---------|-------|
| **Strategy** | Different pipe types = different processing strategies |
| **Observer** | Java UI updates when project/workspace state changes |
| **Factory** | Pipe instantiation by name (C++ registry, Java reflection) |
| **Adapter** | P/Invoke adapters bridge WPF managed ↔ C++ unmanaged |
| **Repository** | Java Marshaller persists project graphs to JSON |
| **Singleton** | Java Workspace; C# AppController |

---

## Build Outputs

| Component | Output | Notes |
|-----------|--------|-------|
| **C++** | `msvc-shared-library/x64/Debug/wavy.dll` | New stack core |
| **C# WPF** | `wpf-app/bin/Debug/net8.0-windows/wpf-app.exe` | New stack UI |
| **Java** | `java-app/target/wavy-1.0-SNAPSHOT-jar-with-dependencies.jar` | Legacy standalone |
