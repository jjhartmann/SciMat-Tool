/*
 * LoaderException.java
 *
 * Created on 07-may-2011
 */
package scimat.api.loader;

/**
 *
 * @author mjcobo
 */
public class LoaderException extends Exception {

  /**
   * Creates a new instance of <code>LoaderException</code> without detail message.
   */
  public LoaderException() {
  }

  /**
   * Constructs an instance of <code>LoaderException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public LoaderException(String msg) {
    super(msg);
  }

  /**
   * Creates a new instance of <code>LoaderException</code> without detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *        {@link #getCause()} method).  (A <tt>null</tt> value is
   *        permitted, and indicates that the cause is nonexistent or
   *        unknown.)
   */
  public LoaderException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new instance of <code>LoaderException</code> without detail message.
   * @param msg the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *        {@link #getCause()} method).  (A <tt>null</tt> value is
   *        permitted, and indicates that the cause is nonexistent or
   *        unknown.)
   */
  public LoaderException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
