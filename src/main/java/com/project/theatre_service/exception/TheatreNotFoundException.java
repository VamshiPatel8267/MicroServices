package com.project.theatre_service.exception;

public class TheatreNotFoundException extends RuntimeException{
    public TheatreNotFoundException(String message){
        super(message);
    }
}
