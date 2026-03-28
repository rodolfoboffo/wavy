# Data Models & Serialization

## Overview

Wavy uses **JSON** as the primary serialization format for persisting projects, pipes, and signal configurations. All project data can be saved and loaded from disk, enabling reproducible signal processing workflows.

## JSON Project Schema

### Top-Level Project Object

```json
{
  "name": "My Signal Processing Project",
  "class": "com.terpomo.wavy.flow.Project",
  "description": "Example project demonstrating data model",
  "created": "2026-03-28T10:30:00Z",
  "pipes": [
    {}
  ],
  "connections": []
}
```

### Pipe Schema

```json
{
  "name": "Pipe0",
  "class": "com.terpomo.wavy.pipes.sources.ConstantWavePipe",
  "type": "source",
  "instanceId": "pipe-uuid-12345",
  "location": {
    "x": 50,
    "y": 100,
    "class": "com.terpomo.wavy.util.Point"
  },
  "parameters": {
    "frequency": 1000.0,
    "amplitude": 1.0,
    "phase": 0.0,
    "sampleRate": 44100
  },
  "inputPorts": [
    {
      "name": "modulation",
      "index": 0,
      "type": "FLOAT",
      "linkedPortId": null
    }
  ],
  "outputPorts": [
    {
      "name": "output",
      "index": 0,
      "type": "FLOAT",
      "linkedPortId": "pipe-uuid-67890:1"
    }
  ]
}
```

### Port Schema

```json
{
  "name": "output",
  "index": 0,
  "direction": "OUTPUT",
  "dataType": "FLOAT",
  "bufferSize": 4096,
  "linkedPort": {
    "pipeId": "pipe-uuid-67890",
    "portIndex": 1,
    "portName": "input"
  }
}
```

### Connection Schema

```json
{
  "id": "conn-uuid-xxxxx",
  "sourcePort": {
    "pipeId": "pipe-uuid-12345",
    "portIndex": 0,
    "portName": "output"
  },
  "targetPort": {
    "pipeId": "pipe-uuid-67890",
    "portIndex": 1,
    "portName": "input"
  }
}
```

---

## Pipe Type Catalog

### Source Pipes

#### 1. ConstantWavePipe

Generates a sinusoidal wave at specified frequency.

```json
{
  "class": "com.terpomo.wavy.pipes.sources.ConstantWavePipe",
  "type": "source",
  "parameters": {
    "frequency": 1000.0,
    "amplitude": 1.0,
    "phase": 0.0,
    "sampleRate": 44100
  },
  "inputPorts": 0,
  "outputPorts": 1
}
```

**Properties**:
- `frequency` (double): Wave frequency in Hz (default: 1000)
- `amplitude` (float): Peak amplitude (default: 1.0)
- `phase` (float): Initial phase in radians (default: 0)
- `sampleRate` (int): Audio sample rate in Hz (default: 44100)

#### 2. NoisePipe

Generates random noise signal.

```json
{
  "class": "com.terpomo.wavy.pipes.sources.NoisePipe",
  "type": "source",
  "parameters": {
    "noiseType": "WHITE",
    "amplitude": 1.0
  },
  "inputPorts": 0,
  "outputPorts": 1
}
```

**Properties**:
- `noiseType` (enum): "WHITE", "PINK", "BROWN"
- `amplitude` (float): Peak amplitude

#### 3. FileReaderPipe

Reads audio from file.

```json
{
  "class": "com.terpomo.wavy.pipes.sources.FileReaderPipe",
  "type": "source",
  "parameters": {
    "filePath": "C:/path/to/audio.wav",
    "loopAudio": true
  },
  "inputPorts": 0,
  "outputPorts": 1
}
```

**Properties**:
- `filePath` (string): Path to audio file
- `loopAudio` (boolean): Loop playback when reaching end

#### 4. MicPipe

Captures microphone input.

```json
{
  "class": "com.terpomo.wavy.pipes.sources.MicPipe",
  "type": "source",
  "parameters": {
    "sampleRate": 44100,
    "bufferSize": 2048
  },
  "inputPorts": 0,
  "outputPorts": 1
}
```

#### 5. RTLPipe

RTL-SDR software-defined radio receiver.

```json
{
  "class": "com.terpomo.wavy.pipes.sources.RTLPipe",
  "type": "source",
  "parameters": {
    "frequency": 100000000,
    "sampleRate": 2400000,
    "gain": 35.0,
    "deviceIndex": 0
  },
  "inputPorts": 0,
  "outputPorts": 1
}
```

---

### Filter Pipes

#### BandPassFilterPipe

Passes frequencies within a specified band.

```json
{
  "class": "com.terpomo.wavy.pipes.filters.BandPassFilterPipe",
  "type": "filter",
  "parameters": {
    "lowFrequency": 500.0,
    "highFrequency": 5000.0,
    "filterOrder": 4,
    "filterType": "BUTTERWORTH"
  },
  "inputPorts": 1,
  "outputPorts": 1
}
```

**Properties**:
- `lowFrequency` (double): Lower cutoff frequency in Hz
- `highFrequency` (double): Upper cutoff frequency in Hz
- `filterOrder` (int): Filter order (1-10)
- `filterType` (enum): "BUTTERWORTH", "CHEBYSHEV"

---

### Operator Pipes

#### CombinationPipe

Adds multiple input signals together.

```json
{
  "class": "com.terpomo.wavy.pipes.operators.CombinationPipe",
  "type": "operator",
  "parameters": {
    "inputCount": 3
  },
  "inputPorts": 3,
  "outputPorts": 1
}
```

**Properties**:
- `inputCount` (int): Number of inputs to combine

#### MultiplicationPipe

Multiplies two signals (ring modulation).

```json
{
  "class": "com.terpomo.wavy.pipes.operators.MultiplicationPipe",
  "type": "operator",
  "parameters": {},
  "inputPorts": 2,
  "outputPorts": 1
}
```

#### ResamplingPipe

Changes sample rate of signal.

```json
{
  "class": "com.terpomo.wavy.pipes.operators.ResamplingPipe",
  "type": "operator",
  "parameters": {
    "targetSampleRate": 48000,
    "filterOrder": 6
  },
  "inputPorts": 1,
  "outputPorts": 1
}
```

#### FMModulationPipe

Frequency modulation.

```json
{
  "class": "com.terpomo.wavy.pipes.operators.FMModulationPipe",
  "type": "operator",
  "parameters": {
    "carrierFrequency": 5000.0,
    "modulationIndex": 5.0
  },
  "inputPorts": 1,
  "outputPorts": 1
}
```

#### IQModulationPipe

In-phase/Quadrature modulation.

```json
{
  "class": "com.terpomo.wavy.pipes.operators.IQModulationPipe",
  "type": "operator",
  "parameters": {
    "carrierFrequency": 10000.0
  },
  "inputPorts": 2,
  "outputPorts": 1
}
```

#### IQDemodulationPipe

In-phase/Quadrature demodulation.

```json
{
  "class": "com.terpomo.wavy.pipes.operators.IQDemodulationPipe",
  "type": "operator",
  "parameters": {
    "carrierFrequency": 10000.0
  },
  "inputPorts": 1,
  "outputPorts": 2
}
```

---

### Monitor Pipes

#### OscilloscopePipe

Real-time waveform visualization (no output).

```json
{
  "class": "com.terpomo.wavy.pipes.monitors.OscilloscopePipe",
  "type": "monitor",
  "parameters": {
    "bufferSize": 4096,
    "triggerMode": "AUTO",
    "timebaseScale": 1.0
  },
  "inputPorts": 1,
  "outputPorts": 0
}
```

**Properties**:
- `bufferSize` (int): Number of samples to display
- `triggerMode` (enum): "AUTO", "MANUAL", "SINGLE"
- `timebaseScale` (float): Time scaling factor

#### FFTPipe

Frequency spectrum analysis.

```json
{
  "class": "com.terpomo.wavy.pipes.monitors.FFTPipe",
  "type": "monitor",
  "parameters": {
    "fftSize": 4096,
    "windowType": "HANN",
    "overlap": 0.5
  },
  "inputPorts": 1,
  "outputPorts": 0
}
```

**Properties**:
- `fftSize` (int): FFT size (must be power of 2)
- `windowType` (enum): "RECTANGULAR", "HANN", "HAMMING", "BLACKMAN"
- `overlap` (float): Overlap factor (0.0-1.0)

---

### Output Pipes

#### AudioPlayerPipe

Plays audio to system speaker.

```json
{
  "class": "com.terpomo.wavy.pipes.output.AudioPlayerPipe",
  "type": "output",
  "parameters": {
    "sampleRate": 44100,
    "channels": 1,
    "bufferSize": 2048,
    "gain": 1.0
  },
  "inputPorts": 1,
  "outputPorts": 0
}
```

**Properties**:
- `sampleRate` (int): Sample rate in Hz
- `channels` (int): Number of audio channels
- `bufferSize` (int): Audio buffer size
- `gain` (float): Output gain multiplier

#### FileWriterPipe

Writes audio to file (WAV format).

```json
{
  "class": "com.terpomo.wavy.pipes.output.FileWriterPipe",
  "type": "output",
  "parameters": {
    "filePath": "C:/output/recording.wav",
    "sampleRate": 44100,
    "channels": 1
  },
  "inputPorts": 1,
  "outputPorts": 0
}
```

---

## Complete Project Example

```json
{
  "name": "FM Synthesis Demo",
  "class": "com.terpomo.wavy.flow.Project",
  "created": "2026-03-28T10:30:00Z",
  "pipes": [
    {
      "name": "Carrier",
      "class": "com.terpomo.wavy.pipes.sources.ConstantWavePipe",
      "instanceId": "pipe-001",
      "location": {"x": 10, "y": 50},
      "parameters": {
        "frequency": 440.0,
        "amplitude": 1.0,
        "phase": 0.0,
        "sampleRate": 44100
      },
      "outputPorts": [{"index": 0, "name": "output", "linkedPortId": "pipe-003:0"}]
    },
    {
      "name": "Modulator",
      "class": "com.terpomo.wavy.pipes.sources.ConstantWavePipe",
      "instanceId": "pipe-002",
      "location": {"x": 10, "y": 150},
      "parameters": {
        "frequency": 5.0,
        "amplitude": 50.0,
        "phase": 0.0,
        "sampleRate": 44100
      },
      "outputPorts": [{"index": 0, "name": "output", "linkedPortId": "pipe-003:1"}]
    },
    {
      "name": "FM Modulator",
      "class": "com.terpomo.wavy.pipes.operators.FMModulationPipe",
      "instanceId": "pipe-003",
      "location": {"x": 200, "y": 100},
      "parameters": {
        "carrierFrequency": 440.0,
        "modulationIndex": 10.0
      },
      "inputPorts": [
        {"index": 0, "name": "carrier", "linkedPortId": "pipe-001:0"},
        {"index": 1, "name": "modulation", "linkedPortId": "pipe-002:0"}
      ],
      "outputPorts": [{"index": 0, "name": "output", "linkedPortId": "pipe-004:0"}]
    },
    {
      "name": "Speaker",
      "class": "com.terpomo.wavy.pipes.output.AudioPlayerPipe",
      "instanceId": "pipe-004",
      "location": {"x": 400, "y": 100},
      "parameters": {
        "sampleRate": 44100,
        "channels": 1,
        "gain": 0.5
      },
      "inputPorts": [{"index": 0, "name": "input", "linkedPortId": "pipe-003:0"}]
    }
  ]
}
```

---

## Signal Type Hierarchy

```
Signal (abstract base)
├── ConstantWave (sinusoid)
├── Noise (random)
└── ConstantValue (DC offset)
```

### ConstantWave

```json
{
  "type": "ConstantWave",
  "frequency": 1000.0,
  "amplitude": 1.0,
  "phase": 0.0
}
```

### Noise

```json
{
  "type": "Noise",
  "noiseType": "WHITE",
  "amplitude": 1.0
}
```

### ConstantValue

```json
{
  "type": "ConstantValue",
  "value": 0.5
}
```

---

## Saved Project Files

All project files are JSON and stored in `saved-projects/` directory.

| File | Description | Purpose |
|------|-------------|---------|
| `filter.json` | Band-pass filter example | FFT analysis demo |
| `fm.json` | FM modulation | Frequency modulation synthesis |
| `iq.json` | IQ modulation | Quadrature amplitude modulation |
| `iqdemod.json` | IQ demodulation | Quadrature signal recovery |
| `noise.json` | Noise generation | White noise output to speaker |
| `resamp.json` | Sample rate conversion | Resampling pipeline |
| `multipl.json` | Signal multiplication | Ring modulation effect |

---

**Last Updated**: 2026-03-28  
**For Versions**: Wavy 1.0+
