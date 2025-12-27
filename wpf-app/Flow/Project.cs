using Wavy.Pipes;

namespace Wavy.Flow
{
    public class Project
    {
        public delegate void PipesModifiedEventHandler(object sender, PipesEventArgs e);
        public event PipesModifiedEventHandler? OnPipeAdded;
        public event PipesModifiedEventHandler? OnPipeRemoved;
        private HashSet<Pipe> _Pipes;
        public Project()
        {
            this.Name = String.Format("Project {0}", Random.Shared.Next());
            this._Pipes = new HashSet<Pipe>();
        }
        public string Name { get; set; }

        public void AddPipe(Pipe pipe)
        {
            if (pipe != null && !this._Pipes.Contains(pipe))
            {
                this._Pipes.Add(pipe);
                this.OnPipeAdded?.Invoke(this, new PipesEventArgs(pipe));
            }
        }

        public void RemovePipe(Pipe pipe) {
            if (this._Pipes.Contains(pipe))
            {
                this._Pipes.Remove(pipe);
                this.OnPipeRemoved?.Invoke(this, new PipesEventArgs(pipe));
            }
        }
    }

    public class PipesEventArgs : EventArgs
    {
        public Pipe Pipe { get; private set; }

        public PipesEventArgs(Pipe pipe) {
            this.Pipe = pipe;
        }
    }
}
