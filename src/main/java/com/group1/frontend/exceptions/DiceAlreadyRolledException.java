package com.group1.frontend.exceptions;

public class DiceAlreadyRolledException extends Exception{
    public DiceAlreadyRolledException(String errorMessage) {
        super(errorMessage);
    }

}
