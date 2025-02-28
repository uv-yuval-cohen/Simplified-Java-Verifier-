package ex5.parser;

public enum LineType {
    WHITE_SPACE,
    VARIABLE_DECLARATION,
    METHOD_DECLARATION,
    METHOD_CALL,
    IF_WHILE_BLOCK_START,       // Start of an if block
    BLOCK_END,            // End of a block
    VARIABLE_ASSIGNMENT,
    RETURN_STATEMENT,
    UNKNOWN
}