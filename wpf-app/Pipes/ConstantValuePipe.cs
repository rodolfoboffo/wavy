using System.Runtime.InteropServices;
using Wavy.Flow;

namespace Wavy.Pipes
{
    public class ConstantValuePipe : Pipe
    {
        [DllImport("wavy.dll")]
        private static extern IntPtr ConstantValuePipe_new();

        protected override IntPtr CreateNativePipeInstance()
        {
            return ConstantValuePipe_new();
        }

        public ConstantValuePipe() : base()
        {
        }
    }
}
