/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimat.api.utils.property;

/**
 *
 * @author mjcobo
 */
public class PropertyRequieredException extends Exception {

  /**
   * Creates a new instance of <code>PropertyRequieredException</code> without detail message.
   */
  public PropertyRequieredException() {
  }

  /**
   * Constructs an instance of <code>PropertyRequieredException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public PropertyRequieredException(String msg) {
    super(msg);
  }
}
