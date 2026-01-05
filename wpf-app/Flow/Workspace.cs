using System.ComponentModel;

namespace Wavy.Flow
{
    public class Workspace : INotifyPropertyChanged
    {
        public delegate void ProjectsModifiedEventHandler(object sender, ProjectsEventArgs e);
        public event ProjectsModifiedEventHandler? OnProjectAdded;
        public event ProjectsModifiedEventHandler? OnProjectRemoved;

        public delegate void SelectedProjectChangedEventHandler(object sender, ProjectsEventArgs e);
        public event SelectedProjectChangedEventHandler? SelectedProjectChanged;
        public event PropertyChangedEventHandler? PropertyChanged;

        public bool IsProjectSelected { get { return (this.SelectedProject != null); } }

        private Project? _SelectedProject;
        public Project? SelectedProject {  
            get { return this._SelectedProject; } 
            set {
                if (this._SelectedProject != value)
                {
                    this._SelectedProject = value;
                    this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("IsProjectSelected"));
                    this.SelectedProjectChanged?.Invoke(this, new ProjectsEventArgs(value));
                }
            }
        }
        private HashSet<Project> Projects;

        public Workspace() {
            this.Projects = new HashSet<Project>();
        }

        public Project CreateNewProject()
        {
            Project p = new Project();
            this.Projects.Add(p);
            this.OnProjectAdded?.Invoke(this, new ProjectsEventArgs(p));
            this.SelectedProject = p;
            return p;
        }

        public void RemoveProject(Project p)
        {
            if (this.SelectedProject == p)
            {
                this.SelectedProject = null;
            }
            this.Projects.Remove(p);
            this.OnProjectRemoved?.Invoke(this, new ProjectsEventArgs(p));
        }
    }

    public class ProjectsEventArgs : EventArgs
    {
        public Project? Project { get; private set; }
        public ProjectsEventArgs(Project? project)
        {
            this.Project = project;
        }
    }
}
