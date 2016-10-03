package src.main.java.fr.insa.ot3.communication.message;

import src.main.java.fr.insa.ot3.model.LatLng;

public class AddLatLng extends Message{
	
	private final String userID;
	private final int gameID;
	private final boolean drawing;
	private LatLng latLng;
	
	public AddLatLng(String userID, int gameID, LatLng latLng, boolean drawing)
	{
		super(Type.ADDLATLNG);
		
		this.userID = userID;
		this.gameID = gameID;
		this.latLng = latLng;
		this.drawing = drawing;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public String getUserID() {
		return userID;
	}

	public int getGameID() {
		return gameID;
	}

	public boolean isDrawing() {
		return drawing;
	}
}
