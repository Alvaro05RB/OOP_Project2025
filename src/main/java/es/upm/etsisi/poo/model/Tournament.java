package es.upm.etsisi.poo.model;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "initialDate", nullable = false)
    private String initialDate;

    @Column(name = "endDate", nullable = false)
    private String endDate;

    @Column(name = "league", nullable = false)
    private String league;

    @Column(name = "sport", nullable = false)
    private String sport;

    @Column(name = "category", nullable = false)
    private CategoryType category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "participates",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private Set<ParticipantBase> participants;

    @OneToMany(mappedBy = "tournament",fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Matchmaking> matches;

    public Tournament(String name, String initialDate, String endDate, String league, String sport,
                      CategoryType category) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(initialDate, formatter);
            LocalDate.parse(endDate, formatter);
            this.name = name;
            this.initialDate = initialDate;
            this.endDate = endDate;
            this.league = league;
            this.sport = sport;
            this.category = category;
            this.participants = new HashSet<>();
            this.matches = new HashSet<>();
        } catch (DateTimeParseException e) {
            this.name = null;
            this.initialDate = null;
            this.endDate = null;
            this.league = null;
            this.sport = null;
            this.category = null;
            this.participants = null;
        }
    }

    public Tournament() {
        // Necesario para Hibernate
    }

    public String getName() {
        return name;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLeague() {
        return league;
    }

    public String getSport() {
        return sport;
    }

    public Set<ParticipantBase> getParticipants() {
        return participants;
    }

    public Set<Matchmaking> getMatches() {
        return matches;
    }

    public long getInitialDateFormat() {
        return Date.parse(initialDate);
    }

    public long getEndDateFormat() {
        return Date.parse(endDate);
    }

    public CategoryType getCategory() {
        return category;
    }

    public boolean isInMatchmake(ParticipantBase participant) {
        for (Matchmaking match : matches) {
            if (match.getParticipant1() == participant || match.getParticipant2() == participant) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return ("Name: " + getName() + ", Starting day: " + getInitialDate() + ", Ending day: " + getEndDate() +
                ", League: " + getLeague() + ", Sport: " + getSport() + ", Category: " + getCategory());
    }

    public ParticipantBase getParticipant(String name) {
        for (ParticipantBase participant : participants) {
            if (participant.getName().equals(name)) return participant;
        }
        return null;
    }
    public Long getId(){
        return id;
    }
    public Set<Participant> getParticipantsFromBase() {
        Set<Participant> result = new HashSet<>();
        for (ParticipantBase participantBase : participants) {
            Participant participant = participantBase.getParticipantOrigen();
            if (participant != null) {
                result.add(participant);
            }
        }
        return result;
    }

    public boolean addPlayerToTournament(Player player) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            result = participants.add(ParticipantBase.getParticipantFromPlayer(player));
            session.saveOrUpdate(this);
            transaction.commit();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean addTeamToTournament(Team team) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            result = participants.add(ParticipantBase.getParticipantFromTeam(team));
            session.saveOrUpdate(this);
            transaction.commit();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean removePlayerFromTournament(Player player) {
        boolean result;
        try(Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            result = participants.remove(ParticipantBase.getParticipantFromPlayer(player));
            session.saveOrUpdate(this);
            transaction.commit();
        }
        return result;
    }

    public boolean removeTeamFromTournament(Team team) {
        boolean result;
        try(Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            result = participants.remove(ParticipantBase.getParticipantFromTeam(team));
            session.saveOrUpdate(this);
            transaction.commit();
        }
        return result;
    }

    public boolean addMatchmakingToTournament(Matchmaking matchmaking) {
        boolean result;
        try(Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            result = matches.add(matchmaking);
            session.update(this);
            transaction.commit();
        }
        return result;

    }

}
