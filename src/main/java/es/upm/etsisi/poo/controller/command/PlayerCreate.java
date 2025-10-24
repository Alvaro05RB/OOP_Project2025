package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.ExistingKeyException;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;

public class PlayerCreate implements Command {
    private final UserController controller;

    public PlayerCreate(UserController controller) {
        this.controller = controller;
    }
/*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 5) {
            if (controller.createPlayer(params[0], params[1], params[2], params[3], params[4]))
                result = "Player created successfully: " + params[0] + " " + params[3] + " added.";
            else result = "username already registered";
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
            checkParams(5, params.length);
            result = playerCreateCommand(params[0],params[1],params[2],params[3],params[4]);
        }catch (InvalidParameterException | ExistingKeyException e){
            result = e.getMessage();
        }
        return result;
    }
    private String playerCreateCommand(String param1, String param2, String param3, String param4, String param5)throws ExistingKeyException {
        if(controller.createPlayer(param1,param2,param3,param4,param5))
            return  "Player created successfully: " + param1 +" "+param4+ " added.";
        else{
            throw new ExistingKeyException("player",param1);
        }
    }
    public String toString() {
        return "player-create";
    }

}
