using System.ComponentModel;
using Wavy.Math;

namespace Wavy.Flow
{
    public abstract class Pipe : INotifyPropertyChanged
    {
        private String _Name;

        public event PropertyChangedEventHandler? PropertyChanged;

        public String Name { get { return _Name; } set { _Name = value; }  }

        public Point _Position;
        public Point Position { 
            get { return this._Position; }
            set { 
                this._Position = value;
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("Position"));
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosX"));
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosY"));
            }
        }

        public float PosX { 
            get { return this._Position.X; }
            set { 
                this._Position = new Point(value, this.PosY);
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosX"));
            }
        }
        public float PosY { 
            get { return this._Position.Y; }
            set
            {
                this._Position = new Point(this.PosX, value);
                this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("PosY"));
            }
        }
        public Pipe() {
            this._Position = new Point(0, 0);
            this._Name = String.Format("Pipe {0}", Random.Shared.Next());
        }
    }
}
