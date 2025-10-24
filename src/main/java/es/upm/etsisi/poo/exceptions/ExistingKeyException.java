package es.upm.etsisi.poo.exceptions;

public class ExistingKeyException extends Exception{
    private String type;
    private String key;
    public ExistingKeyException( String type,String key){
        super("Key already in use: "+type+" named "+key+" already exists");
        this.type=type;
        this.key=key;
    }
}
