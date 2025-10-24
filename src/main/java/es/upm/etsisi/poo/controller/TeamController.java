package es.upm.etsisi.poo.controller;

import es.upm.etsisi.poo.model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TeamController {

    public TeamController() {
        // Necesario para Hibernate
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public boolean createTeam(String name, Admin admin) {
        boolean result = false;
        if (!containsKey(name)) {
            try (Session session = SessionSingleton.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                Team team = new Team(name, admin);
                session.save(team);
                Participant participant = team;
                ParticipantBase participantBase = new ParticipantBase(participant);
                session.save(participantBase);
                result = true;
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Team get(String key) {
        Team team = null;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            team = session.createQuery("FROM Team t WHERE t.name = :key", Team.class).setParameter("key", key).setMaxResults(1).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return team;
    }

    public boolean deleteTeam(String key) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            if (containsKey(key)) {
                ParticipantBase participantBase = ParticipantBase.getParticipantFromTeam(get(key));
                session.delete(participantBase);
                session.delete(get(key));
                result = true;
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addTeam(Player player, String teamKey) {
        return get(teamKey).addPlayerToTeam(player);
    }

    public boolean playerIsInTeam(Player player, String key) {
        boolean result = false;
        if (containsKey(key) && get(key).contains(player.getKey())) {
            result = true;
        }
        return result;
    }

    public boolean removeTeam(Player player, String teamKey) {
        return get(teamKey).removePlayerfromTeam(player);
    }

}
