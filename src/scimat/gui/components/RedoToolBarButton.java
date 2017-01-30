/*
 * RedoToolBarButton.java
 *
 * Created on 08-oct-2008
 */

package scimat.gui.components;

import javax.swing.JButton;
import scimat.gui.undostack.UndoStack;
import scimat.gui.undostack.UndoStackChangeObserver;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class RedoToolBarButton extends JButton implements UndoStackChangeObserver{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public RedoToolBarButton() {
    
    setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-redo24x24.png")));
    setFocusable(false);
    setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    
    UndoStack.addUndoStackChangeObserver(this);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param canUndo
   * @param canRedo
   */
  public void undoStackChanged(boolean canUndo, boolean canRedo) {
    setEnabled(canRedo);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
