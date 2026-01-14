using System.Runtime.InteropServices;
using Wavy.Flow;

namespace Wavy.Pipes
{
    public class OscilloscopePipe : Pipe
    {
        [DllImport("wavy.dll")]
        private static extern IntPtr OscilloscopePipe_new();

        protected override IntPtr CreateNativePipeInstance()
        {
            return OscilloscopePipe_new();
        }

        public OscilloscopePipe() : base()
        {
        }
    }
}
