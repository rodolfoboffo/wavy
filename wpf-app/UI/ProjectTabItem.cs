using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using Wavy.Bridge.Flow;
using Wavy.Core;

namespace Wavy.UI
{
    internal class ProjectTabItem : TabItem
    {
        public Project Project { get; private set; }
        public ProjectTabItem(Project p) : base() {
            this.Project = p;
            this.Header = this.Project.Name;
            this.Content = new ProjectArea();
            this.MouseRightButtonUp += ProjectTabItem_MouseRightButtonUp;
            AppController.Instance.Workspace.ProjectRemoved += Workspace_ProjectRemoved;
        }

        private void Workspace_ProjectRemoved(object sender, ProjectsModifiedEventArgs e)
        {
            if (e.Project == this.Project)
            {
                ((TabControl)this.Parent).Items.Remove(this);
            }
        }

        private void ProjectTabItem_MouseRightButtonUp(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            AppController.Instance.Workspace.RemoveProject(this.Project);
        }
    }
}
