package es.upm.etsisi.poo.exceptions;

public class InvalidCommandException extends Exception{
    private String command;
    public InvalidCommandException(String command){
        super("Invalid command: "+command+" does not exist");
    }
}
