/*
 * UndoToolBarButton.java
 *
 * Created on 15-abr-2008
 */

package scimat.gui.components;

import javax.swing.JButton;
import scimat.gui.undostack.UndoStack;
import scimat.gui.undostack.UndoStackChangeObserver;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class UndoToolBarButton extends JButton implements UndoStackChangeObserver{
  
  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Crea una instancia de UndoToolBarButton por defecto.
   */
  public UndoToolBarButton() {
    super("");

    
    setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit-undo24x24.png")));
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
    setEnabled(canUndo);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
