using Wavy.Flow;

namespace Wavy.Pipes
{
    public class PipeClassMap
    {
        private static PipeClassMap? _Instance;
        public static PipeClassMap Instance { 
            get {
                if (_Instance == null)
                    _Instance = new PipeClassMap();
                return _Instance;
            } 
        }

        private Dictionary<PipeEnum, Type> Map;

        private PipeClassMap()
        {
            this.Map = new Dictionary<PipeEnum, Type>
            {
                { PipeEnum.CONSTANT_VALUE, typeof(ConstantValuePipe) }
            };
        }

        public Type GetPipeTypeByEnum(PipeEnum pipeEnum)
        {
            if (this.Map.ContainsKey(pipeEnum))
                return this.Map[pipeEnum];
            throw new Exception(String.Format("Pipe type not found for Enum {0}", pipeEnum.ToString()));
        }

        public Pipe GetPipeInstanceByEnum(PipeEnum pipeEnum, Project project)
        {
            Type pipeType = this.GetPipeTypeByEnum(pipeEnum);
            System.Reflection.ConstructorInfo? constructor = pipeType.GetConstructor(new Type[] {typeof(Project)});
            if (constructor != null)
            {
                Pipe pipe = (Pipe)constructor.Invoke(new object[] {project});
                return pipe;
            }
            throw new Exception(String.Format("Could not create pipe instance from enum {0}", pipeEnum.ToString()));
        }
    }
}
