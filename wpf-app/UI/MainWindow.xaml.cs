using System.Net.Http.Json;
using System.Text.Json.Nodes;
using System.Windows;
using System.Windows.Controls;
using Wavy.Core;
using Wavy.Flow;
using Wavy.Pipes;

namespace Wavy.UI
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            this.DataContext = AppController.Instance.Workspace;
            this.BuildPipesMenu();
            AppController.Instance.Workspace.OnProjectAdded += Workspace_ProjectAdded;
            this.TabControlProjects.SelectionChanged += TabControlProjects_SelectionChanged;
        }

        private void BuildPipesMenuRecursive(List<KeyValuePair<string, JsonNode?>> nodes, MenuItem parent)
        {
            foreach (KeyValuePair<string, JsonNode?> node in nodes)
            {
                MenuItem newMenuItem = new MenuItem();
                newMenuItem.Header = node.Key;
                parent.Items.Add(newMenuItem);
                List<KeyValuePair<string, JsonNode?>> childrenNodes = node.Value.AsObject().ToList();
                this.BuildPipesMenuRecursive(childrenNodes, newMenuItem);
            }
        }
        private void BuildPipesMenu()
        {
            string jsonContent = Wavy.UIResource.PipesMenu;
            List<KeyValuePair<string, JsonNode?>> nodes = JsonNode.Parse(jsonContent).AsObject().ToList();
            this.BuildPipesMenuRecursive(nodes, this.MenuItemPipes);
        }

        private void TabControlProjects_SelectionChanged(object sender, SelectionChangedEventArgs e)
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

        private void MenuItemConstantWave_Click(object sender, RoutedEventArgs e)
        {
            AppController.Instance.Workspace.SelectedProject?.AddPipe(new ConstantValuePipe());
        }
    }
}