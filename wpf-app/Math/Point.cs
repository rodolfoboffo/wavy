using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wavy.Math
{
    public class Point
    {
        public float X {  get; private set; }
        public float Y { get; private set; }

        public Point(float x, float y)
        {
            X = x;
            Y = y;
        }
    }
}
