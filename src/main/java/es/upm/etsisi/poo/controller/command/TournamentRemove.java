package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;

public class TournamentRemove implements Command {

    private final TournamentController tournamentController;

    public TournamentRemove(TournamentController controller) {
        this.tournamentController = controller;
    }
    /*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 1) {
            result = tournamentController.tournamentRemove(params[0]);
        } else if (params.length == 2) {
            result = tournamentController.tournamentRemove(params[0], params[1]);
        } else {
            result = "Incorrect number of params in Command.";
        }
        return result;
    }*/
    @Override
    public String apply(String[] params) {
        String result;
        try{
            check2Params(1,2,params.length);
            if(params.length==1){
                result = tournamentRemove1ParameterCommand(params[0]);
            }else{
                result = tournamentRemove2ParametersCommand(params[0],params[1]);
            }
        } catch (InvalidParameterException e) {
            result = e.getMessage();
        }
        return result;
    }
    private String tournamentRemove1ParameterCommand(String param1){
        return tournamentController.tournamentRemove(param1);
    }
    private String tournamentRemove2ParametersCommand(String param1, String param2){
        return tournamentController.tournamentRemove(param1,param2);
    }

    public String toString() {
        return "tournament-remove";
    }

}
