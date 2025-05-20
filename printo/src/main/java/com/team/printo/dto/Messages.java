package com.team.printo.dto;

public class Messages {

	// ==================== Email Messages ====================
	public static final String RESET_PASSWORD = "Reset your password";
	public static final String CONFIRM_EMAIL = "Confirm your email address";
	public static final String FAILED_EMAIL = "Failed to send email. Please try again later";
	
	// ==================== Auth Messages ====================
	public static final String LOGIN_SUCCESS = "Login successful";
	public static final String REGISTER_SUCCESS = "Register successful";
	public static final String LOGOUT_SUCCESS = "Logged out successfully";
	public static final String ALREADY_LOGGED_OUT = "You are already logged out";
	public static final String INVALID_CONFIRM_EMAIL = "Invalid confirmation code";
	public static final String CONFIRM_EMAIL_SUCCESS = "Email confirmed successfully";
	public static final String INVALID_PASSWORD = "Current password is incorrect";
	public static final String CHANGE_PASSWORD = "Password changed successfully";
	public static final String CODE_SENT = "Code sent successfully";
	public static final String INVALID_RESET_CODE = "Invalid reset code";
	public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
	public static final String BAD_CREDENTIALS = "Invalid username or password";
	public static final String AUTH_FAILED = "Authentication failed";
	public static final String REQUEST_NOT_SUPPORTED = "Request method not supported";

	// ==================== Token Messages ====================
	public static final String INVALID_REFRESH_TOKEN = "Invalid or missing refresh token";
	public static final String NEW_TOKEN_GENERATED = "New token generated successfully";
	public static final String COULD_NOT_EXTRACT_USER = "Unable to extract username from token";
	public static final String TOKEN_NOT_FOUND = "Token not found or invalid";
	public static final String TOKEN_EXPIRED_OR_REVOKED = "Token expired or revoked";
	public static final String SESSION_EXPIRED = "Your session has expired. Please login again.";
	public static final String ACCESS_DENIED = "You do not have permission to access this resource";
	public static final String MISSING_TOKEN = "JWT token is missing";

	// ==================== User Messages ====================
	public static final String USER_NOT_FOUND = "User not found";
	public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
	public static final String CHANGE_ROLES_ERROR = "Invalid role. Please provide one of: ADMIN, CUSTOMER, etc";
	public static final String USER_UPDATE_PROFILE = "User role updated successfully";
	public static final String USER_UPDATE_IMAGE = "User image updated successfully";
	public static final String DELETE_USER = "User deleted successfully";
	
	// ==================== Format Messages ====================
	public static final String FORMAT_ERROR = "Malformed JSON request";


	// ==================== Image Messages ====================
	public static final String EMPTY_IMAGE= "Image file is empty or null";
	public static final String UPLOAD_IMAGE_FAILED= "Error occurred while uploading image: ";
    
	// ==================== Address Messages ==================== 
	public static final String ADDRESS_NOT_FOUND = "Address not found";
	public static final String ADDRESS_NOT_BELONG_TO_USER = "Address does not belong to this user";
	public static final String EMPTY_ADDRESSES = "No addresses found for this user. Please add at least one";
	public static final String DEFAULT_ADDRESS = "Cannot delete default address";
	public static final String UPDATE_ADDRESS = "Address updated successfully";
	public static final String DELETE_ADDRESS = "Address deleted successfully";
	
	// ==================== Attribute Messages ==================== "Attribute deleted successfully"
	public static final String ATTRIBUTE_ALREADY_EXISTS = "Attribute with this name already exists in the category";
	public static final String ATTRIBUTE_NOT_FOUND =  "Attribute not found";
	public static final String DELETE_ATTRIBUTE =  "Attribute deleted successfully";
	// ==================== Category Messages ==================== 
	public static final String CATEGORY_NOT_FOUND =  "Category not found";
	

}
