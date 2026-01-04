using System.Windows.Controls;
using System.Windows.Input;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class PipePanel : UserControl
    {
        private Pipe _Pipe;
        private Math.Point _StartingPositionRelativeToCanvas;
        private Math.Point _StartingPosition;

        public Pipe Pipe { get { return _Pipe; } set { _Pipe = value; }  }
        public PipePanel(Pipe pipe)
        {
            InitializeComponent();
            this.DataContext = pipe;
            this._Pipe = pipe;
            this._StartingPosition = this.Pipe.Position;
            this._StartingPositionRelativeToCanvas = Math.Point.Zero();
        }

        private void PipePanel_MouseMove(object sender, MouseEventArgs e)
        {
            PipePanel pipePanel = (PipePanel)sender;
            Canvas parentCanvas = (Canvas)pipePanel.Parent;
            if (pipePanel != null && e.LeftButton == MouseButtonState.Pressed)
            {
                Math.Point currentPosition = Math.Point.FromWindowsPoint(e.GetPosition(parentCanvas));
                Math.Point displacement = currentPosition.Subtract(this._StartingPositionRelativeToCanvas);
                this.Pipe.Position = this._StartingPosition.Sum(displacement);
            }
        }

        private void PipePanel_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            PipePanel pipePanel = (PipePanel)sender;
            Canvas parentCanvas = (Canvas)pipePanel.Parent;
            this._StartingPositionRelativeToCanvas = Wavy.Math.Point.FromWindowsPoint(e.GetPosition(parentCanvas));
            this._StartingPosition = new Math.Point(Canvas.GetLeft(pipePanel), Canvas.GetTop(pipePanel));
        }
    }
}
