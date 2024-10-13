package wavy.flow;

import wavy.signals.Signal;

public class Source extends Pipe {
	
	protected Signal signal;
	protected Pipe output;
	
	public Source(Signal signal) {
		super();
		this.signal = signal;
	}
	
	public void setOuput(DataStream output) {
		this.output = output;
		output.setInput(this);
	}

}
