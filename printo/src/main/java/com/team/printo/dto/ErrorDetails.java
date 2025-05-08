package com.team.printo.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

	private String message;
    private Object data;
	private Date timestamp;
	
	public ErrorDetails(Date date, String message, Object data) {
		this.data = data; 
		this.message = message;
		this.timestamp = date;
	}
	
	public ErrorDetails(String message) {
		this.message = message;
		this.timestamp = new Date();
	}
}