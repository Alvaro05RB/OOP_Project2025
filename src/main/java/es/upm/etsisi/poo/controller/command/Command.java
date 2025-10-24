package es.upm.etsisi.poo.controller.command;
import es.upm.etsisi.poo.exceptions.*;

public interface Command {

    String apply(String[] params);
    String toString();

    default void checkParams (int expectedParams, int recivedParams) throws InvalidParameterException {
        if(expectedParams != recivedParams){
            throw new InvalidParameterException(expectedParams,recivedParams);
        }
    }
    default void check2Params (int expectedParams1, int expectedParams2, int recivedParams) throws InvalidParameterException{
        if(expectedParams1 != recivedParams && expectedParams2 != recivedParams){
            throw new InvalidParameterException(expectedParams1,expectedParams2,recivedParams);
        }
    }
    default void check3Params (int expectedParams1, int expectedParams2,int expectedParams3, int recivedParams) throws InvalidParameterException{
        if(expectedParams1 != recivedParams && expectedParams2 != recivedParams && expectedParams3!=recivedParams){
            throw new InvalidParameterException(expectedParams1,expectedParams2,expectedParams3,recivedParams);
        }
    }
}
