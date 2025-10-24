package es.upm.etsisi.poo.controller;

import es.upm.etsisi.poo.model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.management.Query;
import javax.persistence.TypedQuery;
import java.util.*;


public class TournamentController {
    private final MatchmakingController matchmakingController;
    private final UserController userController;

    public TournamentController( MatchmakingController matchmakingController,
                                UserController userController) {
        this.matchmakingController = matchmakingController;
        this.userController = userController;
    }

    public Tournament tournamentCreate(String name, String initialDate, String endDate,
                                       String league, String sport, String category) {
        Tournament result = null;
        Transaction transaction = null;

        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Tournament existingTournament = get(name);
            System.out.println("funciona");
            if (existingTournament == null) {
                try {
                    result = new Tournament(name, initialDate, endDate, league, sport, CategoryType.valueOf(category));
                    session.save(result);
                    transaction.commit();
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid category: " + category);
                    transaction.rollback();
                    return null;
                }
            } else {
                System.out.println("Tournament with name " + name + " already exists.");
                transaction.rollback();
                return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
        return result;
    }


    public boolean tournamentDelete(String tournamentName) {
        boolean isDeleted = false;
        Transaction transaction = null;

        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Tournament existingTournament = get(tournamentName);

            if (existingTournament != null) {
                session.delete(existingTournament);
                transaction.commit();
                isDeleted = true;
            } else {
                System.out.println("Problema 1");
                transaction.rollback();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return isDeleted;
    }

    public String tournamentAdd(Player player, String tournamentName) {
        String result;
        Tournament tournament = get(tournamentName);
        if(tournament!=null){
            if (isInTournament(player, tournament)) {
                result = "You'd already joined to this tournament";
            } else {
                if (tournament.addPlayerToTournament(player)) {
                    result = "You've been successfully joined to the tournament: " + tournament.getName() + " .";
                } else {
                    result = "Error adding the player";
                }
            }
        }
        else{
            result = "Tournament not found";
        }
        return result;
    }

    public String tournamentAdd(String tournamentName, Team team) {
        String result;
        Tournament tournament = get(tournamentName);
        if (tournament.getParticipants().contains(ParticipantBase.getParticipantFromTeam(team))) {
            result = "Your team had already joined to this tournament";
        } else {
            if (tournament.addTeamToTournament(team)) {
                result = "Your team has successfully joined the tournament: " + tournament.getName() + " .";
            } else {
                result = "Error adding the team";
            }
        }
        return result;
    }


    public String tournamentMatchmaking(String option, String participant1Name, String participant2Name,
                                         String tournamentName) {
        String result;
        if (option.equals("-m")) {
            Tournament tournament = get(tournamentName);
            if (tournament != null) {
                ParticipantBase participant1 = tournament.getParticipant(participant1Name);
                ParticipantBase participant2 = tournament.getParticipant(participant2Name);
                if (participant1 != null && participant2 != null) {
                    result = matchmakingController.manualMatchmake(participant1, participant2, tournament);
                } else {
                    result = "Participant not found into the tournament";
                }
            } else {
                result = "Tournament not found";
            }
        } else {
            result = "Incorrect option.";
        }
        return result;
    }


    public String tournamentMatchmaking(String option, String tournamentName) {
        String result;
        if (option.equals("-a")) {
            Tournament tournament = get(tournamentName);
            if (tournament != null) {
                result = matchmakingController.randomMatchmake(tournament);
            } else {
                result = "Tournament not found";
            }
        } else {
            result = "Incorrect option.";
        }
        return result;
    }


    public String tournamentRemove(String tournamentName) {
        Transaction transaction = null;

        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String playerUsername = userController.getLoggedUser().getUsername();
            Player player = session.createQuery("SELECT p FROM Player p WHERE p.username = :username", Player.class)
                    .setParameter("username", playerUsername)
                    .uniqueResult();

            if (player == null) {
                return "Error: Player not found.";
            }

            Tournament tournament = get(tournamentName);
            if (tournament == null) {
                return "Error: Tournament not found.";
            }

            if (tournament.removePlayerFromTournament(player)) {
                transaction.commit();
                return "Player successfully removed from the tournament.";
            } else {
                return "Error: Player is not part of the tournament.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            return "Error: An unexpected error occurred.";
        }
    }

    public String tournamentRemove(String teamName, String tournamentName) {
        String result;
        Transaction transaction = null;

        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String playerUsername = userController.getLoggedUser().getUsername();
            Player player = session.createQuery("SELECT p FROM Player p WHERE p.username = :username", Player.class)
                    .setParameter("username", playerUsername)
                    .uniqueResult();

            if (player == null) {
                result = "Error: Player not found.";
                return result;
            }

            Team team = session.createQuery("SELECT t FROM Team t WHERE t.name = :teamName", Team.class)
                    .setParameter("teamName", teamName)
                    .uniqueResult();

            if (team == null) {
                result = "Error: Team not found.";
                return result;
            }

            if (!team.getPlayers().contains(player)) {
                result = "Error: You are not part of this team.";
                return result;
            }

            Tournament tournament = get(tournamentName);

            if (tournament == null) {
                result = "Error: Tournament not found.";
                return result;
            }

            if (tournament.removeTeamFromTournament(team)) {
                transaction.commit();
                result = "Team successfully removed from the tournament.";
            } else {
                result = "Error: Team is not part of the tournament.";
                transaction.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            result = "Error: An unexpected error occurred.";
        }

        return result;
    }


    public String listTournaments() {
        List<Tournament> tournaments =getTournaments();
        StringBuilder result = new StringBuilder();
        if (tournaments!=null && tournaments.isEmpty()) {
            result.append("Currently, there are no tournaments");
        } else {
            if (userController.getLoggedUser() == null) {
                //TORNEOS Y PARTICIPANTES ORDENADOS RANDOM
                result.append("Displaying tournaments for non-logged user:\n");
                result.append(unknownUserListT());

            } else if (userController.getLoggedUser() instanceof Player) {
                //TORNEOS Y PARTICIPANTES ORDENADOS POR RANKING
                result.append("Displaying tournaments for logged user:\n");
                result.append(loggedUserlistT());

            } else {
                result.append("Displaying tournaments for admin:\n");
                result.append(adminlistT());
                //ELIMINAR TORNEOS FINALIZADOS
                //TORNEOS Y PARTICIPANTES ORDENADOS POR RANKING
            }
        }
        return result.toString();
    }

    private String adminlistT() {
        StringBuilder result = new StringBuilder();
        List<Tournament> tournaments = getTournaments();
        for (Tournament tournament : tournaments) {
            if (tournament.getEndDateFormat() < new Date().getTime()) {
                tournamentDelete(tournament.getName());
            } else {
                result.append(tournament);
                ArrayList<Participant> participants = sort(tournament.getParticipantsFromBase());
                sort(participants);
                result.append("\n\tParticipants:");
                result.append(getTournamentStatistics(participants));
                result.append("\n");
            }
        }
        return result.toString();
    }

    private String getTournamentStatistics(ArrayList<Participant> participants) {
        StringBuilder result = new StringBuilder();
        for (Participant participant : participants) {
            if (participant instanceof Player) {
                result.append("\n\t").append(((Player) participant).getUsername()).append(" ").append(((Player) participant).getScore());
            } else if (participant instanceof Team) {
                result.append("\n\t").append(((Team) participant).getName());
                for (Player player : ((Team) participant).getPlayers()) {
                    result.append("\n\t\t").append(player.getUsername()).append(" ").append(player.getScore());
                }
                result.append("\n\tAverage: ").append(((Team) participant).getScore());
            }
        }
        return result.toString();
    }

    private String unknownUserListT() {
        StringBuilder result = new StringBuilder();
        List<Tournament> tournaments = getTournaments();
        for (Tournament tournament : tournaments) {
            result.append(tournament.toString());
            ArrayList<Participant> participants = new ArrayList<>(tournament.getParticipantsFromBase());
            Collections.shuffle(participants);
            result.append("\n\tParticipants:");
            result.append(getTournamentStatistics(participants));
            result.append("\n");
        }
        return result.toString();
    }

    private String loggedUserlistT() {
        StringBuilder result = new StringBuilder();
        List<Tournament> tournaments = getTournaments();
        for (Tournament tournament : tournaments) {
            result.append(tournament.toString());
            ArrayList<Participant> participants = sort(tournament.getParticipantsFromBase());
            sort(participants);
            result.append("\n\tParticipants:");
            result.append(getTournamentStatistics(participants));
            result.append("\n");
        }
        return result.toString();
    }

    private ArrayList<Participant> sort(Collection<Participant> participants) {
        ArrayList<Participant> result = new ArrayList<>(participants);
        Collections.sort(result, Comparator.comparingDouble(Participant::getScore));
        return result;
    }

    public List<Tournament> getTournaments(Participant participant) {
        List<Tournament> result = null;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            ParticipantBase participantBase = ParticipantBase.getParticipantFromParticipant(participant);

            String sql = "SELECT t FROM Tournament t " +
                    "JOIN t.participants p " +
                    "WHERE p.participantId = :id";

            result = session.createQuery(sql, Tournament.class)
                    .setParameter("id", participantBase.getParticipantId())
                    .getResultList();

            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public List<Tournament> getTournaments() {
        List<Tournament> result = null;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            result = session.createQuery("FROM Tournament", Tournament.class).getResultList();

            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean playerAndTeamIntersect(List<Tournament> playerTournaments, List<Tournament> teamTournaments) {
        boolean result = false;
        for (Tournament tournament : playerTournaments) {
            if (teamTournaments.contains(tournament)) {
                result = true;
            }
        }
        return result;
    }

    //Para players

    public boolean isInTournament(Player player, Tournament tournament) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Object resultQuery = session.createQuery("SELECT 1 FROM Tournament t JOIN t.participants pb " +
                            "WHERE pb.IdPlayer.userId = :Pid AND t.id = :Tid").setParameter("Pid", player.getUserid())
                    .setParameter("Tid", tournament.getId()).setMaxResults(1).uniqueResult();
            if (resultQuery != null) {
                result = true;
            }
            List<ParticipantBase> participantBaseList = session.createQuery("SELECT pb FROM Tournament t JOIN t.participants pb WHERE pb.type = 1", ParticipantBase.class).getResultList();

            for(ParticipantBase participantBase : participantBaseList){
                Participant participant = participantBase.getParticipantOrigen();
                if(participant instanceof Team){
                    if(((Team) participant).getPlayers().contains(player)){
                        result = true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean inOngoingTournament(Player player) {
        boolean result = false;
        List<Tournament> tournaments = getTournaments(player);
        if(tournaments!=null) {
            for (Tournament tournament : tournaments) {
                if (tournament.getInitialDateFormat() < new Date().getTime() && tournament.getEndDateFormat() < new Date().getTime()) {
                    result = true;
                }
            }
        }
        return result;

    }

    public boolean inOngoingTournament(Team team) {
        boolean result = false;
        List<Tournament> tournaments = getTournaments(team);
        if(tournaments!=null) {
            for (Tournament tournament : tournaments) {
                if (tournament.getInitialDateFormat() < new Date().getTime() && tournament.getEndDateFormat() < new Date().getTime()) {
                    result = true;
                }
            }
        }
        return result;
    }


    public Tournament get(String key) {
        Tournament tournament = null;
        Transaction transaction = null;
        if(key!=null){
            try (Session session = SessionSingleton.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                tournament = session.createQuery("FROM Tournament t WHERE t.name = :key", Tournament.class).setParameter("key", key).setMaxResults(1).uniqueResult();
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                if(transaction!= null && transaction.isActive())
                    transaction.rollback();
            }}
        return tournament;
    }
}
