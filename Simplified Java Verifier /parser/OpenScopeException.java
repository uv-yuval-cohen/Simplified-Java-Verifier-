package ex5.parser;

import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

public class OpenScopeException extends SjavacException {
    public OpenScopeException(String message, int lineNumber) {
        super(ErrorMessages.formatLineNumber(lineNumber, message), PARSING_ERROR);
    }
}
