using System.Runtime.InteropServices;

namespace Wavy.Bridge
{
    public class SineTable
    {
        [DllImport("wavy.dll")]
        private static extern IntPtr SineTable_new(int n);

        [DllImport("wavy.dll")]
        private static extern float SineTable_getValue(IntPtr s, int n);

        [DllImport("wavy.dll")]
        private static extern int SineTable_getLength(IntPtr s);

        private IntPtr nativePtr;
        private int n;
        public SineTable(int n)
        {
            this.n = n;
            this.nativePtr = SineTable_new(n);
        }

        public float getValue(int index)
        {
            return SineTable_getValue(nativePtr, index);
        }

        public int getLength()
        {
            return SineTable_getLength(nativePtr);
        }
    }
}
