package ex5.parser;

import ex5.exceptions.SjavacException;
import ex5.utils.ErrorMessages;

import static ex5.utils.Constants.PARSING_ERROR;

public class InvalidCommentException extends SjavacException {
  public InvalidCommentException(String message, int lineNumber) {
    super(ErrorMessages.formatLineNumber(lineNumber, message), PARSING_ERROR);
  }
}
