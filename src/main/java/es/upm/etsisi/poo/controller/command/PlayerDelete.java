package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TeamController;
import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.CommandException;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;
import es.upm.etsisi.poo.exceptions.ObjectNotFoundException;

public class PlayerDelete implements Command {
    private final UserController userController;
    private final TournamentController tournamentController;
    private final TeamController teamController;

    public PlayerDelete(UserController userController, TournamentController tournamentController,
                        TeamController teamController) {
        this.userController = userController;
        this.tournamentController = tournamentController;
        this.teamController = teamController;
    }
/*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 1) {
            if (userController.exists(params[0])) {
                if (!userController.getPlayer(params[0]).isInATeam()) {
                    if (!tournamentController.inOngoingTournament(userController.getPlayer(params[0]))) {
                        if (userController.removeUser(params[0])) {
                            result = "Player " + params[0] + " deleted successfully";
                        } else {
                            result = "Error deleting player " + params[0];
                        }
                    } else {
                        result = "Error: Player " + params[0] + " participates in an ongoing tournament";
                    }
                } else {
                    result = "Error: Player " + params[0] + " is in a team";
                }
            } else {
                result = "Player " + params[0] + " not found";
            }
        } else {
            result = "Incorrect number of params in Command.";
        }
        return result;
    }
    */
    @Override
    public String apply(String[] params) {
        String result;
        try {
            checkParams(1,params.length);
            result = playerDeleteCommand(params[0]);
        }catch (InvalidParameterException | CommandException | ObjectNotFoundException e){
            result = e.getMessage();
        }
        return result;
    }
    private String playerDeleteCommand(String param1)throws CommandException, ObjectNotFoundException {
        if(userController.exists(param1)) {
            if(!userController.getPlayer(param1).isInATeam()) {
                if (!tournamentController.inOngoingTournament(userController.getPlayer(param1))) {
                    if (userController.removeUser(param1)) {
                        return  "Player " + param1 + " deleted successfully";
                    } else {
                        throw new CommandException("Unexpected error deleting player");
                    }
                } else {
                    throw new CommandException(param1,"Player","","participates in an ongoing tournament");
                }
            }else {
                throw new CommandException(param1,"Player","", "is in a team");
            }
        }else {
            throw new ObjectNotFoundException("Player",param1);
        }
    }
    public String toString() {
        return "player-delete";
    }

}
