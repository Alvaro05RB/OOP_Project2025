package es.upm.etsisi.poo.model;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team implements Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Contains",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> players;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_userid")
    private Admin creator;

    public Team(String name, Admin admin) {
        this.name = name;
        this.players = new HashSet<>();
        this.creator = admin;
    }

    public Team() {
        this.teamId = null;
        this.name = null;
        this.players = null;
        this.creator = null;
    }

    public Admin getCreator() {
        return creator;
    }

    public void setCreator(Admin creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addPlayerToTeam(Player player) {
        boolean result = false;
        try(Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            if (!players.contains(player)) {
                result = players.add(player);
                session.update(this);
                transaction.commit();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());;
        }
        return result;
    }


    public boolean removePlayerfromTeam(Player player) {
        boolean result;
        try(Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            result=  players.remove(player);
            session.update(this);
            transaction.commit();
        }
        return result;
    }

    public Double getScore() {
        double result = 0;
        for (Player player : players) {
            result = result + player.getScore();
        }
        result = result / players.size();
        return result;
    }


    @Override
    public String getKey() {
        return getName();
    }

    public String getScoreString() {
        return toString() + ": " + getScore();
    }

    public boolean contains(String key) {
        for (Player player : players) {
            if (player.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public Long getTeamId() {
        return teamId;
    }


}
