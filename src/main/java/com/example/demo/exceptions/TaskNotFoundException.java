package com.example.demo.exceptions;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(int id) {
        super("Requested task could not be found into the DB: " + id);

    }
}
