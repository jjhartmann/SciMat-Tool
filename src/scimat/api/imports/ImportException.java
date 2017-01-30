/*
 * ImportException.java
 *
 * Created on 07-may-2011
 */
package scimat.api.imports;

/**
 *
 * @author mjcobo
 */
public class ImportException extends Exception {

  /**
   * Creates a new instance of <code>ImportException</code> without detail message.
   */
  public ImportException() {
  }

  /**
   * Constructs an instance of <code>ImportException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public ImportException(String msg) {
    super(msg);
  }

  /**
   * Creates a new instance of <code>ImportException</code> without detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *        {@link #getCause()} method).  (A <tt>null</tt> value is
   *        permitted, and indicates that the cause is nonexistent or
   *        unknown.)
   */
  public ImportException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new instance of <code>ImportException</code> without detail message.
   * @param msg the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *        {@link #getCause()} method). (A <tt>null</tt> value is
   *        permitted, and indicates that the cause is nonexistent or
   *        unknown.)
   */
  public ImportException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
