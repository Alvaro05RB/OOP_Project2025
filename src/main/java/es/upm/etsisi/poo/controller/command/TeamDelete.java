package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TeamController;
import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.exceptions.CommandException;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;
import es.upm.etsisi.poo.exceptions.ObjectNotFoundException;

public class TeamDelete implements Command {
    private final TeamController teamController;
    private final TournamentController tournamentController;

    public TeamDelete(TeamController teamController, TournamentController tournamentController) {
        this.teamController = teamController;
        this.tournamentController = tournamentController;
    }
/*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 1) {
            if (teamController.containsKey(params[0])) {
                if (!tournamentController.inOngoingTournament(teamController.get(params[0]))) {
                    if (teamController.deleteTeam(params[0])) {
                        result = "Team deleted successfully: " + params[0] + " has been deleted.";
                    } else {
                        result = "Error deleting team";
                    }
                } else {
                    result = "Team " + params[0] + " participates in an ongoing tournament";
                }
            } else {
                result = "Team " + params[0] + " does not exist";
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
    try{
        checkParams(1,params.length);
        result=teamDeleteCommand(params[0]);
    }catch (InvalidParameterException | CommandException | ObjectNotFoundException e){
        result = e.getMessage();
    }
    return result;
}
    private String teamDeleteCommand(String param1)throws CommandException, ObjectNotFoundException {
        if(teamController.containsKey(param1)){
            if(!tournamentController.inOngoingTournament(teamController.get(param1))){
                if(teamController.deleteTeam(param1)) {
                    return  "Team deleted successfully: " + param1+ " has been deleted.";
                }else{
                    throw new CommandException("Unexpected error deleting team");
                }
            }else{
                throw new CommandException(param1,"Team","","participates in an ongoing tournament");
            }
        }else {
            throw new ObjectNotFoundException("Team",param1);
        }
    }
    public String toString() {
        return "team-delete";
    }

}
