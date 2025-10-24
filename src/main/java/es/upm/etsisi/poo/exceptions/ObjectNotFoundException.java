package es.upm.etsisi.poo.exceptions;

public class ObjectNotFoundException extends Exception{

    public ObjectNotFoundException(String type, String key){
        super("Object not found: "+type+" named "+key+" does not exist");
    }
    public ObjectNotFoundException(String type, String key,String type2, String key2){
        super("Object not found: "+type+" named "+key+" or "+type2+" named "+key2+" does not exist");
    }
}
