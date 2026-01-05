using Wavy.Flow;

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
                { PipeEnum.CONSTANT_VALUE, typeof(ConstantValuePipe) },
                { PipeEnum.CONSTANT_WAVE, typeof(ConstantWavePipe) }
            };
        }

        public Type GetPipeTypeByEnum(PipeEnum pipeEnum)
        {
            if (this.Map.ContainsKey(pipeEnum))
                return this.Map[pipeEnum];
            throw new Exception(String.Format("Pipe type not found for Enum {0}", pipeEnum.ToString()));
        }

        public Pipe GetPipeByEnum(PipeEnum pipeEnum)
        {
            Type pipeType = this.GetPipeTypeByEnum(pipeEnum);
            System.Reflection.ConstructorInfo? constructor = pipeType.GetConstructor(new Type[] { });
            if (constructor != null)
            {
                Pipe pipe = (Pipe)constructor.Invoke(new object[] { });
                return pipe;
            }
            throw new Exception(String.Format("Could not create pipe instance from enum {0}", pipeEnum.ToString()));
        }
    }
}
