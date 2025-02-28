package ex5.validator;

import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

/**
 * Exception thrown when a method with the same name already exists in the global context.
 */
public class InvalidReturnPositionException extends SjavacException {

    public InvalidReturnPositionException(String message, int lineNumber) {
        super(ErrorMessages.formatLineNumber(lineNumber, message),
                PARSING_ERROR); // Parsing error exit code (1)
    }
}