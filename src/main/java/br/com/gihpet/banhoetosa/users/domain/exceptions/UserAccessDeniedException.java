package br.com.gihpet.banhoetosa.users.domain.exceptions;

public class UserAccessDeniedException extends RuntimeException {
    public UserAccessDeniedException(String message) {
        super(message);
    }
}