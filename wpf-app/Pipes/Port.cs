using System.ComponentModel;
using System.Runtime.InteropServices;

namespace Wavy.Flow
{
    public abstract class Port : INotifyPropertyChanged
    {
        [DllImport("wavy.dll")]
        protected static extern IntPtr Port_getName(IntPtr p);
        public event PropertyChangedEventHandler? PropertyChanged;
        
        protected static Dictionary<IntPtr, Port> NativeInstancesMap = new Dictionary<IntPtr, Port>();
        protected readonly IntPtr NativePtr;
        protected bool IsInput;
        public bool IsSelected { get; private set; }
        public String Name { get; private set; }


        public Port(IntPtr p) {
            this.NativePtr = p;
            NativeInstancesMap.Add(p, this);

            string? name = Marshal.PtrToStringAnsi(Port_getName(this.NativePtr));
            this.Name = name == null ? String.Empty : name;
            this.IsSelected = false;
        }

        public bool IsInputPort()
        {
            return IsInput;
        }
        public bool isOutputPort()
        {
            return !IsInput;
        }
    }

    public class InputPort : Port
    {
        public InputPort(IntPtr p) : base(p) {
            IsInput = true;
        }
    }

    public class OutputPort : Port
    {
        public OutputPort(IntPtr p) : base(p) {
            IsInput = false;
        }
    }
}
