package ex5.validator;

import ex5.exceptions.SjavacException;

import static ex5.utils.Constants.FILE_ERROR;

public class InvalidFileException extends SjavacException {
    public InvalidFileException(String message) {
        super(message,FILE_ERROR);
    }
}