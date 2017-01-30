/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimat.api.utils.property;

/**
 *
 * @author mjcobo
 */
public class DuplicatePropertyException extends Exception {

  /**
   * Creates a new instance of <code>DuplicatePropertyException</code> without detail message.
   */
  public DuplicatePropertyException() {
  }

  /**
   * Constructs an instance of <code>DuplicatePropertyException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public DuplicatePropertyException(String msg) {
    super(msg);
  }
}
