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

        public delegate void IsSelectedChangedEventHandler(Port sender, IsSelectedEventArgs e);
        public event IsSelectedChangedEventHandler? IsSelectedChanged;
        public delegate void IsLinkedChangedEventHandler(Port sender, IsLinkedEventArgs e);
        public event IsLinkedChangedEventHandler? IsLinkedChanged;
        public readonly IntPtr NativePtr;
        protected bool IsInput;
        protected bool _IsSelected;
        public Port? LinkedPort { get; private set; }
        public bool IsSelected { 
            get { return this._IsSelected; }
            set {
                this._IsSelected = value;
                this.IsSelectedChanged?.Invoke(this, new IsSelectedEventArgs(value));
            }
        }
        protected bool _IsLinked;
        public bool IsLinked { 
            get { return this._IsLinked; }
            set {
                this._IsLinked = value;
                this.IsLinkedChanged?.Invoke(this, new IsLinkedEventArgs(value));
            }
        }
        public String Name { get; private set; }


        public Port(IntPtr p) {
            this.NativePtr = p;
            string? name = Marshal.PtrToStringAnsi(Port_getName(this.NativePtr));
            this.Name = name == null ? String.Empty : name;
            this._IsSelected = false;

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
                if (linkedPortPtr != sender.NativePtr && linkedPortPtr != IntPtr.Zero) {
                    this.LinkedPort = null;
                    throw new Exception("Could not set Linked Port.");
                }
                this.LinkedPort = sender;
            }
        }
    }

    public class IsSelectedEventArgs : EventArgs
    {
        public bool IsSelected { get; private set; }
        public IsSelectedEventArgs(bool selected)
        {
            this.IsSelected = selected;
        }
    }
    public class IsLinkedEventArgs : EventArgs
    {
        public bool IsLinked { get; private set; }
        public Port? LinkedPort { get; private set; }

        public IsLinkedEventArgs(bool linked)
        {
            this.IsLinked = linked;
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
