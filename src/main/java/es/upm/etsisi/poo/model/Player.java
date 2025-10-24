package es.upm.etsisi.poo.model;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@DiscriminatorValue("Player")
public class Player extends User implements Participant {

    @Column(name = "Forename", nullable = false)
    private final String forename;

    @Column(name = "Surname", nullable = false)
    private final String surname;

    @Column(name = "DNI", unique = true)
    private final String DNI;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CreatorId")
    private final Admin creator;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private final Set<Statistic> statistics;

    public Player(String username,
                  String password,
                  String forename,
                  String surname,
                  String DNI,
                  Admin creator) {
        super(username, password);
        this.forename = forename;
        this.surname = surname;
        this.DNI = DNI;
        this.creator = creator;
        this.statistics = new HashSet<>();
    }

    public Player() {
        super(null, null);
        this.forename = null;
        this.surname = null;
        this.DNI = null;
        this.statistics = null;
        this.creator = null;
    }

    public String getForename() {
        return forename;
    }

    public Set<Statistic> getStatistics() {
        return statistics;
    }

    public String getDNI() {
        return DNI;
    }

    @Override
    public String toString() {
        return "Name: " + forename + "Surname: " + surname + " Score: " + " DNI :" + DNI + statistics.toString(); //
    }

    @Override
    public String getKey() {
        return getUsername();
    }

    public Double getScore() {
        double result = 0;
        for (Statistic statistic : statistics) {
            result+= statistic.getScore();
        }
        return result;
    }

    @Override
    public String getScoreString() {
        return this + ": " + getScore();
    }

    public boolean isInATeam() {

        boolean result;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            result = session.createQuery("SELECT 1 FROM Team t JOIN t.players p WHERE p.id = :playerId")
                    .setParameter("playerId", this.getUserid())
                    .setMaxResults(1)
                    .uniqueResult() != null;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return DNI.equals(player.DNI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DNI);
    }
}

