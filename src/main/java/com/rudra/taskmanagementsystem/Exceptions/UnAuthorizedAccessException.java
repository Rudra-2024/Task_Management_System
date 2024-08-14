package com.rudra.taskmanagementsystem.Exceptions;

public class UnAuthorizedAccessException extends  Exception{
    public UnAuthorizedAccessException(String message){
        super(message);
    }
}
