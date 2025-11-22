using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace Wavy.Core
{
    public class AppController
    {
        private AppController() { }

        private static AppController? Instance { get; set; }

        public static AppController GetInstance()
        {
            if (Instance == null)
                Instance = new AppController();
            return Instance;
        }

        public void ExitApplication()
        {
            System.Windows.Application.Current.Shutdown();
        }
    }
}
