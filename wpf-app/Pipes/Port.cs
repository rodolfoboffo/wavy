using System.ComponentModel;
using System.Runtime.InteropServices;
using Wavy.Core;

namespace Wavy.Flow
{
    public abstract class Port
    {
        [DllImport("wavy.dll")]
        protected static extern IntPtr Port_getName(IntPtr p);
        [DllImport("wavy.dll")]
        protected static extern IntPtr Port_getLinkedPort(IntPtr p);
        [DllImport("wavy.dll")]
        protected static extern IntPtr Port_setLinkedPort(IntPtr p1, IntPtr p2);

        public delegate void SelectedChangedEventHandler(Port sender, SelectedEventArgs e);
        public event SelectedChangedEventHandler? SelectedChanged;
        public readonly IntPtr NativePtr;
        protected bool IsInput;
        protected bool _Selected;
        public Port? LinkedPort { get; private set; }
        public bool Selected { 
            get { return this._Selected; }
            set {
                this._Selected = value;
                this.SelectedChanged?.Invoke(this, new SelectedEventArgs(value));
            }
        }
        public String Name { get; private set; }


        public Port(IntPtr p) {
            this.NativePtr = p;
            string? name = Marshal.PtrToStringAnsi(Port_getName(this.NativePtr));
            this.Name = name == null ? String.Empty : name;
            this._Selected = false;

            AppController.Instance.AddPort(this);
        }

        public bool IsInputPort()
        {
            return IsInput;
        }
        public bool isOutputPort()
        {
            return !IsInput;
        }

        public void SetLinkedPort(Port sender)
        {
            if (this.LinkedPort != sender)
            {
                Port_setLinkedPort(this.NativePtr, sender.NativePtr);
                IntPtr linkedPortPtr = Port_getLinkedPort(this.NativePtr);
                if (linkedPortPtr != sender.NativePtr)
                    throw new Exception("Could not set Linked Port.");
                this.LinkedPort = sender;
            }
        }
    }

    public class SelectedEventArgs : EventArgs
    {
        public bool IsSelected { get; private set; }
        public SelectedEventArgs(bool selected)
        {
            this.IsSelected = selected;
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
