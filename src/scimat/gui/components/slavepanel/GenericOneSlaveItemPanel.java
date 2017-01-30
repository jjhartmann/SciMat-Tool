/*
 * GenericOneSlaveItemPanel.java
 *
 * Created on 26-may-2011, 20:29:07
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import javax.swing.JPanel;
import scimat.gui.components.observer.SlaveItemObserver;

/**
 *
 * @author mjcobo
 */
public class GenericOneSlaveItemPanel extends JPanel {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private ArrayList<SlaveItemObserver> slaveItemObservers;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public GenericOneSlaveItemPanel() {
    
    this.slaveItemObservers = new ArrayList<SlaveItemObserver>();
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   */
  public void addSlaveItemObserver(SlaveItemObserver o) {
  
    this.slaveItemObservers.add(o);
  }
  
  /**
   * 
   */
  public void fireSlaveItemObserver(boolean inNoNull) {
  
    int i;
    
    for (i = 0; i < this.slaveItemObservers.size(); i++) {
      
      this.slaveItemObservers.get(i).slaveItemChanged(inNoNull);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
