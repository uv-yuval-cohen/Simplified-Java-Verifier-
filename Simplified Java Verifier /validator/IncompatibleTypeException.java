package ex5.validator;
import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;


public class IncompatibleTypeException extends SjavacException {
    public IncompatibleTypeException(String message, int lineNumber) {
        super(ErrorMessages.formatLineNumber(lineNumber, message), PARSING_ERROR);
    }
}
