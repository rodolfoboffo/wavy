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
using Wavy.Core;
using Wavy;

namespace Wavy.UI
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            AppController.Instance.Workspace.ProjectAdded += Workspace_ProjectAdded;
        }

        private void Workspace_ProjectAdded(object? sender, ProjectsModifiedEventArgs e)
        {
            ProjectTabItem tab = new ProjectTabItem(e.Project);
            this.TabControlProjects.Items.Add(tab);
        }

        private void MenuItemExitApplication_Click(object sender, RoutedEventArgs e)
        {
            AppController.Instance.ExitApplication();
        }

        private void MenuItemNewProject_Click(object sender, RoutedEventArgs e)
        {
            AppController.Instance.Workspace.CreateNewProject();
        }
    }
}