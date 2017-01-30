/*
 * ExportException.java
 *
 * Created on 07-may-2011
 */
package scimat.api.export;

/**
 *
 * @author mjcobo
 */
public class ExportException extends Exception {

  /**
   * Creates a new instance of <code>ExportException</code> without detail message.
   */
  public ExportException() {
  }

  /**
   * Constructs an instance of <code>ExportException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public ExportException(String msg) {
    super(msg);
  }

  /**
   * Creates a new instance of <code>ExportException</code> without detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *        {@link #getCause()} method).  (A <tt>null</tt> value is
   *        permitted, and indicates that the cause is nonexistent or
   *        unknown.)
   */
  public ExportException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new instance of <code>ExportException</code> without detail message.
   * @param msg the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *        {@link #getCause()} method).  (A <tt>null</tt> value is
   *        permitted, and indicates that the cause is nonexistent or
   *        unknown.)
   */
  public ExportException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
