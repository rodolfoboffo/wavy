using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using Wavy.Core;
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

        public bool IsSelected { get { return this.Port.Selected; }  }

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
            this.Port.SelectedChanged += Port_PortSelectedChanged;
            this.DataContext = this;
            InitializeComponent();
        }

        private void Port_PortSelectedChanged(Port sender, SelectedEventArgs e)
        {
            this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("IsSelected"));
        }

        private void PortValueTextBox_MouseLeftButtonDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            e.Handled = true;
        }

        private void PortValueTextBox_MouseMove(object sender, System.Windows.Input.MouseEventArgs e)
        {
            e.Handled = true;
        }

        private void OutputPortHandleRect_MouseLeftButtonUp(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            this.TogglePortSelection();
            e.Handled = true;
        }

        private void TogglePortSelection()
        {
            this.Port.Selected = !this.Port.Selected;
        }

        private void InputPortHandleRect_MouseLeftButtonUp(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            this.TogglePortSelection();
            e.Handled = true;
        }

        private void OutputPortHandleRect_MouseMove(object sender, System.Windows.Input.MouseEventArgs e)
        {
            e.Handled = true;
        }

        private void InputPortHandleRect_MouseMove(object sender, System.Windows.Input.MouseEventArgs e)
        {
            e.Handled = true;
        }

        private void InputPortHandleRect_MouseLeftButtonDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            e.Handled = true;
        }

        private void OutputPortHandleRect_MouseLeftButtonDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            e.Handled = true;
        }
    }
}
