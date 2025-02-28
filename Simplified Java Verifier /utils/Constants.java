package ex5.utils;

import ex5.context.Context;

import java.io.File;

/**
 * The Constants class defines various constants used throughout the s-Java parser.
 * These constants include keywords, file extensions, parsing result codes, reserved keywords,
 * and regular expressions for parsing and validation.
 */
public class Constants {

    public static final String KEYWORD_INT = "int";
    public static final String KEYWORD_DOUBLE = "double";
    public static final String KEYWORD_BOOLEAN = "boolean";
    public static final String KEYWORD_STRING = "String";
    public static final String KEYWORD_CHAR = "char";
    public static final String UNKNOWN_TYPE = "unknown";

    public static final String VALID_EXTENSION = ".sjava";

    public static final int SUCCESS = 0; // Parsing successful
    public static final int FILE_ERROR = 2; // File-related errors
    public static final int PARSING_ERROR = 1; // Parsing or validation errors

    // Reserved keywords in s-Java
    public static final String[] RESERVED_KEYWORDS = {
            "int", "double", "boolean", "char", "String", "void",
            "if", "while", "return", "final" // Added "final" here
    };

    //argument validator
    public static final int VALID_ARGUMENTS_LENGTH = 1;
    public static final int VALID_FILEPATH_POSITION = 0;

    public static final String FALSE_CONSTANT = "false";
    public static final String TRUE_CONSTANT = "true";
    public static final String EMPTY_STRING = "";

    //if-while
    public static final String AND_OR_SEPARATOR = "&\\{2}|\\|{2}";
    public static final String CONDITION_START = "^\\s*(if|while)\\s*\\(";
    public static final String CLOSE_BOOLEAN_EXPRESSION = "\\s*\\)\\s*\\{$";

    //methods
    public static final int METHOD_NAME_POSITION = 1;
    public static final int PARAMETER_LIST_POSITION = 2;
    public static final int MINIMUM_PARAMETER_LENGTH = 2;
    public static final int MAXIMUM_PARAMETER_LENGTH = 3;

    public static final String WHITESPACE = "\\s+";

    //helper
    public static final int EMPTY_PARAMETER_LENGTH = 1;
    public static final int EMPTY_PARAMETER_POSITION = 0;
    public static final int PRESERVE_TRAILING_EMPTY_STRINGS = -1;

    //varaibles
    public static final int FINAL_POSITION = 1;
    public static final int TYPE_POSITION = 2;
    public static final String END_LINE = "\\s*;\\s*$";
    public static final String DECLARATION_START = "^(final\\s+)?(int|double|boolean|char|String)\\s+";
    public static final String EMPTY_LINE = "";
    public static final String DECLARATION_SEPARATOR = "\\s*,\\s*";
    public static final int INITIALIZATION_VALUE_POSITION =4;
    public static final int INITIALIZATION_VARIABLE_POSITION = 1;

    //parser
}