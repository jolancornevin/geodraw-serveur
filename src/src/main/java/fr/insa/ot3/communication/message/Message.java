package src.main.java.fr.insa.ot3.communication.message;

import java.util.HashMap;

import src.main.java.fr.insa.ot3.utils.Utils;


public class Message 
{
	private static HashMap<Integer, Class<? extends Message>> messagesTypes = new HashMap<Integer, Class<? extends Message>>();
	
	
	protected final int id;
	
	public Message(int id)
	{
		this.id = id;
	}
	
	public Message(Type t)
	{
		this.id = t.ordinal();
	}

	public int getId() {
		return id;
	}
	
	
	public static Message parseMessage(String message)
	{
		Message m = Utils.gson.fromJson(message, Message.class);
		return Utils.gson.fromJson(message, messagesTypes.get(m.getId()));
	}
	
	enum Type{
		TRACE
	}
	
	static
	{
		messagesTypes.put(Type.TRACE.ordinal(), TraceMessage.class);
	}
}
