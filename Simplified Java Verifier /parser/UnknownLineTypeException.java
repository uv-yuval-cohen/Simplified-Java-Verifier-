package ex5.parser;

import ex5.exceptions.SjavacException;

import static ex5.utils.Constants.PARSING_ERROR;

/**
 * Exception thrown when a line in the s-Java file has an unknown or invalid type.
 */
public class UnknownLineTypeException extends SjavacException {
  public UnknownLineTypeException(String message) {
    super(message,PARSING_ERROR);
  }
}
