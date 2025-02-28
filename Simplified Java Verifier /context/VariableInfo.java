package ex5.context;
/**
 * The VariableInfo class stores metadata about a variable, including its type,
 * whether it is final, and whether it is global.
 */
public class VariableInfo {
    private boolean isFinal;
    private VariableType type;
    private boolean isGlobal;

    /**
     * Constructs a new VariableInfo object with the given properties.
     *
     * @param isFinal  Indicates if the variable is final.
     * @param type     The type of the variable.
     * @param isGlobal Indicates if the variable is global.
     */
    public VariableInfo(boolean isFinal, VariableType type, boolean isGlobal) {
        this.isFinal = isFinal;
        this.type = type;
        this.isGlobal = isGlobal;
    }
    /**
     * Checks if the variable is global.
     *
     * @return true if the variable is global, false otherwise.
     */
    public boolean isGlobal(){
        return isGlobal;
    }
    /**
     * Retrieves the type of the variable.
     *
     * @return The VariableType of the variable.
     */
    public VariableType getType() {
        return type;
    }

    /**
     * Checks if the variable is marked as final.
     *
     * @return true if the variable is final, false otherwise.
     */
    public boolean isFinal() {
        return isFinal;
    }
}
