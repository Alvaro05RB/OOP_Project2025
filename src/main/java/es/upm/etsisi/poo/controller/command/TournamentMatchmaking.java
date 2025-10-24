package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;

public class TournamentMatchmaking implements Command {

    private final TournamentController tournamentController;

    public TournamentMatchmaking(TournamentController tournamentController) {
        this.tournamentController = tournamentController;
    }
    /*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 4) {
            result = tournamentController.tournamentMatchmaking(params[0], params[1], params[2], params[3]);
        } else if (params.length == 2) {
            result = tournamentController.tournamentMatchmaking(params[0], params[1]);
        } else {
            result = "Incorrect number of params in Command.";
        }
        return result;
    }*/
    @Override
    public String apply(String[] params) {
        String result;
        try{
            check2Params(2,4,params.length);
            if(params.length==2){
                result = tournamentMatchmaking2ParametersCommand(params[0],params[1]);
            }else {
                result = tournamentMatchmaking4ParametersCommand(params[0],params[1],params[2],params[3]);
            }
        }catch (InvalidParameterException e){
            result = e.getMessage();
        }
        return result;
    }
    private String tournamentMatchmaking2ParametersCommand(String param1, String param2){
        return tournamentController.tournamentMatchmaking(param1,param2);
    }
    private String tournamentMatchmaking4ParametersCommand(String param1, String param2,String param3,String param4){
        return tournamentController.tournamentMatchmaking(param1,param2,param3,param4);
    }

    public String toString() {
        return "tournament-matchmaking";
    }

}
