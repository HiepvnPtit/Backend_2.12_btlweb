package javaSpring.exception;

public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "Uncategorize error."),
    INVALID_KEY(1001, "Invalid message key"), // for debug
    USER_EXISTED(1002, "User existed."),
    USERNAME_INVALID(1003, "Username must be at least 3 characters."),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters."),
    USER_NOT_EXISTED(1005, "User not existed."),
    UNAUTHENTICATED(1006, "Unauthenticated."),
    ;

    private int code;
    private String message;
    

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    
}
