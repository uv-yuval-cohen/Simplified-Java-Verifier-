package ex5.context;

import java.util.List;
import java.util.Set;

/**
 * Represents information about a method in s-Java.
 */
public class MethodInfo {

    private final String name; // Method name
    private final List<Parameter> parameters; // List of parameters (name and type)

    /**
     * Constructs a MethodInfo object.
     *
     * @param name            the name of the method.
     * @param parameters      the list of parameters (name and type).
     */
    public MethodInfo(String name, List<Parameter> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * Gets the name of the method.
     *
     * @return the method name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of parameters.
     *
     * @return the list of parameters.
     */
    public List<Parameter> getParameters() {
        return parameters;
    }


    /**
     * Gets the types of all parameters in the method.
     *
     * @return a list of parameter types.
     */
    public List<VariableType> getParameterTypes() {
        return parameters.stream()
                .map(Parameter::getType)
                .toList();
    }

    /**
     * Gets the names of all parameters in the method.
     *
     * @return a list of parameter names.
     */
    public List<String> getParameterNames() {
        return parameters.stream()
                .map(Parameter::getName)
                .toList();
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
