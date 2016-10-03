package fr.insa.ot3.communication.message;

import java.util.HashMap;

import fr.insa.ot3.utils.Utils;


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
		if(!messagesTypes.containsKey(m.getId()))
			return m;
		return Utils.gson.fromJson(message, messagesTypes.get(m.getId()));
	}
	
	enum Type{
		TRACE,
		ADDLATLNG,
		NEWGAME,
		
		GAMELIST,
		GAMELISTREQUEST,
		
		JOINGAME,
		JOINEDGAME,
		
		GAMEUPDATE,
		
		VOTE,
		GAMERESULT
		//TODO Vote
		//TODO trace update incremental (not full)
	}
	
	static
	{
		messagesTypes.put(Type.TRACE.ordinal(), TraceMessage.class);
		messagesTypes.put(Type.ADDLATLNG.ordinal(), AddLatLng.class);
		
		messagesTypes.put(Type.NEWGAME.ordinal(), NewGame.class);

		messagesTypes.put(Type.GAMELIST.ordinal(), GameList.class);
		messagesTypes.put(Type.GAMELISTREQUEST.ordinal(), GameListRequest.class);

		messagesTypes.put(Type.JOINGAME.ordinal(), JoinGame.class);
		messagesTypes.put(Type.JOINEDGAME.ordinal(), JoinedGame.class);

		messagesTypes.put(Type.GAMEUPDATE.ordinal(), GameUpdate.class);

		messagesTypes.put(Type.VOTE.ordinal(), Vote.class);
	}
}
