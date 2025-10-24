package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;

public class LogoutCommand implements Command {
    private final UserController controller;

    public LogoutCommand(UserController controller) {
        this.controller = controller;
    }
/*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 0) {
            if (controller.logout()) {
                result = "Logged out successfully";
            } else {
                result = "No user is currently logged";
            }
        } else {
            result = "Incorrect number of params in Command.";
        }
        return result;
    }

    public String toString() {
        return "logout";
    }
*/
@Override
public String apply(String[] params) {
    String result;
    try{
        checkParams(0, params.length);
        result = logoutCommand();
    }catch (InvalidParameterException e){
        result = e.getMessage();
    }

    return result;
}
    private String logoutCommand(){
        if(controller.logout()){
            return  "Logged out successfully";
        }
        else{
            return "No user is currently logged";
        }
    }
    public String toString() {
        return "logout";
    }
}
