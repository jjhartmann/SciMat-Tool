/*
 * FindSimilarWordGroupsByPluralsAutomaticTask.java
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
import scimat.gui.commands.edit.join.JoinWordGroupEdit;
import scimat.gui.components.ChooseCharDelimiterDialog;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.model.knowledgebase.dao.WordGroupDAO;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class FindSimilarWordGroupsByPluralsAutomaticTask implements NoUndoableTask {

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
  public FindSimilarWordGroupsByPluralsAutomaticTask(JFrame receiver) {

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

    int i, j, k, maxDocumentsCount, targetGroupIndex;
    WordGroup wordGroup1, wordGroup2, targetWordGroup;
    String wordName1, wordName2;
    boolean found;
    ArrayList<WordGroup> wordGroups;
    ArrayList<WordGroup> vWordGroupsFounded = new ArrayList<WordGroup>();
    WordGroupDAO wordGroupDAO;


    ChooseCharDelimiterDialog dialog = new ChooseCharDelimiterDialog(this.receiver, true);

    dialog.setVisible(true);

    FindSimilarItemsByPlurals findSimilarItemsByPlurals = new FindSimilarItemsByPlurals(String.valueOf(dialog.getCharDelimiter()));

    wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();

    try {

      // Ponemos el cursor en modo ocupado
      CursorManager.getInstance().setWaitCursor();

      // Obtenemos la lista con los identificadores de los keywords.
      wordGroups = wordGroupDAO.getWordGroups();
      Collections.sort(wordGroups, new Comparator<WordGroup>() {

        public int compare(WordGroup o1, WordGroup o2) {
          return o1.getGroupName().compareTo(o2.getGroupName());
        }
      });

      for (i = 0; i < wordGroups.size() - 1; i++) {

        //System.out.println("Finding word group by plurals: " + i + " of " + wordGroups.size());

        vWordGroupsFounded.clear();
        found = false;

        wordGroup1 = wordGroups.get(i);
        wordName1 = wordGroup1.getGroupName();

        j = i + 1;

        do {

          wordGroup2 = wordGroups.get(j);
          wordName2 = wordGroup2.getGroupName();

          if (findSimilarItemsByPlurals.execute(wordName1, wordName2)) {

            //System.out.println("  " + wordName1 + " <---> " + wordName2);

            if (!found) {

              vWordGroupsFounded.add(wordGroup1);
            }

            vWordGroupsFounded.add(wordGroup2);
            wordGroups.remove(j);

            j--;

            found = true;
          }

          j++;

        } while ((j < wordGroups.size()) && (wordName1.charAt(0) == wordName2.charAt(0)));

        if (found) {

          maxDocumentsCount = vWordGroupsFounded.get(0).getDocumentsCount();
          targetWordGroup = vWordGroupsFounded.get(0);
          targetGroupIndex = 0;

          for (k = 1; k < vWordGroupsFounded.size(); k++) {
            
            if (vWordGroupsFounded.get(k).getDocumentsCount() > maxDocumentsCount) {

              maxDocumentsCount = vWordGroupsFounded.get(k).getDocumentsCount();
              targetWordGroup = vWordGroupsFounded.get(k);
              targetGroupIndex = k;
            }
          }
          
          vWordGroupsFounded.remove(targetGroupIndex);
          
          System.out.print(targetWordGroup.getGroupName());
          
          for (k = 1; k < vWordGroupsFounded.size(); k++) {
          
            System.out.println("\t" + vWordGroupsFounded.get(k).getGroupName());
          }
          
          System.out.println();

          (new PerformKnowledgeBaseEditTask(new JoinWordGroupEdit(vWordGroupsFounded, targetWordGroup), this.receiver.getRootPane())).execute();
          
          wordGroups.remove(i);

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
