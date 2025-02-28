package ex5.validator;

import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

/**
 * Exception thrown when an invalid parameter is encountered in a method declaration.
 */
public class InvalidParameterException extends SjavacException {

    public InvalidParameterException(String message, int lineNumber) {
        super(ErrorMessages.formatLineNumber(lineNumber, message),
                PARSING_ERROR); // Parsing error exit code (1)
    }
}
