/**
 * BitbitesException.java
 * 
 * This file defines the custom exception class for the Bitbites application.
 * Used to throw application-specific errors with meaningful error messages.
 * 
 * Dependencies:
 * - java.lang.RuntimeException: Parent exception class
 */
package seedu.duke;

/**
 * BitbitesException is a custom RuntimeException for application-specific errors.
 * It provides meaningful error messages for users when operations fail.
 */
public class BitbitesException extends RuntimeException {
    public BitbitesException(String message) {
        super(message);
    }
}
