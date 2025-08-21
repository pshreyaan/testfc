package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {

    private static final Logger logger = Logger.getLogger("AutomationLogger");

    public static void logAndThrow(String context, Exception e) {
        logger.log(Level.SEVERE, "Exception occurred in: " + context, e);
        throw new RuntimeException("Error at " + context + ": " + e.getMessage(), e);
    }

    public static void logOnly(String context, Exception e) {
        logger.log(Level.SEVERE, "Exception occurred in: " + context, e);
    }
}
