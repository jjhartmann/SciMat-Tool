/*
 * OpenProjectMenuItem.java
 *
 * Created on 29-sep-2008
 */

package scimat.gui.components;

import javax.swing.JMenuItem;
import scimat.project.CurrentProject;
import scimat.project.observer.KnowledgeBaseStateObserver;

/**
 * Esta clase representa el elemento de menu Open.
 * Extiende la funcionalidad de JMenuItem.
 * Observa los cambios de estado en la base de conocimiento.
 * 
 * @author Manuel Jesús Cobo Martin
 */
public class OpenProjectMenuItem extends JMenuItem 
        implements KnowledgeBaseStateObserver {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Construye una nueva instacia por defecto de OpenProjectMenuItem
   */
  public OpenProjectMenuItem() {
   super("Open Project");
    
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
