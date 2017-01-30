/*
 * FindSimilarAuthorsWithoutGroupByDistanceTask.java
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
import scimat.gui.components.movetogroup.MoveSimilarAuthorsToNewGroupDialog;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarAuthorsWithoutGroupByDistanceTask implements NoUndoableTask {

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
  public FindSimilarAuthorsWithoutGroupByDistanceTask(JFrame receiver) {
    
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
    Author author1, author2;
    boolean found, cancelled;
    ArrayList<Author> authors;
    ArrayList<Author> vAuthorsFounded = new ArrayList<Author>();
    AuthorDAO authorDAO;
    MoveSimilarAuthorsToNewGroupDialog matchDialog = new MoveSimilarAuthorsToNewGroupDialog(receiver);

    authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();

    // Pedimos al usuario la distancia minima.
    maxDistance = getMaxDistance();

    matchDialog.reset();

    try {

      // Obtenemos la lista con los identificadores de los keyauthors.
      authors = authorDAO.getAuthorsWithoutGroup();

      cancelled = false;

      for (i = 0; (i < authors.size()) && (!cancelled); i++) {

        System.out.println("Finding keyauthors by distance: "
                + i + " of " + authors.size());

        // Ponemos el cursor en modo ocupado
        CursorManager.getInstance().setWaitCursor();

        vAuthorsFounded.clear();
        found = false;

        author1 = authors.get(i);

        for (j = i + 1; j < authors.size(); j++) {

          author2 = authors.get(j);

          distance = StringUtils.getLevenshteinDistance(author1.getAuthorName(), author2.getAuthorName());

          if (distance <= maxDistance) {

            if (author1.getAuthorName().length() != author2.getAuthorName().length()) {

              if (!found) {

                vAuthorsFounded.add(author1);
              }

              vAuthorsFounded.add(author2);

              // Eliminamos los ID de la lista de IDs para que no puedan ser tomados
              // en cuenta en la busqueda

              found = true;

            } else if (author1.getAuthorName().length() > distance) {

              // Si los tamaños son iguales, la distancia de edicion tiene que
              // ser mayor que el tamaño de los keyauthors

              if (!found) {

                vAuthorsFounded.add(author1);
              }

              vAuthorsFounded.add(author2);

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
          matchDialog.refreshData(vAuthorsFounded);
          matchDialog.setVisible(true);

          cancelled = matchDialog.isCancelled();

          if (cancelled) {

            opt = JOptionPane.showConfirmDialog(receiver, "Are you sure you want "
                    + "to finish the search?", "Finding similar keyauthors",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (opt != JOptionPane.YES_OPTION) {

              cancelled = false;
            }

          } else {

            authors.removeAll(matchDialog.getItems());

            // En el caso de que hayamos asignado a un grupo el keyauthor que se
            // encontraba en la posicion i-esima, decrementamos en una posicion
            // el contador i, para que avancemos una unica posicion en vez de dos.
            if (!author1.equals(authors.get(i))) {

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
