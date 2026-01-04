namespace Wavy.Math
{
    public class Point
    {
        public float X {  get; private set; }
        public float Y { get; private set; }

        public Point(float x, float y)
        {
            X = x;
            Y = y;
        }

        public Point(double x, double y)
        {
            X = (float)x;
            Y = (float)y;
        }

        public static Point Zero()
        {
            return new Point(0, 0);
        }

        public static Point FromWindowsPoint(System.Windows.Point p)
        {
            return new Point(p.X, p.Y);
        }

        public Point Sum(Point b)
        {
            return new Point(this.X + b.X, this.Y + b.Y);
        }

        public Point Subtract(Point b)
        {
            return new Point(this.X - b.X, this.Y - b.Y);
        }

        public override string ToString()
        {
            return String.Format("{{0}, {1}}", this.X, this.Y);
        }
    }
}
