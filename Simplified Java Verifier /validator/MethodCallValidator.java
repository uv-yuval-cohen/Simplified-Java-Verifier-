package ex5.validator;

import ex5.context.Context;
import ex5.context.MethodInfo;
import ex5.context.Parameter;
import ex5.context.VariableType;
//import ex5.exceptions.InvalidParameterException;
//import ex5.exceptions.MethodCallException;
//import ex5.exceptions.MethodCallInvalidParametersException;
import ex5.exceptions.SjavacException;
import ex5.utils.Constants;
import ex5.utils.ErrorMessages;
import ex5.utils.RegexPatterns;

import java.util.List;
import java.util.regex.Matcher;

import static ex5.utils.ErrorMessages.*;
import static ex5.utils.RegexPatterns.METHOD_CALL_PATTERN;
import static ex5.utils.RegexPatterns.METHOD_DECLARATION_PATTERN;

/**
 * Validator for handling method calls in s-Java files.
 * This validator is responsible for:
 *     Validating the syntax of method calls.
 *     Ensuring the method exists in the context.
 *     Checking that the parameters in the call match the method's declaration.
 *     Updating the program context if validation passes.
 */
public class MethodCallValidator {

    /**
     * Validates a method call line and ensures it complies with the method's signature.
     * This method performs the following checks:
     *     Ensures the method call is not in the global scope.
     *     Checks if the method exists in the context.
     *     Validates the number and types of parameters provided in the call.
     * If all checks pass, the method call is considered valid.
     * @param line       the line containing the method call.
     * @param context    the current context, used to track methods and variables.
     * @param lineNumber the line number in the source file for error reporting.
     * @return true if the method call is valid, false otherwise.
     * @throws SjavacException if any validation fails.
     */
    public static boolean validate( String line, Context context, int lineNumber) throws SjavacException {

        //todo: check function exists

        // Ensure method call is not at the global scope
        if (context.isInGlobalScope()) {
            throw new MethodCallException(lineNumber, GLOBAL_METHOD_CALL);
        }
        Matcher matcher = METHOD_CALL_PATTERN.matcher(line);
        matcher.matches();
        // Extract method name and parameter list
        String methodName = matcher.group(Constants.METHOD_NAME_POSITION); // Group 1: Method name
        String parameterList = matcher.group(Constants.PARAMETER_LIST_POSITION); // Group 2: Parameter list

        // Validate comma usage
        //validateCommaUsage(parameterList,lineNumber);

        // Check if the method exists
        // Retrieve method information
        MethodInfo methodInfo = context.getMethod(methodName, lineNumber);
        List<Parameter> declaredParameters = methodInfo.getParameters();

        // Split and count parameters in the call
        String[] callParameters = ValidatorHelper.splitParameterList(parameterList);

        for (String param : callParameters) {
            validateParameter(param.trim(), lineNumber);
        }

        // Validate parameter count
        validateParameterCount(methodName, lineNumber, declaredParameters.size(), callParameters.length);

        // Validate parameter types
        for (int i = 0; i < callParameters.length; i++) {
            String callParameter = callParameters[i].trim();
            Parameter expectedParameter = declaredParameters.get(i);

            // Determine if the parameter is a constant or variable
            if (isConstant(callParameter)) {
                validateConstantType(callParameter, expectedParameter.getType(), lineNumber);
            } else {
                validateVariableType(callParameter, expectedParameter.getType(), context, lineNumber);
            }
        }


        return true;
    }

    private static void validateParameter(String param, int lineNumber) throws InvalidParameterException {
        // Check if the parameter is empty
        if (param == null || param.trim().isEmpty()) {
            throw new InvalidParameterException(EMPTY_PARAMETER, lineNumber);
        }
    }

    private static void validateParameterCount(
            String methodName, int lineNumber, int expected, int actual)
            throws MethodCallInvalidParametersException {
        if (expected != actual) {
            throw new MethodCallInvalidParametersException(lineNumber, methodName, expected, actual);
        }
    }

    private static boolean isConstant(String parameter) {
        return RegexPatterns.INT_PATTERN.matcher(parameter).matches() ||
                RegexPatterns.DOUBLE_PATTERN.matcher(parameter).matches() ||
                RegexPatterns.STRING_PATTERN.matcher(parameter).matches() ||
                RegexPatterns.CHAR_CONSTANT.matcher(parameter).matches()||
                (isBoolean(parameter));
    }

    private static void validateConstantType(String parameter, VariableType expectedType, int lineNumber)
            throws MethodCallException {
        switch (expectedType) {
            case INT:
                if (!RegexPatterns.INT_PATTERN.matcher(parameter).matches()) {
                    throw new MethodCallException(lineNumber, ErrorMessages.EXPECTED_INT + parameter);
                }
                break;
            case DOUBLE:
                if (!RegexPatterns.DOUBLE_PATTERN.matcher(parameter).matches()) {
                    throw new MethodCallException(lineNumber, ErrorMessages.EXPECTED_DOUBLE + parameter);
                }
                break;
            case STRING:
                if (!RegexPatterns.STRING_PATTERN.matcher(parameter).matches()) {
                    throw new MethodCallException(lineNumber, ErrorMessages.EXPECTED_STRING + parameter);
                }
                break;
            case CHAR:
                if (!RegexPatterns.CHAR_CONSTANT.matcher(parameter).matches()) {
                    throw new MethodCallException(lineNumber, ErrorMessages.EXPECTED_CHAR + parameter);
                }
                break;
            case BOOLEAN:
                if ( !RegexPatterns.INT_PATTERN.matcher(parameter).matches()
                && !RegexPatterns.DOUBLE_PATTERN.matcher(parameter).matches()
                    && !isBoolean(parameter)) {
                    throw new MethodCallException(lineNumber, ErrorMessages.EXPECTED_BOOLEAN + parameter);
                }
                break;
            default:
                throw new MethodCallException(lineNumber, ErrorMessages.UNEXPECTED_CONSTANT_TYPE + expectedType);
        }
    }

    private static void validateVariableType(String variableName, VariableType expectedType, Context context,
                                             int lineNumber) throws SjavacException, MethodCallException {
        // Ensure the variable is declared
        if (!context.isVariableDeclared(variableName)) {
            throw new MethodCallException(
                    lineNumber,
                    String.format(ErrorMessages.VARIABLE_NOT_DECLARED, variableName)
            );
        }

        // Ensure the variable is initialized
        if (context.isVariableUninitialized(variableName)) {
            throw new MethodCallException(
                    lineNumber,
                    String.format(ErrorMessages.VARIABLE_NOT_INITIALIZED, variableName)
            );
        }

        // Validate the variable type
        VariableType actualType = context.getVariableType(variableName);
        if (!actualType.equals(expectedType)) {
            throw new MethodCallException(
                    lineNumber,
                    String.format(ErrorMessages.TYPE_MISMATCH, variableName, expectedType, actualType)
            );
        }
    }

    private static Boolean isBoolean(String parameter) {
        return parameter.equals(Constants.TRUE_CONSTANT)||parameter.equals(Constants.FALSE_CONSTANT);
    }


}
