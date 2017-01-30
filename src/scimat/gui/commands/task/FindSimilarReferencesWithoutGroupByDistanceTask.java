/*
 * FindSimilarReferencesWithoutGroupByDistanceTask.java
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
import scimat.gui.components.movetogroup.MoveSimilarReferencesToNewGroupDialog;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarReferencesWithoutGroupByDistanceTask implements NoUndoableTask {

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
  public FindSimilarReferencesWithoutGroupByDistanceTask(JFrame receiver) {
    
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
    Reference reference1, reference2;
    boolean found, cancelled;
    ArrayList<Reference> references;
    ArrayList<Reference> vReferencesFounded = new ArrayList<Reference>();
    ReferenceDAO referenceDAO;
    MoveSimilarReferencesToNewGroupDialog matchDialog = new MoveSimilarReferencesToNewGroupDialog(receiver);

    referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

    // Pedimos al usuario la distancia minima.
    maxDistance = getMaxDistance();

    matchDialog.reset();

    try {

      // Obtenemos la lista con los identificadores de los keyreferences.
      references = referenceDAO.getReferencesWithoutGroup();

      cancelled = false;

      for (i = 0; (i < references.size()) && (!cancelled); i++) {

        System.out.println("Finding keyreferences by distance: "
                + i + " of " + references.size());

        // Ponemos el cursor en modo ocupado
        CursorManager.getInstance().setWaitCursor();

        vReferencesFounded.clear();
        found = false;

        reference1 = references.get(i);

        for (j = i + 1; j < references.size(); j++) {

          reference2 = references.get(j);

          distance = StringUtils.getLevenshteinDistance(reference1.getFullReference(), reference2.getFullReference());

          if (distance <= maxDistance) {

            if (reference1.getFullReference().length() != reference2.getFullReference().length()) {

              if (!found) {

                vReferencesFounded.add(reference1);
              }

              vReferencesFounded.add(reference2);

              // Eliminamos los ID de la lista de IDs para que no puedan ser tomados
              // en cuenta en la busqueda

              found = true;

            } else if (reference1.getFullReference().length() > distance) {

              // Si los tamaños son iguales, la distancia de edicion tiene que
              // ser mayor que el tamaño de los keyreferences

              if (!found) {

                vReferencesFounded.add(reference1);
              }

              vReferencesFounded.add(reference2);

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
          matchDialog.refreshData(vReferencesFounded);
          matchDialog.setVisible(true);

          cancelled = matchDialog.isCancelled();

          if (cancelled) {

            opt = JOptionPane.showConfirmDialog(receiver, "Are you sure you want "
                    + "to finish the search?", "Finding similar keyreferences",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (opt != JOptionPane.YES_OPTION) {

              cancelled = false;
            }

          } else {

            references.removeAll(matchDialog.getItems());

            // En el caso de que hayamos asignado a un grupo el keyreference que se
            // encontraba en la posicion i-esima, decrementamos en una posicion
            // el contador i, para que avancemos una unica posicion en vez de dos.
            if (!reference1.equals(references.get(i))) {

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
