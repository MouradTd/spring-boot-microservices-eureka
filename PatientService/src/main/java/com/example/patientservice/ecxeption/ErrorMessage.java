package com.example.patientservice.ecxeption;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Setter
@Getter
public class ErrorMessage {
    private String message;
    private int status;
    private Date timestamp ;
    private Object errors;

    public ErrorMessage(String message, int status, Date timestamp, List<String> errors) {
        super();
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.errors = errors;
    }


}
