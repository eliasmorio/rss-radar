package fr.emorio.service;

/**
 * Custom exception for feed rescheduling failures.
 */
public class FeedRescheduleException extends RuntimeException {
    
    public FeedRescheduleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FeedRescheduleException(String message) {
        super(message);
    }
}
