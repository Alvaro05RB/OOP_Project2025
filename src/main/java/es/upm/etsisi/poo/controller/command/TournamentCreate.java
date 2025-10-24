package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.exceptions.CommandException;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;
import es.upm.etsisi.poo.model.CategoryType;
import es.upm.etsisi.poo.model.Tournament;

public class TournamentCreate implements Command {

    private final TournamentController tournamentController;

    public TournamentCreate(TournamentController tournamentController) {
        this.tournamentController = tournamentController;
    }
    /*
    @Override
    public String apply(String[] params) {
        if (params.length == 6) {
            try {
                CategoryType.valueOf(params[5]);
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
            Tournament tournament = tournamentController.tournamentCreate(params[0],
                    params[1], params[2], params[3], params[4], params[5]);
            if (tournament != null) {
                if (tournament.getName() != null) {
                    return "Tournament successfully created: " + tournament.getName() + ".";
                } else {
                    return "Tournament creation params are incorrect";
                }
            } else return "Tournament creation failed: New tournament collapses with an already created one.";
        } else return "Incorrect number of params in Command.";
    }*/

    @Override
    public String apply(String[] params) {
        String result;
        try{
            checkParams(6,params.length);
            result = tournamentCreateCommand(params[0],params[1],params[2],params[3],params[4],params[5]);
        }catch (CommandException|InvalidParameterException e){
            result=e.getMessage();
        }
        return result;
    }
    private String tournamentCreateCommand(String param1,String param2,String param3,String param4,String param5,String param6)throws CommandException{
        try {
            CategoryType.valueOf(param6);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        Tournament tournament = tournamentController.tournamentCreate(param1,
                param2, param3, param4, param5, param6);
        if (tournament != null) {
            if (tournament.getName() != null) {
                return "Tournament successfully created: " + tournament.getName() + ".";
            } else {
                throw new CommandException("Tournament creation params are incorrect");
            }
        } else throw new CommandException("Tournament creation failed: New tournament collapses with an already created one.");
    }

    public String toString() {
        return "tournament-create";
    }


}
