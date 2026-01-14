using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class PortPanel : UserControl
    {
        private Port _Port;
        public PortPanel(Port port)
        {
            this._Port = port;
            this.DataContext = this._Port;
            InitializeComponent();
        }
    }
}
