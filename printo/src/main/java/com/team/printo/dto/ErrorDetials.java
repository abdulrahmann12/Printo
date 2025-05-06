package com.team.printo.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetials {

	private String message;
	private String details;
	private Date timestamp;
	
	public ErrorDetials(Date date, String message, String details) {
		this.details = details; 
		this.message = message;
		this.timestamp = date;
	}
}