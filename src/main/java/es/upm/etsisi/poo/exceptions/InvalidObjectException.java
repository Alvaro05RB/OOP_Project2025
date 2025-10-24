package es.upm.etsisi.poo.exceptions;

public class InvalidObjectException extends Exception{
    public InvalidObjectException(String type){
        super("Invalid object exception: A "+type+" cannot perform that action");
    }
}
