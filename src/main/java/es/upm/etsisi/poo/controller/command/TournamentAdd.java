package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TeamController;
import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.*;
import es.upm.etsisi.poo.model.Player;
import es.upm.etsisi.poo.model.Team;
import es.upm.etsisi.poo.model.User;

public class TournamentAdd implements Command {

    private final TournamentController tournamentController;
    private final TeamController teamController;
    private final UserController userController;

    public TournamentAdd(TournamentController tournamentController, UserController userController,
                         TeamController teamController) {
        this.tournamentController = tournamentController;
        this.userController = userController;
        this.teamController = teamController;
    }
/*
    @Override
    public String apply(String[] params) {
        String result;
        User user = userController.getLoggedUser();
            if (user instanceof Player player) {
                if (params.length == 1) {
                    result = tournamentController.tournamentAdd(player, params[0]);
                } else if (params.length == 2) {
                    Team team = teamController.get(params[1]);
                    if (team != null && team.contains(player.getKey())) {
                        result = tournamentController.tournamentAdd(params[0], team);
                    } else {
                        result = "You are not a member of that team";
                    }
                } else {
                    result = "Incorrect number of params in Command.";
                }
            } else result = "Admins can't join a tournament";
        return result;
    }*/
@Override
public String apply(String[] params) {
    String result;
    try{
        check2Params(1,2, params.length);
        loginVerification();
        playerVerification();
        if(params.length==1){
            result = tournamentAdd1ParameterCommand(params[0]);
        }else{
            result = tournamentAdd2ParametersCommand(params[0],params[1]);
        }
    }catch(InvalidParameterException | LoginException | InvalidObjectException | ObjectNotFoundException |
           CommandException e){
        result = e.getMessage();
    }
    return result;
}
    private void loginVerification()throws LoginException{
        if(userController.getLoggedUser() == null){
            throw new LoginException();
        }
    }
    private void playerVerification()throws InvalidObjectException {
        if(!(userController.getLoggedUser() instanceof Player)){
            throw new InvalidObjectException(userController.getLoggedUser().getClass().getName());
        }
    }
    private String tournamentAdd1ParameterCommand(String param1){
        return tournamentController.tournamentAdd((Player) userController.getLoggedUser(), param1);
    }
    private String tournamentAdd2ParametersCommand(String param1, String param2)throws ObjectNotFoundException,CommandException{
        Team team = teamController.get(param2);
        if(team==null){
            throw new ObjectNotFoundException("Team",param2);
        }else if (!team.contains(((Player)userController.getLoggedUser()).getKey())){
            throw new CommandException("You are not a member of this team");
        }else{
            return tournamentController.tournamentAdd(param1,team);
        }
    }

    public String toString() {
        return "tournament-add";
    }

}
