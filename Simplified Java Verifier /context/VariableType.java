package ex5.context;

//import ex5.exceptions.InvalidTypeException;
import ex5.utils.Constants;

import static ex5.utils.ErrorMessages.INVALID_TYPE;

/**
 * The VariableType enum represents the different types of variables that can be used
 * in the program. Each type is associated with a specific keyword.
 */
public enum VariableType {
    INT(Constants.KEYWORD_INT),
    DOUBLE(Constants.KEYWORD_DOUBLE),
    BOOLEAN(Constants.KEYWORD_BOOLEAN),
    STRING(Constants.KEYWORD_STRING),
    CHAR(Constants.KEYWORD_CHAR),
    UNKNOWN(Constants.UNKNOWN_TYPE);

    private final String name;

    /**
     * Constructs a VariableType with the given name.
     *
     * @param name The name of the variable type.
     */
    VariableType(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the variable type.
     *
     * @return The name of the variable type.
     */
    public String getName() {
        return name;
    }

    /**
     * Converts a string to a VariableType.
     *
     * @param name       The string representation of the variable type.
     * @param lineNumber The line number where the conversion is attempted.
     * @return The corresponding VariableType.
     * @throws InvalidTypeException If the string does not match any VariableType.
     */
    public static VariableType fromString(String name, int lineNumber) throws InvalidTypeException {
        for (VariableType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new InvalidTypeException(INVALID_TYPE + name, lineNumber);
    }

    @Override
    public String toString() {
        return name;
    }
}
