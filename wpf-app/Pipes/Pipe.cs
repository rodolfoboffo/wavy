using System.ComponentModel;
using System.Runtime.InteropServices;
using Wavy.Math;

namespace Wavy.Flow
{
    public abstract class Pipe : INotifyPropertyChanged, IDisposable
    {
        [DllImport("wavy.dll")]
        protected static extern void Pipe_free(IntPtr p);
        [DllImport("wavy.dll")]
        protected static extern void Pipe_init(IntPtr p);
        [DllImport("wavy.dll")]
        protected static extern void Pipe_shutdown(IntPtr p);
        [DllImport("wavy.dll")]
        protected static extern ushort Pipe_getInputPortsCount(IntPtr p);
        [DllImport("wavy.dll")]
        protected static extern ushort Pipe_getOutputPortsCount(IntPtr p);
        [DllImport("wavy.dll")]
        protected static extern IntPtr Pipe_getInputPort(IntPtr p, ushort index);
        [DllImport("wavy.dll")]
        protected static extern IntPtr Pipe_getOutputPort(IntPtr p, ushort index);

        private static Dictionary<IntPtr, Pipe> NativeInstancesMap = new Dictionary<IntPtr, Pipe>();
        private static Pipe? GetInstanceByNativeRef(IntPtr p) { return NativeInstancesMap[p]; }

        public delegate void PortsModifiedEventHandler(object sender, PortsEventArgs e);
        public event PortsModifiedEventHandler? OnPortAdded;
        public event PortsModifiedEventHandler? OnPortRemoved;
        public event PropertyChangedEventHandler? PropertyChanged;

        public Project? Project { get; set; }
        private readonly IntPtr NativePtr;
        private String _Name;
        public List<Port> InputPorts, OutputPorts;

        public String Name { get { return _Name; } set { _Name = value; }  }

        public Point _Position;
        public Point Position { 
            get { return this._Position; }
            set { 
                this._Position = value;
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("Position"));
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosX"));
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosY"));
            }
        }

        public float PosX { 
            get { return this._Position.X; }
            set { 
                this._Position = new Point(value, this.PosY);
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosX"));
            }
        }
        public float PosY { 
            get { return this._Position.Y; }
            set
            {
                this._Position = new Point(this.PosX, value);
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosY"));
            }
        }
        public Pipe() {
            this.InputPorts = new List<Port>();
            this.OutputPorts = new List<Port>();
            this._Position = new Point(0, 0);
            this._Name = String.Format("Pipe {0}", Random.Shared.Next());

            this.NativePtr = CreateNativePipeInstance();
            NativeInstancesMap.Add(this.NativePtr, this);

            this.Init();
            this.SyncPorts();
        }

        private void SyncPorts()
        {
            this.InputPorts.Clear();
            ushort pCount = Pipe_getInputPortsCount(this.NativePtr);
            for (ushort i = 0; i < pCount; i++)
            {
                Port p = new InputPort(Pipe_getInputPort(this.NativePtr, i));
                this.InputPorts.Add(p);
                this.OnPortAdded?.Invoke(this, new PortsEventArgs(p));
            }
            this.OutputPorts.Clear();
            pCount = Pipe_getOutputPortsCount(this.NativePtr);
            for (ushort i = 0; i < pCount; i++)
            {
                Port p = new OutputPort(Pipe_getOutputPort(this.NativePtr, i));
                this.OutputPorts.Add(p);
                this.OnPortAdded?.Invoke(this, new PortsEventArgs(p));
            }
        }

        protected abstract nint CreateNativePipeInstance();

        private void Init()
        {
            Pipe_init(this.NativePtr);
        }

        ~Pipe()
        {
            this.Dispose();
        }
        public void Dispose()
        {
            Pipe_shutdown(this.NativePtr);
            NativeInstancesMap.Remove(this.NativePtr);
            Pipe_free(this.NativePtr);
        }
    }
    public class PortsEventArgs : EventArgs
    {
        public Port Port { get; private set; }

        public PortsEventArgs(Port port)
        {
            this.Port = port;
        }
    }
}
