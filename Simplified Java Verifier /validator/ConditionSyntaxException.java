package ex5.validator;
import ex5.exceptions.SjavacException;

import static ex5.utils.Constants.PARSING_ERROR;

public class ConditionSyntaxException extends SjavacException {
    public ConditionSyntaxException(String message) {
        super(message, PARSING_ERROR);
    }
}
