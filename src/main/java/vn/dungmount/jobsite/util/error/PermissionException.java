package vn.dungmount.jobsite.util.error;

public class PermissionException extends Exception {
    // Constructor that accepts a message
    public PermissionException(String message) {
        super(message);
    }
}