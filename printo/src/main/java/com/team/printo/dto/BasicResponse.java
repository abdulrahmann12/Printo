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
    
	public class Messages {
	    public static final String LOGIN_SUCCESS = "Login successful";
	    public static final String REGISTER_SUCCESS = "Register successful";
	    public static final String LOGOUT_SUCCESS = "Logged out successfully";
	    public static final String CONFIRM_EMAIL_SUCCESS = "Email confirmed successfully";
	    public static final String INVALID_PASSWORD = "Current Password is incorrect";
	    public static final String ChANGE_PASSWORD = "Password Changed Successfully";
	    public static final String USER_NOT_FOUND = "User not found";
	    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
	    public static final String CODE_SENT = "Code sent successfully";
	    
	    public static final String INVALID_REFRESH_TOKEN = "Invalid or missing refresh token";
	    public static final String NEW_TOKEN_GENERATED = "New token generated successfully";
	    public static final String COULD_NOT_EXTRACT_USER = "Could not extract user from token";
	    public static final String TOKEN_NOT_FOUND = "Token not found or invalid";
	}

}