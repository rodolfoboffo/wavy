using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Wavy.Bridge.Flow;

namespace Wavy.Core
{
    public class Workspace
    {
        public delegate void ProjectAddedEventHandler(object sender, ProjectsModifiedEventArgs e);
        public event ProjectAddedEventHandler? ProjectAdded;
        public event ProjectAddedEventHandler? ProjectRemoved;

        private HashSet<Project> Projects;

        public Workspace() {
            this.Projects = new HashSet<Project>();
        }

        public Project CreateNewProject()
        {
            Project p = new Project();
            this.Projects.Add(p);
            this.ProjectAdded?.Invoke(this, new ProjectsModifiedEventArgs(p));
            return p;
        }

        public void RemoveProject(Project p)
        {
            this.Projects.Remove(p);
            this.ProjectRemoved?.Invoke(this, new ProjectsModifiedEventArgs(p));
        }
    }

    public class ProjectsModifiedEventArgs : EventArgs
    {
        public Project Project { get; set; }
        public ProjectsModifiedEventArgs(Project project)
        {
            this.Project = project;
        }
    }
}
