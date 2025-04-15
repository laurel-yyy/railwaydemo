package com.ourgroup.railway.framework.result;

/**
 * Result utility class for creating standard API responses
 */
public final class Results {
    
    /**
     * success code
     */
    public static final Integer SUCCESS_CODE = 1;
    
    /**
     * error code
     */
    public static final Integer ERROR_CODE = 0;
    
    /**
     * success message
     */
    public static final String SUCCESS_MESSAGE = "success";
    
    /**
     * Create a success result with data
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }
    
    /**
     * Create a success result without data
     */
    public static <T> Result<T> success() {
        return Result.success();
    }
    
    /**
     * Create a failure result
     */
    public static <T> Result<T> failure(String message) {
        return Result.error(message);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Results() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}