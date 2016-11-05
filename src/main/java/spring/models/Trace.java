package spring.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * Created by Djowood on 05/11/2016.
 */
@Entity
@Table(name = "trace")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Trace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long id_player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_game")
    private Game game;

    private String jsonTrace;

    public Trace(Game game, Long id_player) {
        this.game = game;
        this.id_player = id_player;
    }

    /**
     * Required by JPA
     */
    public Trace() {

    }

    public Trace(Long id) {
        this.id = id;
    }

    public Long getIdPlayer() {
        return id_player;
    }

    public Game getGame() {
        return game;
    }

    public void setJsonTrace(String json) {
        this.jsonTrace = json;
    }

    public String getJsonTrace() {
        return jsonTrace;
    }
}
