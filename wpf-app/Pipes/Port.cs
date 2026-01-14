using System.Runtime.InteropServices;

namespace Wavy.Flow
{
    public class Port
    {
        [DllImport("wavy.dll")]
        protected static extern IntPtr Port_getName(IntPtr p);

        private static Dictionary<IntPtr, Port> NativeInstancesMap = new Dictionary<IntPtr, Port>();
        private readonly IntPtr _NativePtr;
        public String Name { get; private set; }
        
        public Port(IntPtr p) {
            this._NativePtr = p;
            NativeInstancesMap.Add(p, this);

            string? name = Marshal.PtrToStringAnsi(Port_getName(this._NativePtr));
            this.Name = name == null ? String.Empty : name;
        }
    }
}
