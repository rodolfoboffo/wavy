package wavy.flow;

public class DataStream extends Pipe {
	
	protected Pipe input;
	protected Pipe output;
	
	public void Pipe(Pipe input, Pipe output) {
		this.input = input;
		this.output = output;
	}
	
	public void setInput(Pipe input) {
		this.input = input;
	}
	
	public void setOuput(Pipe output) {
		this.output = output;
	}
	
}
