package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TeamController;
import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.ExistingKeyException;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;
import es.upm.etsisi.poo.model.Admin;

public class TeamCreate implements Command {
    private final TeamController controller;
    private final UserController userController;

    public TeamCreate(TeamController controller, UserController userController) {
        this.controller = controller;
        this.userController = userController;
    }
    /*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 1) {
            if (controller.createTeam(params[0], (Admin) userController.getLoggedUser())) {
                result = "Team created successfully: " + params[0] + " was created.";
            } else {
                result = "Error: Team " + params[0] + " already exists";
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
            checkParams(1, params.length);
            result = TeamCreateCommand(params[0]);
        }catch(InvalidParameterException | ExistingKeyException e){
            result = e.getMessage();
        }
        return result;
    }
    private String TeamCreateCommand(String param1) throws ExistingKeyException {
        if(controller.createTeam(param1,(Admin) userController.getLoggedUser())) {
            return  "Team created successfully: " + param1 + " was created.";
        }else{
            throw new ExistingKeyException("Team",param1);
        }
    }

    public String toString() {
        return "team-create";
    }

}
