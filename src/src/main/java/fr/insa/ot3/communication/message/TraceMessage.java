package src.main.java.fr.insa.ot3.communication.message;

import src.main.java.fr.insa.ot3.model.Drawing;

public class TraceMessage extends Message
{
	
	private final Drawing trace;


	public TraceMessage() {
		super(Type.TRACE);
		
		trace = new Drawing();
	}
	
	public TraceMessage(Drawing trace)
	{
		super(Type.TRACE);
		this.trace = trace;
	}


	public Drawing getTrace() {
		return trace;
	}
}
