package ex5.context;

/**
 * The Parameter class represents a parameter of a method, including its name, type, and finality.
 */
public class Parameter {
    private final String name; // parameter name
    private final VariableType type; // parameter type
    private final boolean isFinal; // indicates if the parameter marked as final

    /**
     * Constructs a new Parameter with the given name, type, and finality.
     *
     * @param name    The name of the parameter.
     * @param type    The type of the parameter.
     * @param isFinal Whether the parameter is marked as final.
     */
    public Parameter(String name, VariableType type, boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
    }

    /**
     * Retrieves the name of the parameter.
     *
     * @return The name of the parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the type of the parameter.
     *
     * @return The VariableType of the parameter.
     */
    public VariableType getType() {
        return type;
    }

    /**
     * Checks if the parameter is marked as final.
     *
     * @return true if the parameter is final, false otherwise.
     */
    public boolean isFinal() {
        return isFinal;
    }
}
