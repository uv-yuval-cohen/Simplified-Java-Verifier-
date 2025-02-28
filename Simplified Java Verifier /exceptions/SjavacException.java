package ex5.exceptions;

public abstract class SjavacException extends Exception {
  private final int returnCode;

  public SjavacException(String message, int returnCode) {
    super(message);
    this.returnCode = returnCode;
  }

  public int getReturnCode() {
    return returnCode;
  }
}
