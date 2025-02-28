package ex5.utils;

import java.util.regex.Pattern;

/**
 * All regex patterns used in the program.
 */
public class RegexPatterns {

    public static final Pattern VARIABLE_DECLARATION = Pattern.compile("^(final\\s+)?" +
        "(int|double|char|boolean|String)\\s+((([a-zA-Z]+\\w*)|(_+\\w+))(\\s*=\\s*[^,;]+)?)" +
        "(\\s*,\\s*((([a-zA-Z]+\\w*)|(_+\\w+))(\\s*=\\s*[^,;]+)?))*\\s*;$");


    public static final String ILLEGAL_COMMENT_PATTERN = "^\\s+//.*";
    public static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*$");


    // Regex patterns for line identification
    public static final Pattern METHOD_DECLARATION_PATTERN = Pattern.compile("^void\\s+([a-zA-Z][\\w]*)"
            + "\\s*\\((.*)\\)\\s*\\{\\s*$");
    public static final Pattern VARIABLE_DECLARATION_PATTERN = Pattern.compile("^\\s*(final\\s+)?" +
            "(int|double|boolean|char|String)\\s+.+;\\s*$");
    public static final Pattern IF_WHILE_BLOCK_PATTERN = Pattern.compile("^\\s*(if|while)\\s*\\(.*\\)" +
            "\\s*\\{\\s*$");
    public static final Pattern BLOCK_END_PATTERN = Pattern.compile("^\\s*}\\s*$");
    public static final Pattern VARIABLE_ASSIGNMENT_PATTERN = Pattern.compile("^\\s*\\w+\\s*=\\s*.+;" +
            "\\s*$");
    public static final Pattern RETURN_STATEMENT_PATTERN = Pattern.compile("^\\s*return\\s*;\\s*$");
    public static final Pattern METHOD_CALL_PATTERN = Pattern.compile("^([a-zA-Z][\\w]*)\\s*\\(([^)]*)" +
            "\\)\\s*;$");




    //Variable Declaration
    public static final Pattern VARIABLE_DECLARATION_SYNTAX = Pattern.compile("^(final\\s+)?" +
            "(int|double|char|boolean|String)\\s+((([a-zA-Z]+\\w*)|(_+\\w+))(\\s*=\\s*[^,;]+)?)" +
            "(\\s*,\\s*((([a-zA-Z]+\\w*)|(_+\\w+))(\\s*=\\s*[^,;]+)?))*\\s*;$");
    public static final Pattern INT_PATTERN = Pattern.compile("([+-]?\\d+)");
    public static final Pattern CHAR_PATTERN = Pattern.compile("'[^']'");
//    public static final Pattern STRING_PATTERN = Pattern.compile("\"(.*)\"");
    public static final Pattern STRING_PATTERN = Pattern.compile("(\"[^\"]*\")");
    public static final Pattern DOUBLE_PATTERN = Pattern.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)");
    public static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("([a-zA-Z]+\\w*)|(_+\\w+)");

    // Variable assignment
    public static final Pattern VARIABLE_ASSIGNMENT_SYNTAX_PATTERN = Pattern.compile("((([a-zA-Z]+\\w*)|" +
                    "(_+\\w+))(\\s*=\\s*[^,;]+))(\\s*,\\s*((([a-zA-Z]+\\w*)|(_+\\w+))(\\s*=\\s*[^,;]+)))?;");
    public static final Pattern INTIALIZATION_PATTERN = Pattern.compile("(([a-zA-Z]+\\w*)|(_+\\w+))\\s*=\\s*(.+)");
    //If While Syntax
    public static final Pattern CONDITION_SYNTAX = Pattern.compile("(\\s*(true|false|(([a-zA-Z]+\\w*)|" +
            "(_+\\w+))|([+-]?\\d+)|([+-]?(\\d+(\\.\\d*)?|\\.\\d+)))\\s*(&{2}|\\|{2}))*\\s*(true|false|" +
            "(([a-zA-Z]+\\w*)|(_+\\w+))|([+-]?\\d+)|([+-]?(\\d+(\\.\\d*)?|\\.\\d+)))\\s*");

    public static final Pattern INTEGER_CONSTANT = Pattern.compile("^\\d+$");
    public static final Pattern DOUBLE_CONSTANT = Pattern.compile("^\\d+\\.\\d+$");
    public static final Pattern CHAR_CONSTANT = Pattern.compile("^'.'$");

    //Methods
    public static final Pattern VALID_IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z][\\w]*$");

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile(",", Pattern.LITERAL);
    public static final Pattern VALID_COMMA_USAGE_PATTERN = Pattern.compile("^\\s*[^,]+(\\s*,\\s*[^,]+)*\\s*$");



}
