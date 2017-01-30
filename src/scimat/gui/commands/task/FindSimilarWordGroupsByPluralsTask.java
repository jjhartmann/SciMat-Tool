/*
 * FindSimilarWordGroupsByPluralsTask.java
 *
 * Created on 01-jun-2011, 18:06:26
 */
package scimat.gui.commands.task;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import scimat.api.preprocessing.deduplicate.FindSimilarItemsByPlurals;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.gui.components.joindialog.WordGroupsJoinDialog;
import scimat.model.knowledgebase.dao.WordGroupDAO;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarWordGroupsByPluralsTask implements NoUndoableTask {

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
  public FindSimilarWordGroupsByPluralsTask(JFrame receiver) {
    
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

    int i, j, opt;
    WordGroup wordGroup1, wordGroup2;
    String groupName1, groupName2;
    boolean found, cancelled;
    ArrayList<WordGroup> wordGroups;
    ArrayList<WordGroup> vWordGroupsFounded = new ArrayList<WordGroup>();
    WordGroupDAO wordGroupDAO;
    FindSimilarItemsByPlurals findSimilarItemsByPlurals = new FindSimilarItemsByPlurals();
    WordGroupsJoinDialog joinDialog = new WordGroupsJoinDialog(receiver);

    wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();

    try {

      // Obtenemos la lista con los identificadores de los keywords.
      wordGroups = wordGroupDAO.getWordGroups();

      cancelled = false;

      for (i = 0; (i < wordGroups.size()) && (!cancelled); i++) {

        System.out.println("Finding keywords by plurals: "
                + i + " of " + wordGroups.size());

        // Ponemos el cursor en modo ocupado
        CursorManager.getInstance().setWaitCursor();

        vWordGroupsFounded.clear();
        found = false;

        wordGroup1 = wordGroups.get(i);
        groupName1 = wordGroup1.getGroupName();

        for (j = i + 1; j < wordGroups.size(); j++) {

          wordGroup2 = wordGroups.get(j);
          groupName2 = wordGroup2.getGroupName();

          if (findSimilarItemsByPlurals.execute(groupName1, groupName2)) {

            if (!found) {

              vWordGroupsFounded.add(wordGroup1);
            }

            vWordGroupsFounded.add(wordGroup2);

            // Eliminamos los ID de la lista de IDs para que no puedan ser tomados
            // en cuenta en la busqueda

            found = true;
          }
        }

        if (found) {

          // Desactivamos el modo ocupado del cursor.
          CursorManager.getInstance().setNormalCursor();

          joinDialog.refreshData(vWordGroupsFounded);
          joinDialog.setVisible(true);

          cancelled = joinDialog.isCancelled();

          if (cancelled) {

            opt = JOptionPane.showConfirmDialog(receiver, "Are you sure you want "
                    + "to finish the search?", "Finding similar keywords",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (opt != JOptionPane.YES_OPTION) {

              cancelled = false;
            }

          } else {

            wordGroups.removeAll(joinDialog.getItems());

            // En el caso de que hayamos asignado a un grupo el keyword que se
            // encontraba en la posicion i-esima, decrementamos en una posicion
            // el contador i, para que avancemos una unica posicion en vez de dos.
            if (!wordGroup1.equals(wordGroups.get(i))) {

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

}
