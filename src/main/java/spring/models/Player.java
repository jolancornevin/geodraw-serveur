package spring.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Djowood on 27/09/2016.
 */
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(name = "games_players",
            joinColumns = @JoinColumn(name = "id_player", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "id_game", referencedColumnName = "ID"))
    private List<Game> games;

    public Player() {
        games = new ArrayList<>();
    }

    public Player(Long id) {
        games = new ArrayList<>();
        this.id = id;
    }

    public Player(String name) {
        games = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
