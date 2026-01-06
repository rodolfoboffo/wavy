using System.ComponentModel;
using System.Runtime.InteropServices;
using Wavy.Math;

namespace Wavy.Flow
{
    public abstract class Pipe : INotifyPropertyChanged, IDisposable
    {
        [DllImport("wavy.dll")]
        private static extern IntPtr Pipe_new();
        [DllImport("wavy.dll")]
        private static extern void Pipe_free(IntPtr p);
        [DllImport("wavy.dll")]
        private static extern uint Pipe_getInputPortsCount(IntPtr p);
        [DllImport("wavy.dll")]
        private static extern uint Pipe_getOuputPortsCount(IntPtr p);

        private static Dictionary<IntPtr, Pipe> PipeInstancesMap = new Dictionary<IntPtr, Pipe>();
        private static Pipe? GetInstanceByNativeRef(IntPtr p) { return PipeInstancesMap[p]; }

        public Project Project { get;}
        private readonly IntPtr _NativePtr;
        private String _Name;

        public event PropertyChangedEventHandler? PropertyChanged;

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
        public Pipe(Project project) {
            this.Project = project;
            this._NativePtr = Pipe_new();
            PipeInstancesMap.Add(this._NativePtr, this);
            this._Position = new Point(0, 0);
            this._Name = String.Format("Pipe {0}", Random.Shared.Next());
        }
        ~Pipe()
        {
            this.Dispose();
        }
        public void Dispose()
        {
            PipeInstancesMap.Remove(this._NativePtr);
            Pipe_free(this._NativePtr);
        }
    }
}
