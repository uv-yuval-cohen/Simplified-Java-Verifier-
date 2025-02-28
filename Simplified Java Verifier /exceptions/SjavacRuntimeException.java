package ex5.exceptions;

public abstract class SjavacRuntimeException extends RuntimeException {
    private final int returnCode;

    public SjavacRuntimeException(String message, int returnCode) {
        super(message);
        this.returnCode = returnCode;
    }

    public int getReturnCode() {
        return returnCode;
    }
}
