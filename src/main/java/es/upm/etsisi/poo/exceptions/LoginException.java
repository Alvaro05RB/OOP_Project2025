package es.upm.etsisi.poo.exceptions;

public class LoginException extends Exception{
    public LoginException(){
        super("Login excpetion: You have to be logged in to perform this action");
    }
}
