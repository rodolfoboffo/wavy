using ScottPlot.Plottables;
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
using Wavy.Bridge;
using Wavy.Core;
using Wavy.WPF;

namespace Wavy.WPF.UI
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void MenuItemExitApplication_Click(object sender, RoutedEventArgs e)
        {
            AppController.GetInstance().ExitApplication();
        }

        private void ButtonPlayMain_Click(object sender, RoutedEventArgs e)
        {
            int n = 2048;
            SineTable s = new SineTable(n);
            int[] dataX = new int[n];
            float[] dataY = new float[n];
            for (int i = 0; i < n; i++)
            {
                dataX[i] = i;
                dataY[i] = s.getValue(i);
            }
            WpfPlot.Plot.Add.Scatter(dataX, dataY);
            WpfPlot.Refresh();
            Console.WriteLine("Grafico atualizado");
        }
    }
}