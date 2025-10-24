package es.upm.etsisi.poo.exceptions;

public class InvalidParameterException extends Exception{

    public InvalidParameterException(int expectedParameters, int recivedParameters) {
        super("Invalid number of parameters: " + expectedParameters + " parameters were expected while " + recivedParameters+" was recived");
    }
    public InvalidParameterException(int expectedParameter1,int expectedParameter2, int recivedParameters) {
        super("Invalid number of parameters: Either " + expectedParameter1 + " or "+expectedParameter2+" parameters were expected while " + recivedParameters+" was recived");
    }
    public InvalidParameterException(int expectedParameter1,int expectedParameter2,int expectedParameter3, int recivedParameters) {
        super("Invalid number of parameters: Either " + expectedParameter1 + " or "+expectedParameter2+", or "+expectedParameter3 +" parameters were expected while " + recivedParameters+" was recived");
    }
    public InvalidParameterException() {
        super("Invalid parameter: Please enter a valid parameter");
    }
}

