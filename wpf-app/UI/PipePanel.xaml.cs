using System.Windows.Controls;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class PipePanel : UserControl
    {
        private Pipe _Pipe;
        public Pipe Pipe { get { return _Pipe; } set { _Pipe = value; }  }
        public PipePanel(Pipe pipe)
        {
            InitializeComponent();
            this.DataContext = pipe;
            this._Pipe = pipe;
        }
    }
}
