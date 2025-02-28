package ex5.main;
import ex5.exceptions.SjavacException;
import ex5.exceptions.SjavacRuntimeException;
import ex5.parser.Parser;
import ex5.validator.ArgumentValidator;

import java.io.*;

import static ex5.utils.Constants.FILE_ERROR;
import static ex5.utils.Constants.SUCCESS;
import static ex5.utils.ErrorMessages.IO_ERROR;

/**
 * The ex5.main entry point for the s-Java verifier program.
 */
public class Sjavac {

    public static void main(String[] args) {
        try {

            // Validate the file path
            File file = ArgumentValidator.validateFile(args);

            // Create the ex5.parser and parse the file
            Parser parser = new Parser(file);
            parser.parse();

            // If parsing is successful, print 0 (valid code)
            System.out.println(SUCCESS);

        } catch (IOException e) {
            // Handle IO errors
            System.err.println(IO_ERROR + e.getMessage());
            System.out.println(FILE_ERROR);
        } catch (SjavacException e) {
            System.err.println(e.getMessage());
            System.out.println(e.getReturnCode());
        } catch (SjavacRuntimeException e) {
            System.err.println(e.getMessage());
            System.out.println(e.getReturnCode());
        }
    }
}