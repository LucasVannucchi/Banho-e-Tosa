package br.com.gihpet.banhoetosa.pets.domain.exceptions;

public class PetNotFoundException extends RuntimeException{
    public PetNotFoundException(String message){
        super(message);
    }
}
