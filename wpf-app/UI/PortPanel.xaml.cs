using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class PortPanel : UserControl, INotifyPropertyChanged
    {
        private Port Port;

        public event PropertyChangedEventHandler? PropertyChanged;

        public String PortName
        {
            get { return this.Port.Name; }
        }

        public bool IsSelected { get { return this.Port.IsSelected; }  }

        public Visibility ValueTextBoxVisibility
        {
            get { return Visibility.Collapsed; }
        }

        public Visibility InputPortHandleVisibility

        {
            get { return this.Port.IsInputPort() ? Visibility.Visible : Visibility.Collapsed; }
        }

        public Visibility OutputPortHandleVisibility

        {
            get { return this.Port.IsInputPort() ? Visibility.Collapsed : Visibility.Visible; }
        }

        public PortPanel(Port port)
        {
            this.Port = port;
            this.DataContext = this;
            InitializeComponent();
        }

        private void PortValueTextBox_MouseLeftButtonDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            e.Handled = true;
        }

        private void PortValueTextBox_MouseMove(object sender, System.Windows.Input.MouseEventArgs e)
        {
            e.Handled = true;
        }
    }
}
