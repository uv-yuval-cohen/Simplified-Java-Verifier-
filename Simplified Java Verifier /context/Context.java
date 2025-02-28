package ex5.context;

import ex5.utils.ErrorMessages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static ex5.utils.ErrorMessages.METHOD_NOT_DECLARED;

/**
 * The Context class represents the current state of the code including all the scopes and the variables in
 * each scope and it's state.
 */
public class Context {

    private final Map<String, MethodInfo> methods; // Method names -> Signatures
    private final Stack<Scope> scopeStack; // Stack of active scopes

    /**
     * Initializes the Context with an empty set of global variables, methods, and a global scope.
     */
    public Context() {
        this.methods = new HashMap<>(); // Method names -> Signature
        this.scopeStack = new Stack<>(); // Stack of active scopes
        this.scopeStack.push(new Scope(null)); // Start with global scope
    }


    /**
     * Adds a method to the context.
     *
     * @param name        The name of the method.
     * @param methodInfo  The method's signature and details.
     * return statement in it.
     */
    public void addMethod(String name, MethodInfo methodInfo) {

        methods.put(name, methodInfo);
    }
    /**
     * Pushes a new scope for a method and initializes it with the given parameters.
     *
     * @param parameters  The list of parameters to add to the method scope.
     * @param lineNumber  The line number where the method scope is created.
     * @throws VariableAlreadyExistsException If a parameter conflicts with an existing variable.
     */
    public void pushMethodScope(List<Parameter> parameters, int lineNumber)
            throws VariableAlreadyExistsException {
        // Push a new scope for the method
        pushScope();

        // Add parameters as variables in the new scope
        for (Parameter parameter : parameters) {
            VariableInfo variableInfo = new VariableInfo(parameter.isFinal(), parameter.getType(),
                    false);
            addVariable(parameter.getName(), variableInfo, true, lineNumber);
        }
    }


    /**
     * Pushes a new scope onto the stack.
     */
    public void pushScope() {
        scopeStack.push(new Scope(scopeStack.peek())); // New scope with current scope as parent
    }

    /**
     * Pops the current scope from the stack.
     *
     * @throws IllegalStateException If attempting to pop the global scope.
     */
    public void popScope() {
        if (scopeStack.size() > 1) { // Ensure we never pop the global scope
            scopeStack.pop();
        } else {
            throw new IllegalStateException(ErrorMessages.CANNOT_POP_GLOBAL_SCOPE);
        }
    }

    /**
     * Adds a variable to the current scope.
     *
     * @param name          The name of the variable.
     * @param info          The variable's information (type, final, etc.).
     * @param isInitialized Whether the variable is initialized.
     * @param lineNumber    The line number where the variable is declared.
     * @throws VariableAlreadyExistsException If the variable already exists in the scope.
     */
    public void addVariable(String name, VariableInfo info, boolean isInitialized, int lineNumber) throws VariableAlreadyExistsException
    {
        if (isInitialized) {
            scopeStack.peek().addInitializedVariable(name, info, lineNumber);
        } else {
            scopeStack.peek().addUninitializedVariable(name, info);
        }
    }
    /**
     * Adds a variable specifically to the current method scope.
     *
     * @param name       The name of the variable.
     * @param info       The variable's information (type, final, etc.).
     * @param lineNumber The line number where the variable is declared.
     * @throws VariableAlreadyExistsException If the variable already exists in the scope.
     */
    public void addVariableToMethodScope(String name, VariableInfo info, int lineNumber) throws VariableAlreadyExistsException
            {
        scopeStack.peek().addVariableToMethodScope(name, info, lineNumber);
    }
    /**
     * Retrieves information about a variable by its name.
     *
     * @param name The name of the variable.
     * @return The variable's information.
     */
    public VariableInfo getVariableInfo(String name) {
        return scopeStack.peek().getVariableInfo(name);
    }

    /**
     * Checks if a variable is global.
     *
     * @param name The name of the variable.
     * @return true if the variable is global, false otherwise.
     */
    public boolean isGlobalVariable(String name) {
        return scopeStack.peek().isGlobal(name);
    }
    /**
     * Moves a variable from uninitialized to initialized in the current scope.
     *
     * @param name The name of the variable to move.
     */
    public void moveVariableToInitialized(String name) {
        scopeStack.peek().moveVariableToInitialized(name);
    }

    /**
     * Checks if a variable is declared in the current or any parent scope.
     *
     * @param name The name of the variable.
     * @return true if the variable is declared, false otherwise.
     */
    public boolean isVariableDeclared(String name) {
        return scopeStack.peek().isDeclared(name);
    }
    /**
     * Checks if a variable is declared in the current scope.
     *
     * @param name The name of the variable.
     * @return true if the variable is declared in the current scope, false otherwise.
     */
    public boolean isVariableDeclaredOnThisScope(String name) {
        return scopeStack.peek().isVariableDeclaredOnThisScope(name);
    }

    /**
     * Retrieves the type of a variable.
     *
     * @param name The name of the variable.
     * @return The variable's type.
     * @throws IllegalArgumentException If the variable does not exist.
     */
    public VariableType getVariableType(String name) throws IllegalArgumentException {
        return scopeStack.peek().getType(name);
    }
    /**
     * Checks if a variable is marked as final.
     *
     * @param name The name of the variable.
     * @return true if the variable is final, false otherwise.
     */
    public boolean getVariableFinal(String name){
        return scopeStack.peek().getIsFinal(name);
    }


    /**
     * Checks if a variable is uninitialized in the current scope.
     *
     * @param name The name of the variable.
     * @return true if the variable is uninitialized, false otherwise.
     */
    public boolean isVariableUninitialized(String name) {
        return scopeStack.peek().isVariableUninitialized(name);
    }
    /**
     * Checks if the current scope is the global scope.
     *
     * @return true if in the global scope, false otherwise.
     */
    public boolean isInGlobalScope() {
        return scopeStack.size() == 1;
    }

    /**
     * Checks if the current scope is a method scope.
     *
     * @return true if in a method scope, false otherwise.
     */
    public boolean isInMethodScope() {
        return scopeStack.size() == 2;
    }



    public MethodInfo getMethod(String methodName, int lineNumber) throws MethodCallException {
        MethodInfo methodInfo = this.methods.get(methodName);
        if (methodInfo == null) {
            throw new MethodCallException(lineNumber, methodName + METHOD_NOT_DECLARED);
        }
        return methodInfo;
    }

}
