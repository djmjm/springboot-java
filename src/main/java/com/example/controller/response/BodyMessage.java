package com.example.controller.response;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class BodyMessage {

    private int statusCode;
    private Map<String, Object> response = new HashMap<>();

    public BodyMessage(Object data, HttpStatus status){
        var statusString = status;
        var statusCode = status.value();
        this.statusCode = statusCode;


        response.put("status", statusString);
        response.put("status_code", statusCode);
        response.put("data", data);
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
