package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;

public class StatisticShow implements Command {
    private UserController controller;

    public StatisticShow(UserController controller) {
        this.controller = controller;
    }

   /* @Override
    public String apply(String[] params) {
        String result;
        if(params.length == 0){
            result = controller.showStatistics();
        }else if (params.length == 1) {
            if (params[0].equals("-json")) {
                result = controller.toJson();
            } else if (params[0].equals("-csv")) {
                result = controller.toCSV("statistics");
            } else {
                result = "Incorrect params";
            }
        } else if (params.length == 2) { //El parametro 0 es la opción y el 1 el nombre del fichero
            if (params[0].equals("-csv")) {
                result = controller.toCSV(params[1]);
            } else {
                result = "Incorrect params";
            }
        } else {
            result = "Incorrect number of params in Command.";
        }
        return result;
    }*/
   @Override
   public String apply(String[] params) {
       String result;
       try {
           check3Params(0,1,2, params.length);
           if (params.length == 0) {
               result = controller.showStatistics();
           } else if (params.length == 1) {
               if (params[0].equals("-json")) {
                   result = controller.toJson();
               } else if (params[0].equals("-csv")) {
                   result = controller.toCSV("statistics");
               } else {
                   throw new InvalidParameterException();
               }
           } else { //El parametro 0 es la opción y el 1 el nombre del fichero
               if (params[0].equals("-csv")) {
                   result = controller.toCSV(params[1]);
               } else {
                  throw new InvalidParameterException();
               }
           }
       }catch (InvalidParameterException e){
           result = e.getMessage();
       }
       return result;
   }
    public String toString() {
        return "statistics-show";
    }

}
