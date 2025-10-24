package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TeamController;
import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;
import es.upm.etsisi.poo.exceptions.ObjectNotFoundException;
import es.upm.etsisi.poo.model.Team;


public class TeamRemove implements Command {
    private final UserController playerController;
    private final TeamController teamController;

    public TeamRemove(UserController playerController, TeamController teamController) {
        this.playerController = playerController;
        this.teamController = teamController;
    }
    /*
    @Override
    public String apply(String[] params) {
        String result;
        Team team = teamController.get(params[1]);
        if(team!=null) {
            if (params.length == 2) {
                if (teamController.removeTeam(playerController.getPlayer(params[0]), params[1])) {
                    result = "Player removed from team successfully: " + params[0] + " is no longer in" + params[1];
                } else {
                    result = "Error: Player with username " + params[0] + " or tournament " + params[1] + " does not " +
                            "exist.";
                }
            } else {
                result = "Incorrect number of params in Command.";
            }
        }else {
            result = "Team "+params[1]+"does not exist";
        }
        return result;
    }
*/
    @Override
    public String apply(String[] params) {
        String result;
        try{
            checkParams(2,params.length);
            result = teamRemoveCommand(params[0],params[1]);
        }catch (InvalidParameterException | ObjectNotFoundException e){
            result = e.getMessage();
        }
        return result;
    }
    private String teamRemoveCommand(String param1,String param2)throws ObjectNotFoundException{
        if(teamController.get(param2)!=null) {
            if (teamController.removeTeam(playerController.getPlayer(param1), param2)) {
                return "Player removed from team successfully: " + param1 + " is no longer in" + param2;
            } else {
                throw new ObjectNotFoundException("Player", param1, "Tournament", param2);
            }
        }else {
            throw new ObjectNotFoundException("Team",param2);
        }
    }

    public String toString() {
        return "team-remove";
    }

}
