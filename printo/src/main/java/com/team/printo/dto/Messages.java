package com.team.printo.dto;

public class Messages {

	// ==================== Email Messages ====================
    public static final String RESET_PASSWORD = "Reset your password";
    public static final String CONFIRM_EMAIL = "Confirm your email address";
    public static final String FAILED_EMAIL = "Failed to send email. Please try again later";
	
	// ==================== Auth Messages ====================
    public static final String LOGIN_SUCCESS = "Login successfully";
    public static final String REGISTER_SUCCESS = "Register successfully";
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
    public static final String SESSION_EXPIRED = "Your session has expired. Please login again";
    public static final String ACCESS_DENIED = "You do not have permission to access this resource";
    public static final String MISSING_TOKEN = "JWT token is missing";

	// ==================== User Messages ====================
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    public static final String CHANGE_ROLES_ERROR = "Invalid role. Please provide one of: ADMIN, CUSTOMER, etc.";
    public static final String USER_UPDATE_PROFILE = "User role updated successfully";
    public static final String USER_UPDATE_IMAGE = "User image updated successfully";
    public static final String DELETE_USER = "User deleted successfully";
	
	// ==================== Format Messages ====================
    public static final String FORMAT_ERROR = "Malformed JSON request";


	// ==================== Image Messages ====================
    public static final String EMPTY_IMAGE = "Image file is empty or null";
    public static final String UPLOAD_IMAGE_FAILED = "Error occurred while uploading image";
    public static final String IMAGE_NOT_FOUND = "Image not found";
    public static final String DELETE_IMAGE = "Image deleted successfully";
    public static final String IMAGE_REQUIRED = "Image is required";
	
	// ==================== Template Messages ====================

    public static final String TEMPLATE_NOT_FOUND = "Template not found";
    public static final String DELETE_TEMPLATE = "Template deleted successfully";
	
	
	// ==================== Address Messages ==================== 
    public static final String ADDRESS_NOT_FOUND = "Address not found";
    public static final String ADDRESS_NOT_BELONG_TO_USER = "Address does not belong to this user";
    public static final String EMPTY_ADDRESSES = "No addresses found for this user. Please add at least one";
    public static final String DEFAULT_ADDRESS = "Cannot delete default address";
    public static final String REQUIRE_DEFAULT = "There must be one default address";
    public static final String UPDATE_ADDRESS = "Address updated successfully";
    public static final String DELETE_ADDRESS = "Address deleted successfully";
    public static final String ADD_ADDRESS = "Address added successfully";

	// ==================== Category Messages ==================== 
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String PARENT_CATEGORY_NOT_FOUND = "Parent category not found";
    public static final String CATEGORY_ALREADY_EXISTS = "Category with this name already exists";
    public static final String DELETE_CATEGORY = "Category deleted successfully";
    public static final String ADD_CATEGORY = "Category added successfully";
    public static final String UPDATE_CATEGORY = "Category updated successfully";
    
	// ==================== Attribute Messages ==================== 
    public static final String ATTRIBUTE_NAME_ALREADY_EXISTS = "Attribute with this name already exists in the category";
    public static final String ATTRIBUTE_NOT_FOUND = "Attribute not found";
    public static final String DELETE_ATTRIBUTE = "Attribute deleted successfully";
    public static final String ADD_ATTRIBUTE = "Attribute added successfully";
    public static final String UPDATE_ATTRIBUTE = "Attribute updated successfully";
    public static final String ATTRIBUTE_NOT_BELONG_TO_CATEGORY = "Attribute does not belong to this category";

	// ==================== Product Messages ==================== 
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String DELETE_PRODUCT = "Product deleted successfully";
	
	// ==================== Design Messages ==================== 
    public static final String DESIGN_NOT_FOUND = "Design not found";
    public static final String UPLOAD_DESIGN = "Design uploaded successfully";
    public static final String DELETE_DESIGN = "Design deleted successfully";
	
	// ==================== Review Messages ==================== 
    public static final String REVIEW_NOT_FOUND = "Review not found";
    public static final String ADD_REVIEW = "Review added successfully";
    public static final String DELETE_REVIEW = "Review deleted successfully";
	
	// ==================== Favorite Messages ==================== 
    public static final String ALREADY_EXIST_IN_FAVORITE = "Product already in favorites";
    public static final String PRODUCT_NOT_EXIST_IN_FAVORITE = "Product not in favorites";
    public static final String ADD_TO_FAVORITE = "Product added successfully";
    public static final String REMOVE_FROM_FAVORITE = "Product removed successfully";
    
	// ==================== Cart Messages ====================  
    public static final String NOT_ENOUGH_STOCK = "Not enough available stock";
    public static final String USER_NOT_OWN_DESIGN = "You do not own this design";
    public static final String PRODUCT_NOT_OWN_DESIGN = "This design does not belong to the selected product";
    public static final String ATTRIBUTE_VALUE_NOT_NULL = "Attribute value ID must not be null";
    public static final String PRODUCT_NOT_OWN_ATTRIBUTE = "Attribute value does not belong to the selected product";
    public static final String CART_NOT_FOUND = "Cart not found";
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found";
    public static final String CART_ITEM_NOT_IN_CART = "This cart item does not belong to your cart";
    public static final String ADD_TO_CART = "Added to cart successfully";
    public static final String DELETE_CART_ITEM = "Cart item deleted successfully";
    public static final String CLEAR_CART = "Cart cleared successfully";
	
	// ==================== Order Messages ====================  
    public static final String EMAIL_NOT_CONFIRMED = "Email not confirmed. Please confirm your email before placing order";
    public static final String EMPTY_CART = "Cannot create an order with an empty cart";
    public static final String NOT_SET_QUANTITY = "Product quantity is not set for product";
    public static final String ORDER_CONFIRMATION = "Order confirmation";
    public static final String USER_OR_ADDRESS_IS_NULL = "User ID or Address ID must not be null";
    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String CHANGE_ORDER_STATUS = "Order status updated";
    public static final String ORDER_NOT_BELONG_TO_USER = "Order does not belong to this user";
    public static final String SAME_ORDER_STATUS = "Order status has not changed";
    public static final String INVALID_DATA = "Invalid data. Please check your request body";
    public static final String ATTRIBUTE_VALUE_NOT_AVAILABLE = "Attribute value is not available";
	
}
