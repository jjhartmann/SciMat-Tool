/*
 * JoinWordGroupEdit.java
 *
 * Created on 18-may-2011, 13:43:52
 */
package scimat.gui.commands.edit.join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
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
 * @word mjcobo
 */
public class JoinWordGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<WordGroup> wordGroupsToMove;
  private WordGroup targetWordGroup;

  private ArrayList<ArrayList<Word>> wordsOfSources = new ArrayList<ArrayList<Word>>();

  private TreeSet<Word> wordsOfTarget = new TreeSet<Word>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param wordGroupsToMove
   * @param targetWordGroup
   */
  public JoinWordGroupEdit(ArrayList<WordGroup> wordGroupsToMove, WordGroup targetWordGroup) {

    this.wordGroupsToMove = wordGroupsToMove;
    this.targetWordGroup = targetWordGroup;
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
    int i, j;
    WordDAO wordDAO;
    WordGroupDAO wordGroupDAO;
    WordGroup wordGroup;
    ArrayList<Word> words;

    try {

      i = 0;

      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();

      // Retrieve the realations of the target item.
      this.wordsOfTarget = new TreeSet<Word>(wordGroupDAO.getWords(this.targetWordGroup.getWordGroupID()));

      while ((i < this.wordGroupsToMove.size()) && (successful)) {

        wordGroup = this.wordGroupsToMove.get(i);

        // Retrieve the relations of the source items
        words = wordGroupDAO.getWords(wordGroup.getWordGroupID());
        this.wordsOfSources.add(words);

        // Do the join
        j = 0;

        successful = wordGroupDAO.removeWordGroup(wordGroup.getWordGroupID(), true);

        while ((j < words.size()) && (successful)) {
          
          successful = wordDAO.setWordGroup(words.get(j).getWordID(), 
                  this.targetWordGroup.getWordGroupID(), true);

          j ++;
        }

        i ++;
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

      e.printStackTrace(System.err);
      
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
    TreeSet<Word> tmpWords;
    WordGroupDAO wordGroupDAO;
    WordDAO wordDAO;
    WordGroup wordGroup;
    Word word;
    Iterator<Word> itWord;

    try {

      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();
      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      

      tmpWords = new TreeSet<Word>(wordGroupDAO.getWords(this.targetWordGroup.getWordGroupID()));
      tmpWords.removeAll(this.wordsOfTarget);

      itWord = tmpWords.iterator();

      while ((itWord.hasNext()) && (successful)) {

        successful = wordDAO.setWordGroup(itWord.next().getWordID(), null, true);
      }

      i = 0;

      while ((i < this.wordGroupsToMove.size()) && (successful)) {

        wordGroup = this.wordGroupsToMove.get(i);

        successful = wordGroupDAO.addWordGroup(wordGroup, true);

        j = 0;

        while ((j < this.wordsOfSources.get(i).size()) && (successful)) {

          word = this.wordsOfSources.get(i).get(j);

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

    boolean successful = true;
    int i, j;
    WordDAO wordDAO;
    WordGroupDAO wordGroupDAO;
    WordGroup wordGroup;
    ArrayList<Word> words;

    try {

      i = 0;

      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();

      while ((i < this.wordGroupsToMove.size()) && (successful)) {

        wordGroup = this.wordGroupsToMove.get(i);

        // Retrieve the relations of the source items
        words = this.wordsOfSources.get(i) ;

        // Do the join
        j = 0;

        successful = wordGroupDAO.removeWordGroup(wordGroup.getWordGroupID(), true);

        while ((j < words.size()) && (successful)) {
          
          successful = wordDAO.setWordGroup(words.get(j).getWordID(), 
                  this.targetWordGroup.getWordGroupID(), true);

          j ++;
        }

        i ++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        UndoStack.addEdit(this);

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
