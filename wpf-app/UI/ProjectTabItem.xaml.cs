using System.Windows.Controls;
using Wavy.Core;
using Wavy.Flow;

namespace Wavy.UI
{
    public partial class ProjectTabItem : TabItem
    {
        public ProjectTabItem(Project p) : base()
        {
            this.InitializeComponent();
            this.DataContext = p;
            this.Project = p;
            this.Content = new ProjectArea();
            AppController.Instance.Workspace.ProjectRemoved += Workspace_ProjectRemoved;
            AppController.Instance.Workspace.SelectedProjectChanged += Workspace_SelectedProjectChanged;
        }

        private void Workspace_SelectedProjectChanged(object sender, ProjectsEventArgs e)
        {
            if (e.Project == this.Project)
            {
                ((TabControl)this.Parent).SelectedItem = this;
            }
        }

        public Project Project { get; }

        private void Workspace_ProjectRemoved(object sender, ProjectsEventArgs e)
        {
            if (e.Project == this.Project)
            {
                ((TabControl)this.Parent).Items.Remove(this);
            }
        }

        private void ContextMenuCloseTab_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            AppController.Instance.Workspace.RemoveProject(this.Project);
        }
    }
}
