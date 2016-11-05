package spring.communication.message;

/**
 * Created by arda on 05/10/16.
 */
public class NewUser extends Message {
    private final String name;
    private final String color;
    private final Long playerID;

    public NewUser(Long id, String name, String color) {
        super(Type.NEWUSER);

        this.playerID = id;
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getPlayerID() {
        return playerID;
    }
}
