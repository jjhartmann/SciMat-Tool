/*
 * FindSimilarAuthorReferencesWithoutGroupByDistanceTask.java
 *
 * Created on 01-jun-2011, 18:06:26
 */
package scimat.gui.commands.task;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ChooseLevenshteinDistanceDialog;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.gui.components.movetogroup.MoveSimilarAuthorReferencesToNewGroupDialog;
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarAuthorReferencesWithoutGroupByDistanceTask implements NoUndoableTask {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private JFrame receiver;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param receiver
   */
  public FindSimilarAuthorReferencesWithoutGroupByDistanceTask(JFrame receiver) {
    
    this.receiver = receiver;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Ejecuta la tarea de interaccion
   */
  @Override
  public void execute() {

    int i, j, opt, maxDistance, distance;
    AuthorReference authorReference1, authorReference2;
    boolean found, cancelled;
    ArrayList<AuthorReference> authorReferences;
    ArrayList<AuthorReference> vAuthorReferencesFounded = new ArrayList<AuthorReference>();
    AuthorReferenceDAO authorReferenceDAO;
    MoveSimilarAuthorReferencesToNewGroupDialog matchDialog = new MoveSimilarAuthorReferencesToNewGroupDialog(receiver);
    
    authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

    // Pedimos al usuario la distancia minima.
    maxDistance = getMaxDistance();

    matchDialog.reset();

    try {

      // Obtenemos la lista con los identificadores de los keyauthorReferences.
      authorReferences = authorReferenceDAO.getAuthorReferencesWithoutGroup();

      cancelled = false;

      for (i = 0; (i < authorReferences.size()) && (!cancelled); i++) {

        System.out.println("Finding keyauthorReferences by distance: "
                + i + " of " + authorReferences.size());

        // Ponemos el cursor en modo ocupado
        CursorManager.getInstance().setWaitCursor();

        vAuthorReferencesFounded.clear();
        found = false;

        authorReference1 = authorReferences.get(i);

        for (j = i + 1; j < authorReferences.size(); j++) {

          authorReference2 = authorReferences.get(j);

          distance = StringUtils.getLevenshteinDistance(authorReference1.getAuthorName(), authorReference2.getAuthorName());

          if (distance <= maxDistance) {

            if (authorReference1.getAuthorName().length() != authorReference2.getAuthorName().length()) {

              if (!found) {

                vAuthorReferencesFounded.add(authorReference1);
              }

              vAuthorReferencesFounded.add(authorReference2);

              // Eliminamos los ID de la lista de IDs para que no puedan ser tomados
              // en cuenta en la busqueda

              found = true;

            } else if (authorReference1.getAuthorName().length() > distance) {

              // Si los tamaños son iguales, la distancia de edicion tiene que
              // ser mayor que el tamaño de los keyauthorReferences

              if (!found) {

                vAuthorReferencesFounded.add(authorReference1);
              }

              vAuthorReferencesFounded.add(authorReference2);

              // Eliminamos los ID de la lista de IDs para que no puedan ser tomados
              // en cuenta en la busqueda

              found = true;
            }
          }
        }

        if (found) {

          // Desactivamos el modo ocupado del cursor.
          CursorManager.getInstance().setNormalCursor();

          matchDialog.reset();
          matchDialog.refreshData(vAuthorReferencesFounded);
          matchDialog.setVisible(true);

          cancelled = matchDialog.isCancelled();

          if (cancelled) {

            opt = JOptionPane.showConfirmDialog(receiver, "Are you sure you want "
                    + "to finish the search?", "Finding similar keyauthorReferences",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (opt != JOptionPane.YES_OPTION) {

              cancelled = false;
            }

          } else {

            authorReferences.removeAll(matchDialog.getItems());

            // En el caso de que hayamos asignado a un grupo el keyauthorReference que se
            // encontraba en la posicion i-esima, decrementamos en una posicion
            // el contador i, para que avancemos una unica posicion en vez de dos.
            if (!authorReference1.equals(authorReferences.get(i))) {

              i--;
            }
          }
        }
      }

    } catch (KnowledgeBaseException e) {
    
      ErrorDialogManager.getInstance().showException(e);
    }

    // Desactivamos el modo ocupado del cursor.
    CursorManager.getInstance().setNormalCursor();

    JOptionPane.showMessageDialog(receiver, "The search has finished", 
      "Task finish", JOptionPane.INFORMATION_MESSAGE);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  private int getMaxDistance() {

    // Pedimos al usuario que elija la minima distancia de edicion.
    ChooseLevenshteinDistanceDialog distanceDialog = new ChooseLevenshteinDistanceDialog(receiver);
    distanceDialog.setVisible(true);

    return distanceDialog.getMaxDistance();
  }
}
