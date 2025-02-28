package ex5.validator;

import ex5.context.Context;

import static ex5.utils.ErrorMessages.RETURN_IN_GLOBAL_SCOPE;

// ===========================================
// NOTE: Validators are responsible for both:
// 1. Validating the syntax.
// 2. Ensuring the context reflects valid usage if applicable.
// ===========================================

/**
 * Validator for handling return statements in s-Java files.
 */
public class ReturnStatementValidator {

    /**
     * Validates a return statement line.
     *
     * @param line    the line containing the return statement.
     * @param context the current context, used to check scope and method state.
     * @return true if the return statement is valid, false otherwise.
     */
    public static boolean validate(String line, Context context, int lineNumber)
            throws InvalidReturnPositionException {
        // Log for now to test delegation
//        System.out.println("Validating return statement: " + line);

        if (context.isInGlobalScope()){
            throw new InvalidReturnPositionException(RETURN_IN_GLOBAL_SCOPE, lineNumber);
        }

        return true;
    }
}
