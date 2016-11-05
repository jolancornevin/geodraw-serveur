package spring.communication.message;

import spring.models.LatLng;

public class AddLatLng extends Message {

    private final Long userID;
    private final Long gameID;
    private final boolean drawing;
    private LatLng latLng;

    public AddLatLng(Long userID, Long gameID, LatLng latLng, boolean drawing) {
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

    public Long getUserID() {
        return userID;
    }

    public Long getGameID() {
        return gameID;
    }

    public boolean isDrawing() {
        return drawing;
    }
}
