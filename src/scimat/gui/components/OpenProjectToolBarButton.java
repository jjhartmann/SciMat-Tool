/*
 * OpenProjectToolBarButton.java
 *
 * Created on 08-oct-2008
 */

package scimat.gui.components;

import javax.swing.JButton;
import scimat.project.CurrentProject;
import scimat.project.observer.KnowledgeBaseStateObserver;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class OpenProjectToolBarButton extends JButton 
        implements KnowledgeBaseStateObserver{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public OpenProjectToolBarButton() {
    super("");
    
    setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/openProject24x24.png")));
    setFocusable(false);
    setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    
    // Insertamos al objeto como observador del estado de la base de conocimiento.
    CurrentProject.getInstance().addKnowledgeBaseStateObserver(this);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

   /**
   * Cuando ocurra un cambio en el estado de la base de conocimiento, este 
   * objeto sera notificado a traves de este metodo.
   * 
   * @param loaded nuevo estado de la base de conocimiento. Sera true en caso
   *               de que esta este cargada
   */
  public void knowledgeBaseStateChanged(boolean loaded) {
    setEnabled(!loaded);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
