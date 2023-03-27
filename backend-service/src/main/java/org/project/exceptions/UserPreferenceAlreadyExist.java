package org.project.exceptions;

public class UserPreferenceAlreadyExist extends RuntimeException {
    public UserPreferenceAlreadyExist(String message) {
        super(message);
    }
}