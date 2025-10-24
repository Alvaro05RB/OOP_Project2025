package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;
import es.upm.etsisi.poo.exceptions.ObjectNotFoundException;

public class TournamentDelete implements Command {

    private final TournamentController tournamentController;

    public TournamentDelete(TournamentController controller) {
        this.tournamentController = controller;
    }

    /*@Override
    public String apply(String[] params) {
        String result;
        if (params.length == 1) {
            if (tournamentController.tournamentDelete(params[0])) {
                result = "Tournament successfully deleted.";
            } else result = "Tournament not found.";
        } else {
            result = "Incorrect number of params in Command.";
        }
        return result;
    }*/
    @Override
    public String apply(String[] params) {
        String result;
        try{
            checkParams(1,params.length);
            result=tournamentDeleteCommand(params[0]);
        }catch (ObjectNotFoundException| InvalidParameterException e){
            result = e.getMessage();
        }
        return result;
    }
    private String tournamentDeleteCommand(String param1)throws ObjectNotFoundException{
        if (tournamentController.tournamentDelete(param1)) {
            return  "Tournament successfully deleted.";
        } else throw new ObjectNotFoundException("Tournament",param1);
    }

    public String toString() {
        return "tournament-delete";
    }

}
