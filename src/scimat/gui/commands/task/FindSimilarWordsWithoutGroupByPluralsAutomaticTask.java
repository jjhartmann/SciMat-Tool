/*
 * FindSimilarWordsWithoutGroupByPluralsTask.java
 *
 * Created on 01-jun-2011, 18:06:26
 */
package scimat.gui.commands.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import scimat.api.preprocessing.deduplicate.FindSimilarItemsByPlurals;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.commands.edit.move.MoveWordsToNewWordGroupEdit;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarWordsWithoutGroupByPluralsAutomaticTask implements NoUndoableTask {

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
  public FindSimilarWordsWithoutGroupByPluralsAutomaticTask(JFrame receiver) {

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

    int i, j, k, maxDocumentsCount;
    Word word1, word2;
    String wordName1, wordName2, groupName;
    boolean found;
    ArrayList<Word> words;
    ArrayList<Word> vWordsFounded = new ArrayList<Word>();
    WordDAO wordDAO;
    FindSimilarItemsByPlurals findSimilarItemsByPlurals = new FindSimilarItemsByPlurals();

    wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();

    try {

      // Ponemos el cursor en modo ocupado
      CursorManager.getInstance().setWaitCursor();

      // Obtenemos la lista con los identificadores de los keywords.
      words = wordDAO.getWordsWithoutGroup();
      Collections.sort(words, new Comparator<Word>() {

        public int compare(Word o1, Word o2) {
          return o1.getWordName().compareTo(o2.getWordName());
        }
      });
      //words = new ArrayList<Word>(words.subList(0, 5000));

      for (i = 0; i < words.size() - 1; i++) {

        System.out.println("Finding keywords by plurals: "
                + i + " of " + words.size());

        vWordsFounded.clear();
        found = false;

        word1 = words.get(i);
        wordName1 = word1.getWordName();

        j = i + 1;

        do {

          word2 = words.get(j);
          wordName2 = word2.getWordName();

          if (findSimilarItemsByPlurals.execute(wordName1, wordName2)) {

            System.out.println("  " + wordName1 + " <---> " + wordName2);

            if (!found) {

              vWordsFounded.add(word1);
            }

            vWordsFounded.add(word2);
            words.remove(j);

            j--;

            found = true;
          }

          j++;
          
        } while ((j < words.size()) && (wordName1.charAt(0) == wordName2.charAt(0)));
        

        if (found) {

          maxDocumentsCount = vWordsFounded.get(0).getDocumentsCount();
          groupName = vWordsFounded.get(0).getWordName();

          for (k = 1; k < vWordsFounded.size(); k++) {


            if (vWordsFounded.get(k).getDocumentsCount() > maxDocumentsCount) {

              maxDocumentsCount = vWordsFounded.get(k).getDocumentsCount();
              groupName = vWordsFounded.get(k).getWordName();
            }
          }

          (new PerformKnowledgeBaseEditTask(new MoveWordsToNewWordGroupEdit(vWordsFounded, groupName), this.receiver.getRootPane())).execute();

          words.remove(i);

          i--;
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
