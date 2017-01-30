/*
 * ReportBuilderException.java
 *
 * Created on 03-jun-2011, 01:22:44
 */
package scimat.api.report;

/**
 *
 * @author mjcobo
 */
public class ReportBuilderException extends Exception {

  /**
   * Creates a new instance of <code>ReportBuilderException</code> without
   * detail message.
   */
  public ReportBuilderException() {

  }

  /**
   * Constructs an instance of <code>ReportBuilderException</code> with the
   * specified detail message.
   * 
   * @param message the detail message.
   */
  public ReportBuilderException(String message) {
    super(message);
  }

  /**
   * Constructs an instance of <code>ReportBuilderException</code> with the
   * specified detail cause.
   *
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>ReportBuilderException.getCause()</code> method).
   *              (A null value is permitted, and indicates that the cause is
   *              nonexistent or unknown.)
   */
  public ReportBuilderException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs an instance of <code>ReportBuilderException</code> with the
   * specified detail cause.
   *
   * @param message the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>ReportBuilderException.getCause()</code> method). (A
   *              null value is permitted, and indicates that the cause is
   *              nonexistent or unknown.)
   */
  public ReportBuilderException(String message, Throwable cause) {
    super(message, cause);
  }
}
