using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wavy.Flow
{
    public class Pipe
    {
        private String _Name;
        public String Name { get { return _Name; } set { _Name = value; }  }
        public Pipe() {
            this.Name = String.Format("Constant Wave {0}", Random.Shared.Next());
        }
    }
}
