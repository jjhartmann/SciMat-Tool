/*
 * SlaveItemObserverButton.java
 *
 * Created on 26-may-2011, 20:39:13
 */
package scimat.gui.components;

import javax.swing.JButton;
import scimat.gui.components.observer.SlaveItemObserver;

/**
 *
 * @author mjcobo
 */
public class SlaveItemObserverButton extends JButton implements SlaveItemObserver {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public SlaveItemObserverButton() {
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   */
  public void slaveItemChanged(boolean isNotNull) {
    setEnabled(isNotNull);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
