package ex5.validator;

import ex5.context.Context;
import ex5.context.VariableType;
//import ex5.exceptions.*;
import ex5.utils.Constants;
import ex5.utils.ErrorMessages;
import ex5.utils.RegexPatterns;

import java.util.ArrayList;
import java.util.regex.Matcher;

// ===========================================
// NOTE: Validators are responsible for both:
// 1. Validating the syntax.
// 2. Updating the context if validation passes.
// ===========================================

/**
 * Validator for handling if and while -block declarations in s-Java files.
 */
public class IfWhileBlockValidator {


    /**
     * Validates an if and while -block line and updates the context.
     *
     * @param line    the line containing the if-block declaration.
     * @param context the current context, used to track scopes.
     * @return true if the if-block declaration is valid, false otherwise.
     */
    public static boolean validate(String line, Context context, int lineNumber)
            throws ConditionSyntaxException, IfWhileInGlobalException, UnAssignedVariableException,
            VariableNotDefinedException, IncompatibleTypeException {

        //check that we're in a method
        if(context.isInGlobalScope()){
            throw new IfWhileInGlobalException(ErrorMessages.CONDITION_OUTSIDE_METHOD, lineNumber);
        }
        //check that the internal expression is a valid condition
        String expression = extractCondition(line);
        Matcher syntaxMatcher = RegexPatterns.CONDITION_SYNTAX.matcher(expression);
        if(!syntaxMatcher.matches()){
           throw new ConditionSyntaxException(ErrorMessages.INVALID_CONDITION_SYNTAX);
        }
        //extract all variables in the condition
        String[] booleans = expression.split(Constants.AND_OR_SEPARATOR);
        for (String bool:booleans){
            bool = bool.trim();
            Matcher boolMatcher = RegexPatterns.VARIABLE_NAME_PATTERN.matcher(bool);
            if(boolMatcher.matches() && ((!bool.equals(Constants.TRUE_CONSTANT)) &&
                    !bool.equals(Constants.FALSE_CONSTANT))){
                //check that the var is declared
                if(!context.isVariableDeclared(bool)){
                    throw new VariableNotDefinedException(ErrorMessages.VARIABLE_NOT_DEFINED, lineNumber);
                }
                //check the type is boolean / double / int
                if(context.getVariableType(bool)!= VariableType.BOOLEAN &&
                        context.getVariableType(bool)!= VariableType.INT &&
                        context.getVariableType(bool)!= VariableType.DOUBLE){
                    throw new IncompatibleTypeException(ErrorMessages.INCOMPATIBLE_TYPE, lineNumber);
                }
                // check it is assigned
                if(context.isVariableUninitialized(bool)){
                    throw new UnAssignedVariableException(ErrorMessages.UNASSIGNED_VARIABLE + bool, lineNumber);
                }

            }
        }
        context.pushScope();
        return true;
    }

    private static String extractCondition(String line) {
        String expression = line.replaceFirst(Constants.CONDITION_START, Constants.EMPTY_STRING);
        expression = expression.replaceAll(Constants.CLOSE_BOOLEAN_EXPRESSION, Constants.EMPTY_STRING);
        expression = expression.trim();
        return expression;
    }
}