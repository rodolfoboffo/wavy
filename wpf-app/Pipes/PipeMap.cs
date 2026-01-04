namespace Wavy.Pipes
{
    public class PipeMap
    {
        private static PipeMap? _Instance;
        public static PipeMap Instance { 
            get {
                if (_Instance == null)
                    _Instance = new PipeMap();
                return _Instance;
            } 
        }

        private Dictionary<PipeEnum, Type> Map;

        private PipeMap()
        {
            this.Map = new Dictionary<PipeEnum, Type>
            {
                { PipeEnum.CONSTANT_VALUE, typeof(ConstantValuePipe) }
            };
        }

        public Type GetPipeByEnum(PipeEnum pipeEnum)
        {
            return this.Map.GetValueOrDefault(pipeEnum, null);
        }
    }
}
