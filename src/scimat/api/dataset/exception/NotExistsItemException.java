/*
 * NotExistsItemException.java
 *
 * Created on 05-nov-2010, 19:16:00
 */
package scimat.api.dataset.exception;

/**
 *
 * @author mjcobo
 */
public class NotExistsItemException extends RuntimeException {

  /**
   * Creates a new instance of <code>NewException</code> without detail message.
   */
  public NotExistsItemException() {
  }

  /**
   * Constructs an instance of <code>NewException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public NotExistsItemException(String msg) {
    super(msg);
  }
}
