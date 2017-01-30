/*
 * IncorrectDataObserver.java
 *
 * Created on 03-oct-2008
 */

package scimat.gui.components;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public interface IncorrectDataObserver {
  
  /**
   * 
   * @param correct
   * @param message
   */
  public void incorrectData(boolean correct, String message);
}
