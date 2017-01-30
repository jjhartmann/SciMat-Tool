/*
 * FindSimilarReferenceSourcesWithoutGroupByDistanceTask.java
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
import scimat.gui.components.movetogroup.MoveSimilarReferenceSourcesToNewGroupDialog;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarReferenceSourcesWithoutGroupByDistanceTask implements NoUndoableTask {

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
  public FindSimilarReferenceSourcesWithoutGroupByDistanceTask(JFrame receiver) {
    
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
    ReferenceSource referenceSource1, referenceSource2;
    boolean found, cancelled;
    ArrayList<ReferenceSource> referenceSources;
    ArrayList<ReferenceSource> vReferenceSourcesFounded = new ArrayList<ReferenceSource>();
    ReferenceSourceDAO referenceSourceDAO;
    MoveSimilarReferenceSourcesToNewGroupDialog matchDialog = new MoveSimilarReferenceSourcesToNewGroupDialog(receiver);

    referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();

    // Pedimos al usuario la distancia minima.
    maxDistance = getMaxDistance();

    matchDialog.reset();

    try {

      // Obtenemos la lista con los identificadores de los keyreferenceSources.
      referenceSources = referenceSourceDAO.getReferenceSourcesWithoutGroup();

      cancelled = false;

      for (i = 0; (i < referenceSources.size()) && (!cancelled); i++) {

        System.out.println("Finding keyreferenceSources by distance: "
                + i + " of " + referenceSources.size());

        // Ponemos el cursor en modo ocupado
        CursorManager.getInstance().setWaitCursor();

        vReferenceSourcesFounded.clear();
        found = false;

        referenceSource1 = referenceSources.get(i);

        for (j = i + 1; j < referenceSources.size(); j++) {

          referenceSource2 = referenceSources.get(j);

          distance = StringUtils.getLevenshteinDistance(referenceSource1.getSource(), referenceSource2.getSource());

          if (distance <= maxDistance) {

            if (referenceSource1.getSource().length() != referenceSource2.getSource().length()) {

              if (!found) {

                vReferenceSourcesFounded.add(referenceSource1);
              }

              vReferenceSourcesFounded.add(referenceSource2);

              // Eliminamos los ID de la lista de IDs para que no puedan ser tomados
              // en cuenta en la busqueda

              found = true;

            } else if (referenceSource1.getSource().length() > distance) {

              // Si los tamaños son iguales, la distancia de edicion tiene que
              // ser mayor que el tamaño de los keyreferenceSources

              if (!found) {

                vReferenceSourcesFounded.add(referenceSource1);
              }

              vReferenceSourcesFounded.add(referenceSource2);

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
          matchDialog.refreshData(vReferenceSourcesFounded);
          matchDialog.setVisible(true);

          cancelled = matchDialog.isCancelled();

          if (cancelled) {

            opt = JOptionPane.showConfirmDialog(receiver, "Are you sure you want "
                    + "to finish the search?", "Finding similar keyreferenceSources",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (opt != JOptionPane.YES_OPTION) {

              cancelled = false;
            }

          } else {

            referenceSources.removeAll(matchDialog.getItems());

            // En el caso de que hayamos asignado a un grupo el keyreferenceSource que se
            // encontraba en la posicion i-esima, decrementamos en una posicion
            // el contador i, para que avancemos una unica posicion en vez de dos.
            if (!referenceSource1.equals(referenceSources.get(i))) {

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
