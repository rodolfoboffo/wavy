using System.Configuration;
using Wavy.Flow;

namespace Wavy.Core
{
    public class AppController
    {
        private static AppController? _instance;
        protected Dictionary<IntPtr, Port> PortsMap;
        protected Dictionary<IntPtr, Pipe> PipesMap;
        public Workspace Workspace {  get; private set; }
        private Port? SelectedPort { get; set; }
        private AppController() {
            this.Workspace = new Workspace();
            this.PortsMap = new Dictionary<IntPtr, Port>();
            this.PipesMap = new Dictionary<IntPtr, Pipe>();
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

        public void AddPort(Port p)
        {
            this.PortsMap.Add(p.NativePtr, p);
            p.IsSelectedChanged += Port_SelectedChanged;
        }

        private void Port_SelectedChanged(Port sender, IsSelectedEventArgs e)
        {
            if (this.SelectedPort == null && e.IsSelected)
            {
                this.SelectedPort = sender;
            }
            else if (this.SelectedPort == sender && !e.IsSelected)
            {
                this.SelectedPort = null;
            }
            else if (this.SelectedPort != null && this.SelectedPort != sender && e.IsSelected)
            {
                this.SelectedPort.SetLinkedPort(sender);
                this.SelectedPort.IsSelected = false;
                sender.IsSelected = false;
            }
        }
    }
}
