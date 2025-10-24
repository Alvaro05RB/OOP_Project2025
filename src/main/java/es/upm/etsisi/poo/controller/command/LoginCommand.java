package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;

public class LoginCommand implements Command {

    private final UserController controller;

    public LoginCommand(UserController controller) {
        this.controller = controller;
    }
/*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 2) {
            if (controller.login(params[0], params[1])) {
                result = "Login completed succesfully. Hello, " + params[0] + ".";
            } else result = "Login failed";
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
        checkParams(2,params.length);
        result = loginCommand(params[0],params[1]);
    }catch(InvalidParameterException e){
        result = e.getMessage();
    }
    return result;
}
    private String loginCommand(String param1, String param2){
        if (controller.login(param1,param2)){
            return  "Login completed succesfully. Hello, " + param1 + ".";
        }
        else {
            return  "Login failed";
        }
    }

    @Override
    public String toString() {
        return "login";
    }
}
