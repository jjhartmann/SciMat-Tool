/*
 * AddAuthorsToDocumentEdit.java
 *
 * Created on 25-may-2011, 18:30:11
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentAuthorDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddAuthorsToDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Document document;
  private ArrayList<Author> authors;
  private int[] positions;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param document
   * @param journal 
   */
  public AddAuthorsToDocumentEdit(Document document, ArrayList<Author> authors) {
    this.document = document;
    this.authors = authors;
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
    DocumentAuthorDAO documentAuthorDAO;
    
    try {
      
      this.positions = new int[this.authors.size()];
      
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      
      for (i = 0; i < this.authors.size(); i++) {
        
        this.positions[i] = documentAuthorDAO.getMaxPosition(this.document.getDocumentID()) + 1;
      
        documentAuthorDAO.addDocumentAuthor(this.document.getDocumentID(), 
                this.authors.get(i).getAuthorID(), this.positions[i], true);
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
    int i;
    DocumentAuthorDAO documentAuthorDAO;
    
    try {
      
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      
      for (i = 0; i < this.authors.size(); i++) {
      
        documentAuthorDAO.removeDocumentAuthor(this.document.getDocumentID(), 
                this.authors.get(i).getAuthorID(), true);
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
    DocumentAuthorDAO documentAuthorDAO;
    
    try {
      
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      
      for (i = 0; i < this.authors.size(); i++) {
      
        documentAuthorDAO.addDocumentAuthor(this.document.getDocumentID(), 
                this.authors.get(i).getAuthorID(), this.positions[i], true);
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
