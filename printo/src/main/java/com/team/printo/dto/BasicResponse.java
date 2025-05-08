package com.team.printo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse {

    private String message;
    private Object data;
    private Date timestamp;

    public BasicResponse(String message, Object data) {
        this.message = message;
        this.data = data;
        this.timestamp = new Date();
    }

    public BasicResponse(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}