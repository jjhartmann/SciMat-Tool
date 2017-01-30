/*
 * NewProjectToolBarButton.java
 *
 * Created on 08-oct-2008
 */

package scimat.gui.components;

import javax.swing.JButton;
import scimat.project.CurrentProject;
import scimat.project.observer.KnowledgeBaseStateObserver;

/**
 * Esta clase representa al boton de la barra de herramientas encargado de crear
 * un nuevo projecto.
 * 
 * Extiende la funcionalidad de JButton.
 * 
 * Observa los cambios de estado en la base de conocimiento.
 * 
 * @author Manuel Jesus Cobo Martin.
 */
public class NewProjectToolBarButton extends JButton implements KnowledgeBaseStateObserver{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public NewProjectToolBarButton() {
    super("");
    
    setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/newProject24x24.png")));
    setFocusable(false);
    setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    
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
