package org.project.exceptions;

public class TransactionFailed extends RuntimeException {
    public TransactionFailed(String message) {
        super(message);
    }
}