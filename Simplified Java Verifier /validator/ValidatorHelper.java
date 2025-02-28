package ex5.validator;

//import ex5.exceptions.InvalidMethodDeclarationException;
import ex5.exceptions.SjavacException;
import ex5.utils.Constants;

import java.util.Arrays;
import java.util.List;

import static ex5.utils.ErrorMessages.INVALID_COMMA;
import static ex5.utils.RegexPatterns.COMMA_SPLIT_PATTERN;
import static ex5.utils.RegexPatterns.VALID_COMMA_USAGE_PATTERN;

/**
 * Helper class for common validation-related operations.
 */
public class ValidatorHelper {


    private ValidatorHelper() {
        // Prevent instantiation
    }

    /**
     * Splits a parameter list string into an array of individual parameters.
     *
     * @param parameterList the parameter list string to split.
     * @return an array of individual parameters, or an empty array if no parameters are found.
     */
    public static String[] splitParameterList(String parameterList) {
        String[] paramList = COMMA_SPLIT_PATTERN.split(parameterList,
                Constants.PRESERVE_TRAILING_EMPTY_STRINGS); // Use -1 to preserve trailing empty strings
        if (paramList.length==Constants.EMPTY_PARAMETER_LENGTH &&
                paramList[Constants.EMPTY_PARAMETER_POSITION].isBlank()) {
            return new String[0];
        }
        return paramList;
    }

    /**
     * Validates the usage of commas in a parameter list.
     *
     * @param parameterList the parameter list to validate.
     * @param lineNumber    the line number where the issue occurred.
     * @throws SjavacException if the comma usage is invalid.
     */
    public static void validateCommaUsage(String parameterList, int lineNumber) throws SjavacException {
        if (!VALID_COMMA_USAGE_PATTERN.matcher(parameterList).matches()) {
            throw new InvalidMethodDeclarationException(lineNumber, INVALID_COMMA + parameterList);
        }
    }
}
