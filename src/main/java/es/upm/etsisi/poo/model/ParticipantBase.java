package es.upm.etsisi.poo.model;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "participant")
public class ParticipantBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;
    @Transient
    private Participant participantOrigen;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team IdTeam;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", referencedColumnName = "Userid")
    private Player IdPlayer;
    @Column(name="type")

    @OneToMany(mappedBy = "participant1",fetch = FetchType.EAGER)
    private Set<Matchmaking> matchmakings1;

    @OneToMany(mappedBy = "participant2",fetch = FetchType.EAGER)
    private Set<Matchmaking> matchmakings2;

    @Column(name = "type")
    private int type;

    @Column(name="name")
    private String name;

    public ParticipantBase(Participant participant){
        this.name=participant.getKey();
        this.participantOrigen = participant;
        if(participant instanceof Player){
            IdPlayer = ((Player) participant);
            type = 0;
            IdTeam = null;
        }else if(participant instanceof Team){
            IdTeam = ((Team)participant);
            type = 1;
            IdPlayer = null;
        }
    }
    public ParticipantBase(){}

    public Team getTeam(){
        return IdTeam;
    }
    public Player getPlayer(){
        return IdPlayer;
    }
    public Participant getParticipantOrigen(){
        if(this.type ==1 ){
            return getTeam();
        }else {
            return getPlayer();
        }
    }

    public String getName() {
        return name;
    }

    public int getType() {return type;}

    public static ParticipantBase getParticipantFromPlayer(Player player){
        ParticipantBase result = null;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "FROM ParticipantBase WHERE IdPlayer.userId = :id";

            Query query = session.createQuery(hql);
            query.setParameter("id", player.getUserid());
            query.setMaxResults(1);

            result = (ParticipantBase) query.getSingleResult();

            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    public static ParticipantBase getParticipantFromTeam(Team team){
        ParticipantBase result = null;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "FROM ParticipantBase WHERE IdTeam.teamId = :id";

            Query query = session.createQuery(hql);
            query.setParameter("id", team.getTeamId());
            query.setMaxResults(1);

            try {
                result = (ParticipantBase) query.getSingleResult();
            } catch (NoResultException e) {
                System.out.println("No participant found for team ID: " + team.getTeamId());
            } catch (NonUniqueResultException e) {
                System.out.println("Multiple participants found for team ID: " + team.getTeamId());
            }

            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    public static ParticipantBase getParticipantFromParticipant(Participant participant){
        ParticipantBase result = null;
        if(participant instanceof Player){
            result = getParticipantFromPlayer((Player) participant);
        }else if(participant instanceof Team){
            result = getParticipantFromTeam((Team) participant);
        }
        return result;
    }
    public Long getParticipantId(){
        return participantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParticipantBase that = (ParticipantBase) o;
        return participantId != null && participantId.equals(that.participantId);
    }

    @Override
    public int hashCode() {
        return participantId != null ? participantId.hashCode() : 0;
    }

}
