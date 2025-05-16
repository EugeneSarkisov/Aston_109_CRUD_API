package com.aston.crud_api.util.validation;


import com.aston.crud_api.util.user_exceptions.IncorrectInputException;

public class Validator {
    public static boolean isAgeValid(int age) throws IncorrectInputException {
        if(age < 18){
            throw new IncorrectInputException("Please, check your input. You must be older than 18 years.");
        } else {
            return true;
        }
    }

    public static boolean isUsernameValid(String username) throws IncorrectInputException {
        char[] usernameChars = username.toCharArray();
        boolean usernameCheck = true;
        for (char usernameChar : usernameChars) {
            if (usernameChar == ' ' || usernameChars.length > 21 || usernameChars.length < 5) {
                usernameCheck = false;
                break;
            }
        }
        if (!usernameCheck){
            throw new IncorrectInputException("Your username must contain from 6 to 20 letters," +
                    " numbers or symbols, except spaces!");
        } else {
            return true;
        }
    }
}
