/*
 * AddWordsToWordGroupEdit.java
 *
 * Created on 10-abr-2011, 18:37:43
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddWordsToWordGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Word> wordsToAdd;
  private ArrayList<WordGroup> oldWordGroupOfWords;
  private WordGroup targetWordGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AddWordsToWordGroupEdit(ArrayList<Word> wordsToAdd, WordGroup targetWordGroup) {
    this.wordsToAdd = wordsToAdd;
    this.targetWordGroup = targetWordGroup;

    this.oldWordGroupOfWords = new ArrayList<WordGroup>();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   * @throws KnowledgeBaseException
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {

    int i;
    Word word;
    boolean successful = false;

    try {

      for (i = 0; i < this.wordsToAdd.size(); i++) {

        word = this.wordsToAdd.get(i);

        this.oldWordGroupOfWords.add(CurrentProject.getInstance().getFactoryDAO().getWordDAO().getWordGroup(word.getWordID()));

        successful = CurrentProject.getInstance().getFactoryDAO().getWordDAO().setWordGroup(word.getWordID(),
                this.targetWordGroup.getWordGroupID(), true);
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

    int i;
    WordGroup wordGroup;
    boolean successful = false;

    try {

      for (i = 0; i < this.wordsToAdd.size(); i++) {

        wordGroup = this.oldWordGroupOfWords.get(i);

        if (wordGroup != null) {

          successful = CurrentProject.getInstance().getFactoryDAO().getWordDAO().setWordGroup(this.wordsToAdd.get(i).getWordID(),
                  wordGroup.getWordGroupID(), true);

        } else {

          successful = CurrentProject.getInstance().getFactoryDAO().getWordDAO().setWordGroup(this.wordsToAdd.get(i).getWordID(), null, true);
        }
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
    boolean successful = false;

    try {

      for (i = 0; i < this.wordsToAdd.size(); i++) {

        successful = CurrentProject.getInstance().getFactoryDAO().getWordDAO().setWordGroup(this.wordsToAdd.get(i).getWordID(),
                this.targetWordGroup.getWordGroupID(), true);
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
