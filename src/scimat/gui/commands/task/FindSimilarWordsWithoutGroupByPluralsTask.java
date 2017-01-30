/*
 * FindSimilarWordsWithoutGroupByPluralsTask.java
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
import scimat.gui.components.movetogroup.MoveSimilarWordsToNewGroupDialog;
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarWordsWithoutGroupByPluralsTask implements NoUndoableTask {

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
  public FindSimilarWordsWithoutGroupByPluralsTask(JFrame receiver) {
    
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
    Word word1, word2;
    String wordName1, wordName2;
    boolean found, cancelled;
    ArrayList<Word> words;
    ArrayList<Word> vWordsFounded = new ArrayList<Word>();
    WordDAO wordDAO;
    FindSimilarItemsByPlurals findSimilarItemsByPlurals = new FindSimilarItemsByPlurals();
    MoveSimilarWordsToNewGroupDialog matchDialog = new MoveSimilarWordsToNewGroupDialog(receiver);

    wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();

    try {

      // Obtenemos la lista con los identificadores de los keywords.
      words = wordDAO.getWordsWithoutGroup();

      cancelled = false;

      for (i = 0; (i < words.size()) && (!cancelled); i++) {

        System.out.println("Finding keywords by plurals: "
                + i + " of " + words.size());

        // Ponemos el cursor en modo ocupado
        CursorManager.getInstance().setWaitCursor();

        vWordsFounded.clear();
        found = false;

        word1 = words.get(i);
        wordName1 = word1.getWordName();

        for (j = i + 1; j < words.size(); j++) {

          word2 = words.get(j);
          wordName2 = word2.getWordName();

          if (findSimilarItemsByPlurals.execute(wordName1, wordName2)) {

            if (!found) {

              vWordsFounded.add(word1);
            }

            vWordsFounded.add(word2);

            // Eliminamos los ID de la lista de IDs para que no puedan ser tomados
            // en cuenta en la busqueda

            found = true;
          }
        }

        if (found) {

          // Desactivamos el modo ocupado del cursor.
          CursorManager.getInstance().setNormalCursor();

          matchDialog.refreshData(vWordsFounded);
          matchDialog.setVisible(true);

          cancelled = matchDialog.isCancelled();

          if (cancelled) {

            opt = JOptionPane.showConfirmDialog(receiver, "Are you sure you want "
                    + "to finish the search?", "Finding similar keywords",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (opt != JOptionPane.YES_OPTION) {

              cancelled = false;
            }

          } else {

            words.removeAll(matchDialog.getItems());

            // En el caso de que hayamos asignado a un grupo el keyword que se
            // encontraba en la posicion i-esima, decrementamos en una posicion
            // el contador i, para que avancemos una unica posicion en vez de dos.
            if (!word1.equals(words.get(i))) {

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
