package br.com.gihpet.banhoetosa.users.domain.exceptions;

public class DuplicateRecordException extends RuntimeException{
    public DuplicateRecordException(String message){
        super(message);
    }
}
