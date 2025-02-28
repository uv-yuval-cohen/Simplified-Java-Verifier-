package ex5.validator;

import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

public class MethodCallException extends SjavacException {
    public MethodCallException(int lineNumber, String message) {
        super(ErrorMessages.formatLineNumber(lineNumber, message),
                PARSING_ERROR); // Parsing error exit code (1)
    }
}