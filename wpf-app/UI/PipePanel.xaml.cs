using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class PipePanel : UserControl
    {
        public Pipe Pipe { get; set; }
        public bool IsMoving { get; set; }
        private Math.Point StartingPositionRelativeToCanvas;
        private Math.Point StartingPosition;

        public PipePanel(Pipe pipe)
        {
            InitializeComponent();
            this.DataContext = pipe;
            this.Pipe = pipe;
            this.IsMoving = false;
            this.StartingPosition = this.Pipe.Position;
            this.StartingPositionRelativeToCanvas = Math.Point.Zero();
            if (this.Pipe.Project != null)
                this.Pipe.Project.OnPipeRemoved += PipePanel_OnPipeRemoved;
            this.RecreatePortPanels();
            this.Pipe.OnPortAdded += Pipe_OnPortAdded;
        }

        private void Pipe_OnPortAdded(object sender, PortsEventArgs e)
        {
            Application.Current.Dispatcher.Invoke(new Action(() => {
                this.RecreatePortPanels();
            }));
        }

        private void RecreatePortPanels()
        {
            this.PortsStackPanel.Children.Clear();
            foreach (Port p in this.Pipe.InputPorts)
            {
                this.PortsStackPanel.Children.Add(new PortPanel(p));
            }
            foreach (Port p in this.Pipe.OutputPorts) {
                this.PortsStackPanel.Children.Add(new PortPanel(p));
            }
        }

        private void PipePanel_OnPipeRemoved(object sender, PipesEventArgs e)
        {
            Application.Current.Dispatcher.Invoke(new Action(() => {
                if (this.Pipe == e.Pipe)
                {
                    Canvas parentCanvas = (Canvas)this.Parent;
                    parentCanvas.Children.Remove(this);
                }
            }));
        }

        private void PipePanel_MouseMove(object sender, MouseEventArgs e)
        {
            PipePanel pipePanel = (PipePanel)sender;
            Canvas parentCanvas = (Canvas)pipePanel.Parent;
            if (this.IsMoving)
            {
                if (pipePanel != null && e.LeftButton == MouseButtonState.Pressed)
                {
                    Math.Point currentPosition = Math.Point.FromWindowsPoint(e.GetPosition(parentCanvas));
                    Math.Point displacement = currentPosition.Subtract(this.StartingPositionRelativeToCanvas);
                    this.Pipe.Position = this.StartingPosition.Sum(displacement);
                }
                else
                {
                    this.IsMoving = false;
                }
            }
        }

        private void PipePanel_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            PipePanel pipePanel = (PipePanel)sender;
            Canvas parentCanvas = (Canvas)pipePanel.Parent;
            this.IsMoving = true;
            this.StartingPositionRelativeToCanvas = Wavy.Math.Point.FromWindowsPoint(e.GetPosition(parentCanvas));
            this.StartingPosition = new Math.Point(Canvas.GetLeft(pipePanel), Canvas.GetTop(pipePanel));
        }

        private void RemovePipeMenuItem_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            Task.Run(() => { this.Pipe.Project?.RemovePipe(this.Pipe); ; });
        }
    }
}
