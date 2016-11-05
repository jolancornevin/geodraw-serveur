package spring.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Djowood on 27/09/2016.
 */
@Entity
@Table(name = "player")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(name = "games_players",
            joinColumns = @JoinColumn(name = "id_player", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "id_game", referencedColumnName = "ID"))
    private Set<Game> games;

    public Player() {
        games = new HashSet<>();
    }

    public Player(Long id) {
        games = new HashSet<>();
        this.id = id;
    }

    public Player(String name) {
        games = new HashSet<>();
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

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public void removeGame(Game game) {
        this.games.remove(game);
    }
}
