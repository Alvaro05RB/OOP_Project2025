package es.upm.etsisi.poo.controller;
import es.upm.etsisi.poo.model.Matchmaking;
import es.upm.etsisi.poo.model.ParticipantBase;
import es.upm.etsisi.poo.model.Tournament;

public class MatchmakingController {


    public MatchmakingController() {}

    public String manualMatchmake(ParticipantBase participant1, ParticipantBase participant2, Tournament tournament) {
        StringBuilder resul = new StringBuilder();
        if (!tournament.isInMatchmake(participant1) && !tournament.isInMatchmake(participant2)) {
            if (tournament.addMatchmakingToTournament(new Matchmaking(participant1, participant2, tournament))) {
                resul.append("Matchmake added successfully");
            }
        } else resul.append("These participants already have a matchmaking in this tournament");
        if (resul.toString().contains("Matchmake added successfully")) {
            for (Matchmaking matchmaking : tournament.getMatches()) {
                resul.append("\n\t").append(matchmaking.toString());
            }
        }
        return resul.toString();
    }

    public String randomMatchmake(Tournament tournament) {
        StringBuilder result = new StringBuilder();
        if (tournament.getParticipants().size() % 2 == 0) {
            tournament.getMatches().clear();
            int index1, index2;
            boolean[] used = new boolean[tournament.getParticipants().size()]; //se inicializan todos los valores por defecto a false
            ParticipantBase[] aux = new ParticipantBase[tournament.getParticipants().size()];
            int j = 0;
            for (ParticipantBase participant : tournament.getParticipants()) {
                aux[j] = participant;
                j++;
            }
            for (int i = 0; i < tournament.getParticipants().size() / 2; i++) {
                index1 = chooseParticipant(used);
                index2 = chooseParticipant(used);
                Matchmaking randomMatchmake = new Matchmaking(aux[index1], aux[index2], tournament);
                if (tournament.addMatchmakingToTournament(randomMatchmake)) {
                    result.append("\t").append(randomMatchmake).append("\n");
                } else result.append("Fail when adding the matchmake to the list");
            }
        } else {
            result.append("The total number of players isn't even");
        }
        return result.toString();
    }

    private int chooseParticipant(boolean[] used) {
        int resul = 0;
        boolean chosen = true;
        while (chosen) {
            resul = (int) (Math.random() * used.length);
            chosen = used[resul];
        }
        used[resul] = true;
        return resul;
    }
}