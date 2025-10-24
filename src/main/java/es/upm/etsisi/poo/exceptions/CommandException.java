package es.upm.etsisi.poo.exceptions;

public class CommandException extends Exception{
    public CommandException(String message){
        super(message);
    }
    public CommandException(String key, String type, String messagePt1, String messagePt2){
        super("Command exception: "+messagePt1+" "+type+" "+key+" "+messagePt2);
    }
    public CommandException(String key,String type, String extraParam,String extraType, String messagePt1, String messagePt2, String messagePt3){
        super("Command exception: "+messagePt1+" "+type+" "+key+" "+messagePt2+" "+extraType+" "+extraParam+" "+messagePt3);
    }
}
