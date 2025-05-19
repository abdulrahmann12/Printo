package com.team.printo.dto;

public class Messages {

	// Email Messages
    public static final String RESET_PASSWORD = "Reset Your Password";
    public static final String CONFIRM_EMAIL = "Confirm Your Email Address";
    
    // Auth Messages	
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String REGISTER_SUCCESS = "Register successful";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";
    public static final String CONFIRM_EMAIL_SUCCESS = "Email confirmed successfully";
    public static final String INVALID_PASSWORD = "Current Password is incorrect";
    public static final String CHANGE_PASSWORD = "Password Changed Successfully";
    public static final String CODE_SENT = "Code sent successfully";
    public static final String INVAILD_RESET_CODE = "Invalid reset code";
    public static final String EMAIL_EXIST = "Email is Already Exist";
    public static final String BAD_CREDENTIAL = "Invalid username or password";
    public static final String AUTH_FAILED = "Authentication failed";
    public static final String REQUEST_NOT_SUPPORTED = "Request method not supported";
    
    // Token Messages
    public static final String INVALID_REFRESH_TOKEN = "Invalid or missing refresh token";
    public static final String NEW_TOKEN_GENERATED = "New token generated successfully";
    public static final String COULD_NOT_EXTRACT_USER = "Could not extract user from token";
    public static final String TOKEN_NOT_FOUND = "Token not found or invalid";
    public static final String SESSION_EXPIRED = "Your session has expired. Please login again.";
    public static final String ACCESS_DENIED = "You do not have permission to access this resource";
    
    
    
    // User Messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    public static final String CHANGE_ROLES_ERROR = "Invalid role. Please provide one of: ADMIN, CUSTOMER, etc.";
    
    // Format Messages
    public static final String FORMAT_ERROR = "Malformed JSON request";
    
    
}
