/*
 * AddPublishDateToDocumentEdit.java
 *
 * Created on 25-may-2011, 18:30:11
 */
package scimat.gui.commands.edit.add;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddPublishDateToDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Document document;
  private PublishDate publishDate;
  private PublishDate oldPublishDate;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param document
   * @param publishDate 
   */
  public AddPublishDateToDocumentEdit(Document document, PublishDate publishDate) {
    this.document = document;
    this.publishDate = publishDate;
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
    DocumentDAO documentDAO;
    
    try {
      
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      
      this.oldPublishDate = documentDAO.getPublishDate(this.document.getDocumentID());
      
      if (this.publishDate != null) {
      
        documentDAO.setPublishDate(document.getDocumentID(), this.publishDate.getPublishDateID(), true);
        
      } else {
      
        documentDAO.setPublishDate(document.getDocumentID(), null, true);
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
    
    boolean successful = true; 
    DocumentDAO documentDAO;
    
    try {
      
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      
      if (this.oldPublishDate != null) {
      
        documentDAO.setPublishDate(document.getDocumentID(), this.oldPublishDate.getPublishDateID(), true);
        
      } else {
      
        documentDAO.setPublishDate(document.getDocumentID(), null, true);
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
    DocumentDAO documentDAO;
    
    try {
      
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      
      if (this.publishDate != null) {
      
        documentDAO.setPublishDate(document.getDocumentID(), this.publishDate.getPublishDateID(), true);
        
      } else {
      
        documentDAO.setPublishDate(document.getDocumentID(), null, true);
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
