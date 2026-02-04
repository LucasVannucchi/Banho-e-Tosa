package br.com.gihpet.banhoetosa.pets.domain.exceptions;

public class PetAlreadyExistsException  extends RuntimeException{
    public PetAlreadyExistsException(String message){
        super(message);
    }
}
