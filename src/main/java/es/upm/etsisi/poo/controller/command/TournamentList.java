package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;

public class TournamentList implements Command {

    private final TournamentController tournamentController;

    public TournamentList(TournamentController tournamentController) {
        this.tournamentController = tournamentController;
    }
    /*

    @Override
    public String apply(String[] params) {
        return tournamentController.listTournaments();
    }
     */
    @Override
    public String apply(String[] params) {
        String result;
        try {
            checkParams(0,params.length);
            result= tournamentController.listTournaments();
        }catch (InvalidParameterException e){
            result = e.getMessage();
        }
        return result;
    }

    public String toString() {
        return "tournament-list";
    }

}
