/*
 * KnowledgeBaseStateObserverMenuItem.java
 *
 * Created on 03-feb-2009, 12:20:54
 */
package scimat.gui.components;

import javax.swing.JMenuItem;
import scimat.project.CurrentProject;
import scimat.project.observer.KnowledgeBaseStateObserver;

/**
 * Extiende la funcionalidad de JMenuItem.
 * Observa los cambios de estado en la base de conocimiento.
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class KnowledgeBaseStateObserverMenuItem extends JMenuItem
        implements KnowledgeBaseStateObserver {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public KnowledgeBaseStateObserverMenuItem() {
    super();

    init();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Cuando ocurra un cambio en el estado de la base de conocimiento, este
   * objeto sera notificado a traves de este metodo.
   *
   * @param loaded nuevo estado de la base de conocimiento.
   */
  public void knowledgeBaseStateChanged(boolean loaded) {
    setEnabled(loaded);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * Initialize the object.
   */
  private void init() {

    // Insertamos al objeto como observador del estado de la base de conocimiento.
    CurrentProject.getInstance().addKnowledgeBaseStateObserver(this);
  }
}
