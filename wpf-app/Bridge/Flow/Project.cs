using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wavy.Bridge.Flow
{
    public class Project
    {
        public Project()
        {
            this.Name = String.Format("Project {0}", 2);
        }
        public string Name { get; set; }
    }
}
