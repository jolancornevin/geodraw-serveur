package spring.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import spring.utils.Utils;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Djowood on 27/09/2016.
 */
@Entity
@Table(name = "game")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean locked;
    private int maxNbPlayer;
    private Date startDate;
    private Date endDate;
    private boolean over;
    private String theme;

    @ManyToMany
    @JoinTable(name = "games_players",
            joinColumns = @JoinColumn(name = "id_game", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "id_player", referencedColumnName = "ID"))
    private Set<Player> players;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "game")
    private List<Trace> bdTraces;

    @Transient
    private HashMap<Long, Integer> votes;

    @Transient
    private Map<Long, Drawing> traces;

    /* =================================================================================================================
                                                        Constructeur
    ================================================================================================================= */
    public Game() {
        players = new HashSet<>();
        traces = new HashMap<>();
        votes = new HashMap<>();
    }

    public Game(Long id) {
        players = new HashSet<>();
        traces = new HashMap<>();
        votes = new HashMap<>();
        this.id = id;
    }

    public Game(String name, Boolean lock, int maxNbPlayer, int hours, int minutes, String theme, Boolean over) {
        this.name = name;
        this.locked = lock;
        this.over = over;
        this.maxNbPlayer = maxNbPlayer;
        this.theme = theme;

        Calendar c = Calendar.getInstance();
        this.startDate = c.getTime();

        endDate = null;

        c.add(Calendar.HOUR, hours);
        c.add(Calendar.MINUTE, minutes);

        players = new HashSet<>();
        traces = new HashMap<>();
        votes = new HashMap<>();
    }

    public List<Trace> getBdTraces() {
        return bdTraces;
    }

    public void addBdTrace(Trace trace) {
        this.bdTraces.add(trace);
        setTracesFromBd();
    }

    public Long getId() {
        return id;
    }

    public Boolean getLock() {
        return locked;
    }

    public String getName() {
        return name;
    }

    public int getCurrentNbPlayer() {
        return this.players.size();
    }

    public void voteFor(long playerID) {
        if (hasPlayer(playerID)) {
            votes.put(playerID, votes.get(playerID) + 1);
        }

    }

    /**
     * Adds a player to the current game
     *
     * @param player : The player
     * @return true if the player has been successfully added, false otherwise
     */
    public boolean addPlayer(Player player) {
        if (players.size() < maxNbPlayer) {
            if (players.contains(player))
                return true;

            players.add(player);

            votes.put(player.getId(), 0);

            traces.put(player.getId(), new Drawing());

            return true;
        }
        return false;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    public boolean hasPlayer(Long idPlayer) {
        for (Player player : players) {
            if (player.getId().equals(idPlayer))
                return true;
        }
        return false;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public int getMaxNbPlayer() {
        return maxNbPlayer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTheme() {
        return theme;
    }

    /**
     * Renvoit le drawing d'un utilisateur. Si il n'existe pas, on le créer
     *
     * @param playerID
     * @return
     */
    public Drawing getTrace(Long playerID) {
        if (!traces.containsKey(playerID))
            traces.put(playerID, new Drawing());

        return traces.get(playerID);
    }

    public void updateTrace(Long playerID, Drawing newTrace) {
        traces.put(playerID, newTrace);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setMaxNbPlayer(int maxNbPlayer) {
        this.maxNbPlayer = maxNbPlayer;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Map<Long, Drawing> getTraces() {
        setTracesFromBd();
        return traces;
    }

    public Player getPlayer(Long idPlayer) {
        for (Player player : players) {
            if (player.getId().equals(idPlayer))
                return player;
        }
        return null;
    }

    /**
     * Met à jours l'attribut bdTraces pour le sauvegarder dans la base de données
     */
    public void setBdTraces() {
        for (Map.Entry<Long, Drawing> entryTrace : traces.entrySet()) {
            Trace trace = null;
            //On parcours toutes les traces existantes
            for (Trace tr : bdTraces) {
                //Si la trace existait déjà, on la récupère
                if (tr.getIdPlayer().equals(entryTrace.getKey())) {
                    trace = tr;
                    break;
                }
            }

            //La trace est null, elle n'existait pas avant, donc on la créer
            if (trace == null)
                trace = new Trace(this, (entryTrace.getKey()));

            //On set le drawing dans la trace
            trace.setJsonTrace(Utils.gson.toJson(entryTrace.getValue()));

            int index = bdTraces.indexOf(trace);
            //Si la list bdTraces contenait déjà la trace, on la supprime, pour la remetre juste après
            // (on ne peux pas faire de update)
            if (index != -1)
                bdTraces.remove(index);

            bdTraces.add(trace);
        }
    }

    /**
     *
     */
    public void setTracesFromBd() {
        for (Trace trace : bdTraces) {
            traces.put(trace.getIdPlayer(), Utils.gson.fromJson(trace.getJsonTrace(), Drawing.class));
        }
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }
}
