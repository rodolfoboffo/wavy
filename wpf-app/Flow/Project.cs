namespace Wavy.Flow
{
    public class Project
    {
        public Project()
        {
            this.Name = String.Format("Project {0}", Random.Shared.Next());
        }
        public string Name { get; set; }
    }
}
