using System.Text;
using System.Text.Json;
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
                if (node.Value != null)
                {
                    MenuItem newMenuItem = new MenuItem();
                    newMenuItem.Header = node.Key;
                    JsonNode nodeValue = node.Value;
                    if (nodeValue.GetValueKind().Equals(JsonValueKind.Object))
                    {
                        List<KeyValuePair<string, JsonNode?>> childrenNodes = nodeValue.AsObject().ToList();
                        this.BuildPipesMenuRecursive(childrenNodes, newMenuItem);
                    }
                    else if (nodeValue.GetValueKind().Equals(JsonValueKind.String))
                    {
                        string pipeEnumNodeValue = nodeValue.GetValue<String>();
                        PipeEnum pipeEnum = (PipeEnum)Enum.Parse(typeof(PipeEnum), pipeEnumNodeValue);
                        newMenuItem.Tag = pipeEnum;
                        newMenuItem.Click += MenuItemPipeInstance_Click;
                    }
                    parent.Items.Add(newMenuItem);
                }
            }
        }

        private void BuildPipesMenu()
        {
            byte[] pipesMenuFileContent = Wavy.UIResource.PipesMenu;
            string jsonContent = Encoding.ASCII.GetString(pipesMenuFileContent);
            JsonNode? jsonParsedContent = JsonNode.Parse(jsonContent);
            if (jsonParsedContent != null)
            {
                List<KeyValuePair<string, JsonNode?>> nodes = jsonParsedContent.AsObject().ToList();
                this.BuildPipesMenuRecursive(nodes, this.MenuItemPipes);
            }
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
            if (e.Project != null)
            {
                ProjectTabItem tab = new ProjectTabItem(e.Project);
                this.TabControlProjects.Items.Add(tab);
            }
        }

        private void MenuItemExitApplication_Click(object sender, RoutedEventArgs e)
        {
            AppController.Instance.ExitApplication();
        }

        private void MenuItemNewProject_Click(object sender, RoutedEventArgs e)
        {
            AppController.Instance.Workspace.CreateNewProject();
        }

        private void MenuItemPipeInstance_Click(object sender, RoutedEventArgs e)
        {
            Project? selectedProject = AppController.Instance.Workspace.SelectedProject;
            if (selectedProject != null) {
                PipeEnum pipeEnum = (PipeEnum)((MenuItem)sender).Tag;
                selectedProject.CreatePipe(pipeEnum);
            }
        }
    }
}