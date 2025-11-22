using System.Runtime.InteropServices;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace wpf_app
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        [DllImport("wavy.dll")]
        static extern int wavy_getCounter();

        [DllImport("wavy.dll")]
        static extern void wavy_startThread();

        [DllImport("wavy.dll")]
        static extern void wavy_stopThread();

        private void ButtonStartThreadClick(object sender, RoutedEventArgs e)
        {
            wavy_startThread();
        }
        private void ButtonGetCounterClick(object sender, RoutedEventArgs e)
        {
            int CounterValue = wavy_getCounter();
            this.LabelCounter.Content = String.Format("Counter: {0}", CounterValue);
        }

        private void ButtonStopThreadClick(object sender, RoutedEventArgs e)
        {
            wavy_stopThread();
        }
    }
}