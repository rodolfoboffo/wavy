using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Wavy.Math;

namespace Wavy.Flow
{
    public abstract class Pipe
    {
        private String _Name;
        public String Name { get { return _Name; } set { _Name = value; }  }
        public Point Position { get; private set; }
        public Pipe() {
            this.Position = new Point(0, 0);
            this._Name = String.Format("Constant Wave {0}", Random.Shared.Next());
        }
    }
}
