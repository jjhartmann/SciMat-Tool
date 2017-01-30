/*
 * UpdateDocumentAuthorPositionEdit.java
 *
 * Created on 27-may-2011, 1:18:42
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentAuthorDAO;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateDocumentAuthorPositionEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Integer documentID;
  private Integer authorID;
  private int position;
  private DocumentAuthor oldDocumentAuthor;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param documentID
   * @param authorID
   * @param position 
   */
  public UpdateDocumentAuthorPositionEdit(Integer documentID, Integer authorID, int position) {
    
    this.documentID = documentID;
    this.authorID = authorID;
    this.position = position;
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
    DocumentAuthorDAO documentAuthorDAO;
    
    try {
      
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      
      this.oldDocumentAuthor = documentAuthorDAO.getDocumentAuthor(this.documentID, this.authorID);
      
      successful = documentAuthorDAO.setPosition(this.documentID, this.authorID, this.position, true);
      
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
    DocumentAuthorDAO documentAuthorDAO;
    
    try {

      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      
      successful = documentAuthorDAO.setPosition(this.documentID, this.authorID, this.oldDocumentAuthor.getPosition(), true);
      
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
    DocumentAuthorDAO documentAuthorDAO;
    
    try {

      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      
      successful = documentAuthorDAO.setPosition(this.documentID, this.authorID, this.position, true);
      
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
