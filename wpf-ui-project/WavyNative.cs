using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace WavyUI
{
    class WavyNative
    {
        [DllImport("wavy")]
        private static extern int wavy_numberTwo();

        public int getNumberTwo()
        {
            return wavy_numberTwo();
        }

    }
}
