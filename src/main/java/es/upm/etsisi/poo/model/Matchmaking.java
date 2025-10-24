package es.upm.etsisi.poo.model;

import javax.persistence.*;

@Entity
@Table(name = "matchmakings")
public class Matchmaking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchmakingid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant1s")
    private ParticipantBase participant1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant2s")
    private ParticipantBase participant2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tournaments")
    private Tournament tournament;

    public Matchmaking() {
        //necesario para Hibernate
    }

    public Matchmaking(ParticipantBase participant1, ParticipantBase participant2, Tournament tournament) {
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.tournament = tournament;
    }

    public ParticipantBase getParticipant1() {
        return participant1;
    }

    public ParticipantBase getParticipant2() {
        return participant2;
    }

    public String toString() {
        return (participant1.getName() + " vs " + participant2.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matchmaking that = (Matchmaking) o;
        return matchmakingid != null && matchmakingid.equals(that.matchmakingid);
    }

    @Override
    public int hashCode() {
        return matchmakingid != null ? matchmakingid.hashCode() : 0;
    }
}
