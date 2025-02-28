package ex5.validator;

//import ex5.exceptions.InvalidFileException;
import ex5.utils.Constants;
import ex5.utils.ErrorMessages;

import java.io.File;

/**
 * A utility class for validating the command-line arguments and file input for the program.
 */
public class ArgumentValidator {

    /**
     * Validates the command-line arguments to ensure a single valid file is provided.
     *
     * @param args the command-line arguments provided to the program.
     * @return the validated file path as a string.
     * @throws InvalidFileException if the arguments are invalid or the file is not accessible.
     */

    public static File validateFile(String[] args) throws InvalidFileException {
        if (args.length != Constants.VALID_ARGUMENTS_LENGTH) {
            throw new InvalidFileException(ErrorMessages.INVALID_ARG_COUNT);
        }

        String filePath = args[Constants.VALID_FILEPATH_POSITION];

        // Check file extension
        if (!filePath.endsWith(Constants.VALID_EXTENSION)) {
            throw new InvalidFileException(ErrorMessages.INVALID_FILE_EXTENSION + Constants.VALID_EXTENSION);
        }

        // Check if the file exists and is readable
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            throw new InvalidFileException(ErrorMessages.FILE_NOT_FOUND_OR_UNREADABLE + filePath);
        }

        return file;
    }
}