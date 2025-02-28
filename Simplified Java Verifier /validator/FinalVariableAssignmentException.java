package ex5.validator;

import ex5.exceptions.SjavacRuntimeException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

public class FinalVariableAssignmentException extends SjavacRuntimeException {
  public FinalVariableAssignmentException(String message, int lineNumber) {
    super(ErrorMessages.formatLineNumber(lineNumber, message),
            PARSING_ERROR);
  }
}

