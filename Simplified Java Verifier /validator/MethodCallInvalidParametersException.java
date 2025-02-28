package ex5.validator;

import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

/**
 * Exception thrown when a method call has invalid parameters.
 */
public class MethodCallInvalidParametersException extends SjavacException {
    public MethodCallInvalidParametersException(int lineNumber, String methodName, int expected, int actual) {
        super(ErrorMessages.formatLineNumber(
                lineNumber,
                String.format(ErrorMessages.METHOD_CALL_INVALID_PARAMETERS, methodName, expected, actual)
        ), PARSING_ERROR);
    }
}
