using System.Windows;
using System.Windows.Controls;
using Wavy.Core;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            this.DataContext = AppController.Instance.Workspace;
            AppController.Instance.Workspace.ProjectAdded += Workspace_ProjectAdded;
            this.TabControlProjects.SelectionChanged += TabControlProjects_SelectionChanged;
        }



        private void TabControlProjects_SelectionChanged(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {
            ProjectTabItem selectedProjectTabItem = ((ProjectTabItem)((TabControl)e.Source).SelectedItem);
            if (selectedProjectTabItem != null)
            {
                Project p = ((ProjectTabItem)((TabControl)e.Source).SelectedItem).Project;
                AppController.Instance.Workspace.SelectedProject = p;
            }else
            {
                AppController.Instance.Workspace.SelectedProject = null;
            }
        }

        private void Workspace_ProjectAdded(object? sender, ProjectsEventArgs e)
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