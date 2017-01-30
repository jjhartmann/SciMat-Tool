/*
 * GlobalSlavePanel.java
 *
 * Created on 13-mar-2011, 15:58:07
 */
package scimat.gui.components.globalslavepanel;

import javax.swing.JPanel;

/**
 * 
 * @author mjcobo
 * @param <M> the master {@link Entity}.
 */
public abstract class GlobalSlavePanel<M extends Comparable<M>> extends JPanel {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private M masterItem;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public GlobalSlavePanel() {

    this.masterItem = null;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param masterItem
   */
  public abstract void refresh(M masterItem);
  
  /**
   *
   * @return
   */
  public M getMasterItem() {

    return this.masterItem;
  }

  /**
   *
   * @param masterItem
   */
  protected void setMasterItem(M masterItem) {
    this.masterItem = masterItem;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
