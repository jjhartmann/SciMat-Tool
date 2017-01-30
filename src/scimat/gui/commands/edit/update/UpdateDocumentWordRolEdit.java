/*
 * UpdateDocumentWordRolEdit.java
 *
 * Created on 27-may-2011, 1:18:42
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentWordDAO;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateDocumentWordRolEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Integer documentID;
  private Integer wordID;
  private boolean authorKeyword;
  private boolean sourceKeyword;
  private boolean addedKeyword;
  private DocumentWord oldDocumentWord;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param documentID
   * @param wordID
   * @param authorKeyword
   * @param sourceKeyword
   * @param addedKeyword 
   */
  public UpdateDocumentWordRolEdit(Integer documentID, Integer wordID, 
          boolean authorKeyword, boolean sourceKeyword, 
          boolean addedKeyword) {
    
    this.documentID = documentID;
    this.wordID = wordID;
    this.authorKeyword = authorKeyword;
    this.sourceKeyword = sourceKeyword;
    this.addedKeyword = addedKeyword;
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
    DocumentWordDAO documentWordDAO;
    
    try {

      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      
      this.oldDocumentWord = documentWordDAO.getDocumentWord(this.documentID, this.wordID);
      
      successful = documentWordDAO.setAuthorWord(this.documentID, this.wordID, this.authorKeyword, true);
      successful = documentWordDAO.setSourceWord(this.documentID, this.wordID, this.sourceKeyword, true);
      successful = documentWordDAO.setAddedWord(this.documentID, this.wordID, this.addedKeyword, true);
      
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
    DocumentWordDAO documentWordDAO;
    
    try {

      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      
      successful = documentWordDAO.setAuthorWord(this.documentID, this.wordID, this.oldDocumentWord.isAuthorKeyword(), true);
      successful = documentWordDAO.setSourceWord(this.documentID, this.wordID, this.oldDocumentWord.isSourceKeyword(), true);
      successful = documentWordDAO.setAddedWord(this.documentID, this.wordID, this.oldDocumentWord.isAddedKeyword(), true);
      
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
    DocumentWordDAO documentWordDAO;
    
    try {

      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      
      successful = documentWordDAO.setAuthorWord(this.documentID, this.wordID, this.authorKeyword, true);
      successful = documentWordDAO.setSourceWord(this.documentID, this.wordID, this.sourceKeyword, true);
      successful = documentWordDAO.setAddedWord(this.documentID, this.wordID, this.addedKeyword, true);
      
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
