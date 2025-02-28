package ex5.context;


import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

/**
 * Exception thrown when an invalid type is encountered in s-Java.
 */
public class InvalidTypeException extends SjavacException {

    public InvalidTypeException(String message, int lineNumber) {
        super(ErrorMessages.formatLineNumber(lineNumber, message),
                PARSING_ERROR); // Return code 1 for parsing errors
    }
}
