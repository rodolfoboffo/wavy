using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Wavy.Bridge.Flow;

namespace Wavy.Core
{
    public class AppController
    {
        private static AppController? _instance;
        public Workspace Workspace {  get; private set; }
        private AppController() {
            this.Workspace = new Workspace();
        }

        public static AppController Instance { 
            get {
                if (_instance == null)
                    _instance = new AppController();
                return _instance;
            }
            private set { _instance = value; } 
        }

        public void ExitApplication()
        {
            System.Windows.Application.Current.Shutdown();
        }
    }
}
