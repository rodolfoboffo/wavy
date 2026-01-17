using System.Windows;
using System.Windows.Controls;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class PortPanel : UserControl
    {
        private Port Port;

        public String PortName
        {
            get { return this.Port.Name; }
        }

        public Visibility ValueTextBoxVisibility
        {
            get { return Visibility.Visible; }
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
