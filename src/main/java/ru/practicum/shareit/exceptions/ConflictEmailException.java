package ru.practicum.shareit.exceptions;

public class ConflictEmailException extends RuntimeException{
    public ConflictEmailException(String message){
        super(message);
    }
}
