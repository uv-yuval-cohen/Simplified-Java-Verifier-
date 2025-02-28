package ex5.context;

import ex5.utils.ErrorMessages;

import java.util.HashMap;
import java.util.Map;
/**
 * The Scope class represents a scope in the program, containing initialized and uninitialized variables.
 * It provides mechanisms to manage variables within the current scope and resolve variables in parent scopes.
 */
public class Scope {


    private final Map<String, VariableInfo> initializedVariables; // Variable name -> Type
    private final Scope parent; // Reference to the parent scope
    private final Map<String, VariableInfo> uninitializedVariables; // uninitialized variables in the scope

    /**
     * Constructs a new Scope.
     *
     * @param parent The parent scope, or null if this is the global scope.
     */
    public Scope(Scope parent) {
        this.initializedVariables = new HashMap<>();
        this.parent = parent;
        this.uninitializedVariables = new HashMap<>();

    }

    /**
     * Adds an initialized variable to the current scope.
     *
     * @param name       The name of the variable.
     * @param info       The variable's information.
     * @param lineNumber The line number where the variable is declared.
     * @throws VariableAlreadyExistsException If the variable already exists in the current scope.
     */
    public void addInitializedVariable(String name, VariableInfo info, int lineNumber)
            throws VariableAlreadyExistsException
    {
        if (initializedVariables.containsKey(name)) {
            throw new VariableAlreadyExistsException(ErrorMessages.VARIABLE_ALREADY_DECLARED + name,
                    lineNumber);
        }
        initializedVariables.put(name, info);
    }
    /**
     * Adds an uninitialized variable to the current scope.
     *
     * @param name       The name of the variable.
     * @param info       The variable's information.
     */
    public void addUninitializedVariable(String name, VariableInfo info) {

        uninitializedVariables.put(name, info);
    }


    /**
     * Checks if a variable is declared in the current or parent scopes.
     *
     * @param name The name of the variable.
     * @return true if the variable is declared, false otherwise.
     */
    public boolean isDeclared(String name) {
        if (initializedVariables.containsKey(name)|| uninitializedVariables.containsKey(name)) {
            return true;
        }
        return parent != null && parent.isDeclared(name);
    }


    /**
     * Checks if a variable is declared in the current scope.
     *
     * @param name The name of the variable.
     * @return true if the variable is declared in the current scope, false otherwise.
     */
    public boolean isVariableDeclaredOnThisScope(String name) {
        return (initializedVariables.containsKey(name)|| uninitializedVariables.containsKey(name));
    }

    /**
     * Checks if a variable is uninitialized in the current or parent scopes.
     *
     * @param name The name of the variable.
     * @return true if the variable is uninitialized, false otherwise.
     */
    public boolean isVariableUninitialized(String name) {
        if (uninitializedVariables.containsKey(name)) {
            return true;
        }
        if (initializedVariables.containsKey(name)) {
            return false;
        }
        return parent != null && parent.isVariableUninitialized(name);
    }

    /**
     * Moves a variable from uninitialized to initialized in the current or parent scopes.
     *
     * @param name The name of the variable to move.
     */
    public void moveVariableToInitialized(String name) {
        // Check if the variable exists in the uninitialized map
        if (uninitializedVariables.containsKey(name)) {
            // Retrieve the variable info
            VariableInfo info = uninitializedVariables.remove(name);

            // Add it to the initialized map
            initializedVariables.put(name, info);
        } else {
            // If the variable is not uninitialized, check the parent scope
            if (parent != null) {
                parent.moveVariableToInitialized(name);

            }
        }
    }

    /**
     * Retrieves the type of a variable, searching in the current and parent scopes.
     *
     * @param name The name of the variable.
     * @return The variable's type.
     * @throws IllegalArgumentException If the variable does not exist.
     */
    public VariableType getType(String name) throws IllegalArgumentException {
        if (initializedVariables.containsKey(name)) {
            return initializedVariables.get(name).getType();
        } else if (uninitializedVariables.containsKey(name)) {
            return uninitializedVariables.get(name).getType();
        }
        if (parent != null) {
            return parent.getType(name);
        }
        throw new IllegalArgumentException(ErrorMessages.VARIABLE_NOT_DECLARED + name);
    }

    /**
     * Checks if a variable is marked as final, searching in the current and parent scopes.
     *
     * @param name The name of the variable.
     * @return true if the variable is final, false otherwise.
     * @throws IllegalArgumentException If the variable does not exist.
     */
    public boolean getIsFinal(String name) throws IllegalArgumentException{
        if (initializedVariables.containsKey(name)) {
            return initializedVariables.get(name).isFinal();
        } else if (uninitializedVariables.containsKey(name)) {
            return uninitializedVariables.get(name).isFinal();
        }
        if (parent != null) {
            return parent.getIsFinal(name);
        }
        throw new IllegalArgumentException(ErrorMessages.VARIABLE_NOT_DECLARED + name);
    }

    /**
     * Checks if a variable is global, searching in the current and parent scopes.
     *
     * @param name The name of the variable.
     * @return true if the variable is global, false otherwise.
     * @throws IllegalArgumentException If the variable does not exist.
     */
    public boolean isGlobal(String name) {
        if (initializedVariables.containsKey(name)) {
            return initializedVariables.get(name).isGlobal();
        } else if (uninitializedVariables.containsKey(name)) {
            return uninitializedVariables.get(name).isGlobal();
        }
        if (parent != null) {
            return parent.isGlobal(name);
        }
        throw new IllegalArgumentException(ErrorMessages.VARIABLE_NOT_DECLARED + name);
    }


    private boolean isInMethodScope() {
        if (parent ==null){
            return false;
        }
        return parent.parent == null;
    }

    /**
     * Adds a variable specifically to the method scope.
     *
     * @param name       The name of the variable.
     * @param info       The variable's information.
     * @param lineNumber The line number where the variable is declared.
     * @throws VariableAlreadyExistsException If the variable already exists in the scope.
     */
    public void addVariableToMethodScope(String name, VariableInfo info, int lineNumber)
            throws VariableAlreadyExistsException
    {
        if (parent == null) {
            return;
        }
        if (!isInMethodScope()) {
            parent.addVariableToMethodScope(name, info, lineNumber);
        }
        // in method scope
        addInitializedVariable(name,info,lineNumber);
    }
    /**
     * Retrieves information about a variable by its name, searching in the current and parent scopes.
     *
     * @param name The name of the variable.
     * @return The variable's information.
     * @throws IllegalArgumentException If the variable does not exist.
     */
    public VariableInfo getVariableInfo (String name) {
        if (initializedVariables.containsKey(name)) {
            return initializedVariables.get(name);
        }
        if (uninitializedVariables.containsKey(name)) {
            return uninitializedVariables.get(name);
        }
        if (parent != null) {
            return parent.getVariableInfo(name);
        }
        throw new IllegalArgumentException(ErrorMessages.VARIABLE_NOT_DECLARED + name);
    }
}
