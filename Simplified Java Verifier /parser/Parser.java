package ex5.parser;

import ex5.context.Context;
import ex5.exceptions.*;
import ex5.exceptions.SjavacException;
import ex5.validator.UnAssignedVariableException;
import ex5.validator.VariableNotDefinedException;
import ex5.validator.IncompatibleTypeException;
import ex5.validator.ConditionSyntaxException;
import ex5.validator.*;

import java.io.*;

import static ex5.utils.ErrorMessages.*;
import static ex5.utils.RegexPatterns.*;

/**
 * The Parser class is responsible for parsing an s-Java file.
 * It performs two passes over the file to validate syntax and semantics,
 * ensuring proper handling of variables, methods, and control blocks.
 */
public class Parser {

    private final Context context;
    private final File file;
    private int lineNumber;
    private boolean isReturn;
    private boolean isFirstPass =true;
    private int scopeTrack=0;


    /**
     * Constructs a new Parser for the given file path.
     *
     * @param file the s-Java file.
     * @throws InvalidFileException if the file is invalid.
     */
    public Parser(File file) throws InvalidFileException {
        this.file = file; // a Valid file (after argument check)
        this.context = new Context();
        this.isFirstPass = false;

    }


    /**
     * Parses the given s-Java file.
     *
     * @throws IOException if an error occurs during file reading.
     */
    public void parse() throws IOException, SjavacException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // First pass
            isFirstPass = true; // Set methodRun to true for the first pass
            processFile(reader);

            // Second pass
            lineNumber = 0;
            isFirstPass = false; // Set methodRun to false for the second pass
            // Reopen the file for the second pass
            try (BufferedReader secondReader = new BufferedReader(new FileReader(file))) {
                processFile(secondReader);
            }
        }
    }



    private void processFile(BufferedReader reader) throws IOException, SjavacException {
        String line;


        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (isWhiteSpace(line)){
                continue;
            }
            line = line.trim(); // Trim leading/trailing whitespace

            if(isFirstPass){
                processFirstPass(line);
                continue;
            }
            //second pass -
            // Identify the type of the line
            LineType lineType = identifyLineType(line);

            // Second pass logic
            switch (lineType) {
                case WHITE_SPACE:
                    handleIllegalWhiteSpace();
                    break;
                case VARIABLE_DECLARATION:
                    if (!context.isInGlobalScope()) {
                        handleVariableDeclaration(line);
                    }
                    break;
                case METHOD_DECLARATION:
                    handleMethodDeclaration(line, isFirstPass);
                    break;
                case IF_WHILE_BLOCK_START:
                    handleIfWhileBlockStart(line);
                    break;
                case BLOCK_END:
                    if ( !isReturn && context.isInMethodScope()) {
                        throw new UnreturnedMethodException(UNRETURNED_METHOD, lineNumber);
                    }
                    handleBlockEnd(line);
                    break;
                case METHOD_CALL:
                    handleMethodCall(line);
                    break;
                case VARIABLE_ASSIGNMENT:
                    if (!context.isInGlobalScope()) {
                        handleVariableAssignment(line);
                    }
                    break;
                case RETURN_STATEMENT:
                    handleReturnStatement(line);
                    break;
                default:
                    throw new UnknownLineTypeException(UNKNOWN_LINE_TYPE + lineNumber);
            }
            isReturn = lineType == LineType.RETURN_STATEMENT;
        }
        if (scopeTrack!=0 || !context.isInGlobalScope()) {
            throw new OpenScopeException(OPEN_SCOPE ,++lineNumber);
        }

    }


    private void handleIllegalWhiteSpace() throws InvalidCommentException {
        throw new InvalidCommentException(INVALID_COMMENT, this.lineNumber);
    }

    private void processFirstPass(String line) throws SjavacException {
        // check if method declaration line
        if (METHOD_DECLARATION_PATTERN.matcher(line).matches()) {
            handleMethodDeclaration(line, isFirstPass);
            scopeTrack++;
        }
        if (IF_WHILE_BLOCK_PATTERN.matcher(line).matches()) {
            scopeTrack++;
        }
        if (BLOCK_END_PATTERN.matcher(line).matches()) {
            scopeTrack--;
        }
        if (VARIABLE_DECLARATION_PATTERN.matcher(line).matches()
            && scopeTrack==0) {
            handleVariableDeclaration(line);
        }
        if (VARIABLE_ASSIGNMENT_PATTERN.matcher(line).matches()
        && scopeTrack==0) {
            handleVariableAssignment(line);
        }

    }


    /**
     * Identifies the type of the given line.
     *
     * @param line the line to analyze.
     * @return the LineType corresponding to the given line.
     */
    private LineType identifyLineType(String line) throws SjavacException {
        if (line == null || line.isEmpty() || line.startsWith("//")) {
            return LineType.WHITE_SPACE; // Skip empty or null lines
        }
        if (METHOD_DECLARATION_PATTERN.matcher(line).matches()) {
            return LineType.METHOD_DECLARATION;
        }
        if (VARIABLE_DECLARATION_PATTERN.matcher(line).matches()) {
            return LineType.VARIABLE_DECLARATION;
        }
        if (IF_WHILE_BLOCK_PATTERN.matcher(line).matches()) {
            return LineType.IF_WHILE_BLOCK_START;
        }
        if (METHOD_CALL_PATTERN.matcher(line).matches()) {
            return LineType.METHOD_CALL;
        }
        if (BLOCK_END_PATTERN.matcher(line).matches()) {
            return LineType.BLOCK_END;
        }
        if (VARIABLE_ASSIGNMENT_PATTERN.matcher(line).matches()) {
            return LineType.VARIABLE_ASSIGNMENT;
        }
        if (RETURN_STATEMENT_PATTERN.matcher(line).matches()) {
            return LineType.RETURN_STATEMENT;
        }
        return LineType.UNKNOWN;
    }

    private void handleVariableDeclaration(String line) throws SjavacException{
        boolean isValid = VariableDeclarationAssignmentValidator.validateDeclaration(line, context,
                lineNumber, isFirstPass);

    }
    private void handleMethodDeclaration(String line, boolean isMethodRun) throws SjavacException {
        boolean isValid = MethodDeclarationValidator.validate(line, context, lineNumber, isMethodRun);

    }
    private void handleMethodCall(String line) throws SjavacException {
        boolean isValid = MethodCallValidator.validate(line, context, lineNumber);

    }

    private void handleBlockEnd(String line) {
        //doesnt need validator, can do only -
        context.popScope();
    }
    private void handleVariableAssignment(String line) throws SjavacException {
        boolean isValid = VariableDeclarationAssignmentValidator.validateAssignment(line, context,
                lineNumber);

    }
    private void handleReturnStatement(String line) throws InvalidReturnPositionException {
        boolean isValid = ReturnStatementValidator.validate(line, context, lineNumber);

    }
    private void handleIfWhileBlockStart(String line) throws UnAssignedVariableException,
            VariableNotDefinedException, ConditionSyntaxException, IncompatibleTypeException {
        boolean isValid = IfWhileBlockValidator.validate(line, context, lineNumber);

    }

    private boolean isWhiteSpace(String line) {
        return line == null || EMPTY_LINE_PATTERN.matcher(line).matches() || line.startsWith("//");
    }

}


