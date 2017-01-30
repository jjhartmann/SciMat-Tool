/*
 * MoveWordsToNewWordGroupEdit.java
 *
 * Created on 24-may-2011, 17:54:23
 */
package scimat.gui.commands.edit.move;

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
public class MoveWordsToNewWordGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<Word> wordsToMove;
  private String groupName;
  private WordGroup wordGroup;
  private boolean groupNew; // true if the group has to be created.
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param wordsToMove 
   */
  public MoveWordsToNewWordGroupEdit(ArrayList<Word> wordsToMove, String groupName) {
    this.wordsToMove = wordsToMove;
    this.groupName = groupName;
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
    
    boolean successful = true;
    int i;
    Integer wordGroupID;
    Word word;
    WordGroupDAO wordGroupDAO;
    WordDAO wordDAO;
    
    try {
      
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();
      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      
      wordGroup = wordGroupDAO.getWordGroup(this.groupName);

      if (wordGroup == null) {

        this.groupNew = true;

        wordGroupID = wordGroupDAO.addWordGroup(this.groupName, false, true);

        wordGroup = wordGroupDAO.getWordGroup(wordGroupID);

      } else {

        this.groupNew = false;
      }
      
      for (i = 0; i < this.wordsToMove.size(); i++) {
      
        word = this.wordsToMove.get(i);
        
        successful = wordDAO.setWordGroup(word.getWordID(), wordGroup.getWordGroupID(), true);
      }
      
      // To update the wordgroup (documents count...)
      wordGroup = wordGroupDAO.getWordGroup(wordGroup.getWordGroupID());

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
    
    boolean successful = true;
    int i;
    WordGroupDAO wordGroupDAO;
    WordDAO wordDAO;
    
    try {
      
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();
      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      
      for (i = 0; i < this.wordsToMove.size(); i++) {
        
        successful = wordDAO.setWordGroup(this.wordsToMove.get(i).getWordID(), null, true);
      }
      
      if (this.groupNew) {
        
        successful = wordGroupDAO.removeWordGroup(this.wordGroup.getWordGroupID(), true);
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
    int i;
    WordGroupDAO wordGroupDAO;
    WordDAO wordDAO;
    
    try {
      
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();
      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      
      if (groupNew) {
        
        successful = wordGroupDAO.addWordGroup(this.wordGroup, true);
      }
      
      for (i = 0; i < this.wordsToMove.size(); i++) {
        
        successful = wordDAO.setWordGroup(this.wordsToMove.get(i).getWordID(), wordGroup.getWordGroupID(), true);
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
