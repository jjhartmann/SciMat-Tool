/*
 * DeleteWordGroupEdit.java
 *
 * Created on 14-mar-2011, 17:37:48
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.dao.WordGroupDAO;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteWordGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<WordGroup> wordGroupsToDelete;
  private ArrayList<ArrayList<Word>> words = new ArrayList<ArrayList<Word>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param groupName
   * @param stopGroup
   */
  public DeleteWordGroupEdit(ArrayList<WordGroup> wordGroupsToDelete) {
    super();
    
    this.wordGroupsToDelete = wordGroupsToDelete;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @throws KnowledgeBaseException
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {

    boolean successful = true;
    int i;
    WordGroupDAO wordGroupDAO;
    WordGroup wordGroup;

    try {

      i = 0;
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();

      while ((i < this.wordGroupsToDelete.size()) && (successful)) {

        wordGroup = this.wordGroupsToDelete.get(i);

        // Retrieve its relation
        this.words.add(wordGroupDAO.getWords(wordGroup.getWordGroupID()));

        successful = wordGroupDAO.removeWordGroup(wordGroup.getWordGroupID(), true);

        i++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        UndoStack.addEdit(this);


      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();

        this.errorMessage = "An error happened";

      }


    } catch (KnowledgeBaseException e) {

      CurrentProject.getInstance().getKnowledgeBase().rollback();

      successful = false;
      this.errorMessage = "An exception happened.";

      throw e;
    }

    return successful;

  }

  /**
   *
   * @throws CannotUndoException
   */
  @Override
  public void undo() throws CannotUndoException {
    super.undo();

    int i, j;
    boolean successful = true;
    WordGroupDAO wordGroupDAO;
    WordDAO wordDAO;
    WordGroup wordGroup;
    Word word;

    try {

      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();
      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();

      i = 0;

      while ((i < this.wordGroupsToDelete.size()) && (successful)) {

        wordGroup = this.wordGroupsToDelete.get(i);

        successful = wordGroupDAO.addWordGroup(wordGroup, true);

        j = 0;

        while ((j < this.words.get(i).size()) && (successful)) {

          word = this.words.get(i).get(j);

          successful = wordDAO.setWordGroup(word.getWordID(),
                                            wordGroup.getWordGroupID(), true);

          j++;
        }

        i++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
      }

    } catch (KnowledgeBaseException e) {

      e.printStackTrace(System.err);

      try{

        CurrentProject.getInstance().getKnowledgeBase().rollback();

      } catch (KnowledgeBaseException e2) {

        e2.printStackTrace(System.err);

      }
    }
  }

  /**
   *
   * @throws CannotUndoException
   */
  @Override
  public void redo() throws CannotUndoException {
    super.redo();

    int i;
    boolean successful = true;
    WordGroupDAO wordGroupDAO;
    WordGroup wordGroup;

    try {

      i = 0;
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();

      while ((i < this.wordGroupsToDelete.size()) && (successful)) {

        wordGroup = this.wordGroupsToDelete.get(i);

        successful = wordGroupDAO.removeWordGroup(wordGroup.getWordGroupID(), true);

        i++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

        KnowledgeBaseEventsReceiver.getInstance().fireEvents();
        
        
      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
      }

    } catch (KnowledgeBaseException e) {

      e.printStackTrace(System.err);

      try{

        CurrentProject.getInstance().getKnowledgeBase().rollback();

      } catch (KnowledgeBaseException e2) {

        e2.printStackTrace(System.err);

      }
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
