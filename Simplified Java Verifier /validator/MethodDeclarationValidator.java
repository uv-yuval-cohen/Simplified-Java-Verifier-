package ex5.validator;

import ex5.context.Context;
import ex5.context.MethodInfo;
import ex5.context.Parameter;
import ex5.context.VariableType;
import ex5.exceptions.SjavacException;
import ex5.utils.Constants;
import ex5.utils.ErrorMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import static ex5.utils.Constants.RESERVED_KEYWORDS;
import static ex5.utils.ErrorMessages.*;
import static ex5.utils.RegexPatterns.*;

// ===========================================
// NOTE: Validators are responsible for both:
// 1. Validating the syntax.
// 2. Updating the context if validation passes.
// ===========================================

/**
 * Validator for handling method declarations in s-Java files.
 */
public class MethodDeclarationValidator {

    /**
     * Validates a method declaration line and updates the context.
     *
     * @param line    the line containing the method declaration.
     * @param context the current context, used to track methods.
     * @return true if the method declaration is valid, false otherwise.
     *
     */

    public static boolean validate(String line, Context context, int lineNumber, boolean isMethodRun)
            throws SjavacException {
        // Log for now to test delegation
//        System.out.println("Validating method declaration: " + line);

        // Ensure method declaration is at the global scope
        if (!context.isInGlobalScope()) {
            throw new InvalidMethodDeclarationException(lineNumber, METHOD_INSIDE_METHOD);
        }
        Matcher matcher = METHOD_DECLARATION_PATTERN.matcher(line);
        matcher.matches();
        String methodName = matcher.group(Constants.METHOD_NAME_POSITION); // Group 1: Method name
        if (isMethodRun) {
            // Validate method name
            validateNameNotReserved(methodName,
                    () -> new InvalidMethodDeclarationException(lineNumber, METHOD_RESERVED_WORD
                            + methodName));

            String parameterList = matcher.group(Constants.PARAMETER_LIST_POSITION); // Group 2: Parameter list
            List<Parameter> parameterObjects = new ArrayList<>();

            // Split and validate parameters
            String[] parameters = ValidatorHelper.splitParameterList(parameterList);
            for (String param : parameters) {
                Parameter parameter = validateParameter(param.trim(), lineNumber);
                parameterObjects.add(parameter);
            }


            // Proceed to create MethodInfo
            MethodInfo methodInfo = new MethodInfo(methodName, parameterObjects);

            // Add to the global context
            context.addMethod(methodName, methodInfo);
        }


        if (!isMethodRun) {
            // Push a new scope for the method and add parameters
            context.pushMethodScope(context.getMethod(methodName,lineNumber).getParameters(), lineNumber);
        }
        return true;
    }

    private static Parameter validateParameter(String param, int lineNumber) throws SjavacException {
        //Todo: check there is already a parameter with this name

        // Check if the parameter is empty
        if (param == null || param.trim().isEmpty()) {
            throw new InvalidParameterException(EMPTY_PARAMETER, lineNumber);
        }

        // Split by whitespace (final is optional)
        String[] parts = param.trim().split(Constants.WHITESPACE);

        // Validate parameter format
        if (parts.length < Constants.MINIMUM_PARAMETER_LENGTH || parts.length >
                Constants.MAXIMUM_PARAMETER_LENGTH) {
            throw new InvalidParameterException(INVALID_PARAMETER + param,lineNumber);
        }

        boolean isFinal = parts[0].equals("final");
        if (!isFinal && parts.length==Constants.MAXIMUM_PARAMETER_LENGTH) {
            throw new InvalidParameterException(INVALID_PARAMETER + param,lineNumber);
        }
        String typeString = isFinal ? parts[1] : parts[0];
        String name = isFinal ? (parts.length > Constants.MINIMUM_PARAMETER_LENGTH ? parts[2] : null) :
                parts[1];

        // Check if type and name are valid
        if (name == null) {
            throw new InvalidParameterException(MISSING_PARAMETER_NAME + param, lineNumber);
        }

        // Validate the parameter type (throws if invalid)
        VariableType type = VariableType.fromString(typeString, lineNumber);
        if(type.equals(VariableType.UNKNOWN)){
            throw new InvalidParameterException(VARIABLE_RESERVED_WORD + name, lineNumber);
        }
        validateNameNotReserved(name, () -> new InvalidParameterException(VARIABLE_RESERVED_WORD + name,
                lineNumber));
        validateParameterName(name,lineNumber);

        // Create and return the Parameter object
        return new Parameter(name, type, isFinal);

    }

    private static void validateParameterName(String name, int lineNumber) throws InvalidParameterException {
        // Validate the name against the regex pattern
        if (!VALID_IDENTIFIER_PATTERN.matcher(name).matches()) {
            throw new InvalidParameterException(INVALID_PARAMETER_NAME + name, lineNumber);
        }

    }

    private static void validateNameNotReserved(String name, Supplier<SjavacException> exceptionSupplier)
            throws SjavacException {

        // Ensure the name is not a reserved keyword
        if (isReservedKeyword(name)) {
            throw exceptionSupplier.get();
        }
    }


    private static boolean isReservedKeyword(String name) {
        return Arrays.asList(RESERVED_KEYWORDS).contains(name);
    }

}
