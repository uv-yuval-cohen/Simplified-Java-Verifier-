package ex5.validator;

import ex5.context.Context;
import ex5.context.VariableInfo;
import ex5.context.VariableType;
import ex5.exceptions.SjavacException;
import ex5.utils.Constants;
import ex5.utils.ErrorMessages;
import ex5.utils.RegexPatterns;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ===========================================
// NOTE: Validators are responsible for both:
// 1. Validating the syntax.
// 2. Updating the context if validation passes.
// ===========================================

/**
 * Validator for handling variable declarations and assignment in s-Java files.
 */
public class VariableDeclarationAssignmentValidator {
    /**
     * Validates a variable declaration line and updates the context.
     *
     * @param line       the line containing the variable declaration.
     * @param context    the current context, used to track variables.
     * @param lineNumber the line number in the file where the declaration is.
     * @param isFirstPass and indicator that says if the validation is done in the first pass of the parser.
     */

    public static boolean validateDeclaration(String line, Context context, int lineNumber,
                                              boolean isFirstPass) throws SjavacException {
        // Determine the scope
        String scope = context.isInGlobalScope() ? "global" : "local";

        //check syntax
        Matcher syntaxMatcher = checkDeclarationSyntaxValid(line, lineNumber);
        //extract variables and their value

        Map<String,String> variableValues = extractVariablesAndValues(removeTypeAndIdentifier(line),
                lineNumber);
        String type = syntaxMatcher.group(Constants.TYPE_POSITION);
        String isFinal = syntaxMatcher.group(Constants.FINAL_POSITION);
        for(String key : variableValues.keySet()) {
            if (isFirstPass && context.isVariableDeclaredOnThisScope(key) ||
                    (context.isVariableDeclaredOnThisScope(key) && !context.isInGlobalScope()) ) {
                throw new VariableAlreadyExistsException(ErrorMessages.VARIABLE_ALREADY_DECLARED + key,
                        lineNumber);
            } else if (context.isInGlobalScope() && !isFirstPass) {
                // First pass handled that
                return true;
            }
            //if the line begins with final, make sure all vars are not null
            isFinalChecker(key, isFinal, variableValues, lineNumber);
            // check that the value fits the type
            String value = variableValues.get(key);
            if(value != null){
                //if value type matches one of the constants
                if(isValueConstant(key, variableValues)!= VariableType.UNKNOWN){
                    // yes - check that the variable type matches
                    compareConstantValueToType(key, variableValues, type, lineNumber);
                }
                else{ // maybe the value is a variable
                    variableValueChecker(context, key, value, type, lineNumber);
                }
            }
            // add variable to its scope

            addVariable(key, type, context,syntaxMatcher.group(Constants.FINAL_POSITION)!=null,
                    value != null, lineNumber);

        }
        // Temporary: Always return true for now
        return true;
    }
    /**
     * Validates a variable assignment line and updates the context.
     *
     * @param line       the line containing the variable declaration.
     * @param context    the current context, used to track variables.
     * @param lineNumber the line number in the file where the declaration is.
     */
    public static boolean validateAssignment(String line, Context context, int lineNumber)
            throws FinalVariableAssignmentException, UnAssignedVariableException,
            VariableAssignmentSyntaxException, VariableDeclarationSyntaxException,
            VariableDeclareTwiceException, VariableNotDefinedException, VariableAlreadyExistsException,
            IncompatibleTypeException, SjavacException {

        //check valid syntax
        checkAssignmentSyntaxValid(line, lineNumber);
        //identify all the variables in the assignment
        Map<String, String> variablesValues = extractVariablesAndValues(removeLineEnd(line), lineNumber);
        // for each var:
        for(String variable: variablesValues.keySet()) {

            // check that the var exists in the current or upper scope
            if(!context.isVariableDeclared(variable)) {
                throw new VariableNotDefinedException(ErrorMessages.VARIABLE_NOT_DEFINED+ variable,
                        lineNumber);
            }
            // check if it's final
            if(context.getVariableFinal(variable)){
                throw new FinalVariableAssignmentException(ErrorMessages.FINAL_VARIABLE + variable,
                        lineNumber);
            }
            // check the type is valid
            String value = variablesValues.get(variable);
            String type = context.getVariableType(variable).toString();
            if(value != null){
                //if value type matches one of the constants
                if(isValueConstant(variable, variablesValues)!= VariableType.UNKNOWN){
                    // yes - check that the variable type matches
                    compareConstantValueToType(variable, variablesValues, type, lineNumber);
                }
                else{ // maybe the value is a variable
                    variableValueChecker(context, variable, value, type, lineNumber);
                }

                if (context.isVariableUninitialized(variable)) {
                    if (context.isGlobalVariable(variable) && !context.isInGlobalScope()) {
                        context.addVariableToMethodScope(variable, context.getVariableInfo(variable),
                                lineNumber);
                    } else {
                        context.moveVariableToInitialized(variable);
                    }
                }
            }

        }
        return true;
    }

    private static String removeLineEnd(String line) {
        String newLine = line.replaceAll(Constants.END_LINE, "");
        return newLine;
    }

    private static void isFinalChecker(String key, String isFinal, Map<String, String> variableValues,
                                       int lineNumber) throws UnInitializedFinalException {
        if(isFinal !=null){
            if(variableValues.get(key) == null){
                throw new UnInitializedFinalException(ErrorMessages.UNINITIALIZED_FINAL+key, lineNumber);
            }
        }
    }

    private static void variableValueChecker(Context context, String name, String value, String type,
                                             int lineNumber) throws UnAssignedVariableException,
            VariableAssignmentSyntaxException, VariableNotDefinedException, IncompatibleTypeException {
        Matcher valueMatcher = RegexPatterns.VARIABLE_NAME_PATTERN.matcher(value);
        //if not any of the contants then maybe the value is a variable
        if(!valueMatcher.matches()){
            throw new VariableAssignmentSyntaxException(ErrorMessages.INVALID_ASSIGNMENT_SYNTAX, lineNumber);
        }
        //check that it exists
        if(!context.isVariableDeclared(value)){
            throw new VariableNotDefinedException(ErrorMessages.VARIABLE_NOT_DEFINED + value, lineNumber);
        }

        if(context.isVariableUninitialized(value)){
                throw new UnAssignedVariableException(ErrorMessages.UNASSIGNED_VARIABLE + value, lineNumber);

        }

        compareVariableTypeAndVariableValue(context, value, type, lineNumber);


    }

    private static void compareVariableTypeAndVariableValue(Context context, String value, String type,
                                                            int lineNumber) throws IncompatibleTypeException {
        if(type.equals(VariableType.BOOLEAN.toString())){
            if(!context.getVariableType(value).toString().equals(VariableType.BOOLEAN.toString()) &&
                    !context.getVariableType(value).toString().equals(VariableType.INT.toString()) &&
                    !context.getVariableType(value).toString().equals(VariableType.DOUBLE.toString())){
                throw new IncompatibleTypeException(ErrorMessages.INCOMPATIBLE_TYPE + value, lineNumber);
            }
        } else if (type.equals(VariableType.DOUBLE.toString())) {
            if(!context.getVariableType(value).toString().equals(VariableType.DOUBLE.toString()) &&
                    !context.getVariableType(value).toString().equals(VariableType.INT.toString())){
                throw new IncompatibleTypeException(ErrorMessages.INCOMPATIBLE_TYPE + value, lineNumber);
            }
        }

        else{

            if(!type.equals(context.getVariableType(value).toString())){
                throw new IncompatibleTypeException(ErrorMessages.INCOMPATIBLE_TYPE + value, lineNumber);
            }
        }
    }


    private static void addVariable(String name, String type, Context context, boolean isFinal,
                                    boolean isAssigned, int lineNumber) throws VariableAlreadyExistsException, SjavacException {
        if(type.equals(VariableType.INT.toString())){
            context.addVariable(name, new VariableInfo(isFinal, VariableType.INT, context.isInGlobalScope()),
                    isAssigned, lineNumber);
            return;
        }
        if(type.equals(VariableType.STRING.toString())){
            context.addVariable(name, new VariableInfo(isFinal, VariableType.STRING, context.isInGlobalScope()),
                    isAssigned, lineNumber);
            return;
        }
        if(type.equals(VariableType.DOUBLE.toString())){
            context.addVariable(name, new VariableInfo(isFinal, VariableType.DOUBLE, context.isInGlobalScope()),
                    isAssigned, lineNumber);
            return;
        }
        if (type.equals(VariableType.BOOLEAN.toString())){
            context.addVariable(name,new VariableInfo(isFinal, VariableType.BOOLEAN, context.isInGlobalScope())
                    ,isAssigned, lineNumber);
            return;
        }
        if (type.equals(VariableType.CHAR.toString())){
            context.addVariable(name, new VariableInfo(isFinal, VariableType.CHAR, context.isInGlobalScope())
                    ,isAssigned, lineNumber);
        }

    }

    private static VariableType isValueConstant(String key, Map<String, String> variableValues) {
        String value = variableValues.get(key);
        if(checkStringValid(value)){
            return VariableType.STRING;
        }
        else if(checkIntValid(value)){
            return VariableType.INT;
        }
        else if (checkCharValid(value)){
            return VariableType.CHAR;
        }
        else if (checkBooleanValid(value)){
            return VariableType.BOOLEAN;
        }
        else if (checkDoubleValid(value)){
            return VariableType.DOUBLE;
        }
        return VariableType.UNKNOWN;

    }
    private static void compareConstantValueToType(String key, Map<String, String> variableValues,
                                                   String type, int lineNumber)
            throws IncompatibleTypeException {
        String value = variableValues.get(key);
        if(type.equals(VariableType.STRING.toString()) &&checkStringValid(value)){
            return;
        }
        else if(type.equals(VariableType.INT.toString())&&checkIntValid(value)){
            return;
        }
        else if (type.equals(VariableType.CHAR.toString()) &&checkCharValid(value)){
            return;
        }
        else if (type.equals(VariableType.BOOLEAN.toString()) &&checkBooleanValid(value)){
            return;
        }
        else if (type.equals(VariableType.DOUBLE.toString()) &&checkDoubleValid(value)){
            return;
        }
        
        throw new IncompatibleTypeException(ErrorMessages.INCOMPATIBLE_TYPE, lineNumber);

    }

    private static boolean checkDoubleValid(String value) {
        Matcher doubleMatcher = RegexPatterns.DOUBLE_PATTERN.matcher(value);
        if(!(doubleMatcher.matches() | checkIntValid(value))){
//            throw new InvalidDoubleException(ErrorMessages.INVALID_DOUBLE);
            return false;
        }
        return true;
    }
    private static boolean checkStringValid(String value) {
        Matcher stringMatcher = RegexPatterns.STRING_PATTERN.matcher(value);
        if(!stringMatcher.matches()){
//            throw new InvalidStringException(ErrorMessages.INVALID_STRING);
            return false;
        }
        return true;
    }
    private static boolean checkIntValid(String value) {
        Matcher intMatcher = RegexPatterns.INT_PATTERN.matcher(value);
        if(!intMatcher.matches()){
//            throw new InvalidIntValueException(ErrorMessages.INVALID_INT);
            return false;
        }
        return true;
    }

    private static boolean checkCharValid(String value) {
        Matcher charMatcher = RegexPatterns.CHAR_PATTERN.matcher(value);
        if(!charMatcher.matches()){
//            throw new InvalidCharValueException(ErrorMessages.INVALID_CHAR);
            return false;
        }
        return true;
    }

    private static boolean checkBooleanValid(String value) {
        if(!(value.equals(Constants.FALSE_CONSTANT) || value.equals(Constants.TRUE_CONSTANT) ||
                checkIntValid(value)|| checkDoubleValid(value))){
//            throw new InvalidBooleanValueException("The value is not a valid boolean.");
            return false;
        }
        return true;
    }

    private static Matcher checkDeclarationSyntaxValid(String line, int lineNumber)
            throws VariableDeclarationSyntaxException {
        Matcher syntaxMatcher = RegexPatterns.VARIABLE_DECLARATION_SYNTAX.matcher(line);
        if(!syntaxMatcher.matches()){
            throw new VariableDeclarationSyntaxException(ErrorMessages.INVALID_DECLARATION_SYNTAX+ line,
                    lineNumber);
        }
        return syntaxMatcher;
    }
    private static Matcher checkAssignmentSyntaxValid(String line, int lineNumber)
            throws VariableAssignmentSyntaxException {
        Matcher syntaxMatcher = RegexPatterns.VARIABLE_ASSIGNMENT_SYNTAX_PATTERN.matcher(line);
        if(!syntaxMatcher.matches()){
            throw new VariableAssignmentSyntaxException(ErrorMessages.INVALID_ASSIGNMENT_SYNTAX+ line,
                    lineNumber);
        }
        return syntaxMatcher;
    }

    private static String removeTypeAndIdentifier(String line){
        String variablePart = line.replaceFirst(Constants.DECLARATION_START, Constants.EMPTY_LINE)
                .replaceAll(Constants.END_LINE, "");
        return variablePart;

    }
    private static  Map<String, String>extractVariablesAndValues(String variablePart, int lineNumber)
            throws VariableDeclareTwiceException {
        // Extract the variable section // a, b=5,c
        //delete final, type and ;

        Map<String, String> variableValues = new LinkedHashMap<>();
        List<String> notInitializedVariables = new ArrayList<String>();
        for(String variable: variablePart.split(Constants.DECLARATION_SEPARATOR)) { // can be var
            // name alone or with '='
            Matcher variableAssignmentMatcher = RegexPatterns.INTIALIZATION_PATTERN.matcher(variable);
            if(!variableAssignmentMatcher.find()) {
                notInitializedVariables.add(variable);
            }
            else{ // var name with =
                for(String var: notInitializedVariables){ // insert all vars before the =
                    checkVariableDeclaredOnce(var,variableValues, lineNumber); // make sure we didn't
                    // declare that already
                    variableValues.put(var,variableAssignmentMatcher.group
                            (Constants.INITIALIZATION_VALUE_POSITION));
                }
                checkVariableDeclaredOnce(variableAssignmentMatcher.group
                        (Constants.INITIALIZATION_VARIABLE_POSITION),variableValues, lineNumber);
                // now check for the var =
                variableValues.put(variableAssignmentMatcher.group
                        (Constants.INITIALIZATION_VARIABLE_POSITION), variableAssignmentMatcher.group
                        (Constants.INITIALIZATION_VALUE_POSITION));
                notInitializedVariables.clear();
                //make sure the same variable is not declared more than once.

            }
        }
        for(String variable: notInitializedVariables){
            checkVariableDeclaredOnce(variable,variableValues,lineNumber);
            variableValues.put(variable,null);
        }

        return variableValues;
    }

    private static void checkVariableDeclaredOnce(String variable, Map<String, String> variableValues,
                                                  int lineNumber) throws VariableDeclareTwiceException {
        if(variableValues.containsKey(variable)) {
            throw new VariableDeclareTwiceException(ErrorMessages.DOUBLE_DECLARATION + variable, lineNumber);
        }
    }
}