package ex5.utils;

/**
 * a class with all the messages for throwing exceptions.
 */
public class ErrorMessages {
    public static final String INVALID_TYPE = "Invalid variable type: ";
    public static final String VARIABLE_ALREADY_DECLARED = "Variable already declared: ";
    public static final String VARIABLE_NOT_DECLARED = "Variable not declared: %s";
    public static final String METHOD_ALREADY_DECLARED = "Method already declared: ";
    public static final String CANNOT_POP_GLOBAL_SCOPE = "Cannot pop the global scope.";

    public static final String INVALID_ARG_COUNT = "Invalid number of arguments. Expected a single " +
            "file name.";
    public static final String INVALID_FILE_EXTENSION = "Invalid file extension. Expected: ";
    public static final String FILE_NOT_FOUND_OR_UNREADABLE = "File does not exist or is not readable: ";

    public static final String ILLEGAL_COMMENT = "Illegal comment line with leading whitespace: ";
    public static final String IO_ERROR = "I/O error: ";

    public static final String RETURN_NOT_IN_VALID_SCOPE = "Return statements are not in method scope.";
    public static final String RETURN_IN_GLOBAL_SCOPE = "Return statements are not allowed outside of a " +
            "method.";

    public static final String LINE_AFTER_RETURN_ILLEGAL = "The line following a return statement is not " +
            "allowed.";
    public static final String OPEN_SCOPE = "All scopes must be closed. ";
    public static final String INVALID_COMMENT = "Comments should not start with space";



    //variable declaration error messages
    public static final String INVALID_DECLARATION_SYNTAX = "The variable declaration syntax is incorrect: ";
    public static final String INCOMPATIBLE_TYPE = "The variable type is not compatible with the value type.";
    public static final String DOUBLE_DECLARATION = "The line is illegal because it declares a variable " +
            "twice: ";


    public static final String VARIABLE_NOT_DEFINED = "Variable not defined: ";
    public static final String  FINAL_VARIABLE = "Can't assign a value to a final variable: ";
    public static final String UNINITIALIZED_FINAL = "Final variable must be initialized: ";
    public static final String INVALID_ASSIGNMENT_SYNTAX = "Invalid assignment syntax: ";
    //If While error messages
    public static final String CONDITION_OUTSIDE_METHOD = "A conditional statement must appear inside a" +
            " method.";
    public static final String INVALID_CONDITION_SYNTAX = "Invalid condition syntax.";
    public static final String UNASSIGNED_VARIABLE = "Unassigned variable: ";


    //methods
    public static final String METHOD_INSIDE_METHOD = "Method cannot be declared inside another method.";
    public static final String METHOD_RESERVED_WORD = "Method name cannot be a reserved word: ";
    public static final String VARIABLE_RESERVED_WORD = "Variable name cannot be a reserved word: ";
    public static final String INVALID_COMMA = "Invalid comma usage in parameter list: ";
    public static final String EMPTY_PARAMETER = "Empty parameter detected: ";
    public static final String GLOBAL_METHOD_CALL = "Can't call method in global scope.";
    public static final String METHOD_NOT_DECLARED = " is not a declared method";
    public static final String METHOD_CALL_INVALID_PARAMETERS = "Incorrect number of arguments for method %s." +
            " Expected %d but got %d";
    public static final String MISSING_PARAMETER_NAME = "Missing parameter name: ";
    public static final String INVALID_PARAMETER_NAME = "Invalid parameter name: ";
    public static final String INVALID_PARAMETER = "Invalid parameter format: ";

    public static final String EXPECTED_INT = "Expected int but got: ";
    public static final String EXPECTED_DOUBLE = "Expected double but got: ";
    public static final String EXPECTED_STRING = "Expected String but got: ";
    public static final String EXPECTED_CHAR = "Expected char but got: ";
    public static final String UNEXPECTED_CONSTANT_TYPE = "Unexpected constant type: ";

    public static final String VARIABLE_NOT_INITIALIZED = "Variable %s is not initialized.";
    public static final String TYPE_MISMATCH = "Type mismatch for variable %s. Expected %s but got %s.";


    public static final String LINE_NUMBER_TEMPLATE = "Line %d - ";
    public static final String UNRETURNED_METHOD = "Method must have a return statement";
    public static final String EXPECTED_BOOLEAN = "Expected boolean but got: ";

    public static final String UNKNOWN_LINE_TYPE = "Unknown line type on line ";

    public static String formatLineNumber(int lineNumber, String message) {
        return String.format(LINE_NUMBER_TEMPLATE, lineNumber) + message;
    }

}