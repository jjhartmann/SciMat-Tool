
/*
 * AddWordsToDocumentEdit.java
 *
 * Created on 25-may-2011, 18:30:11
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentWordDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteWordsFromDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Document document;
  private ArrayList<DocumentWord> documentWords;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param document
   * @param documentWords 
   */
  public DeleteWordsFromDocumentEdit(Document document, ArrayList<DocumentWord> documentWords) {
    this.document = document;
    this.documentWords = documentWords;
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
    DocumentWordDAO documentWordDAO;
    
    try {
      
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      
      for (i = 0; i < this.documentWords.size(); i++) {
      
        documentWordDAO.removeDocumentWord(this.document.getDocumentID(), 
                this.documentWords.get(i).getWord().getWordID(), true);
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
    DocumentWordDAO documentWordDAO;
    DocumentWord documentWord;
    
    try {
      
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      
      for (i = 0; i < this.documentWords.size(); i++) {
        
        documentWord = this.documentWords.get(i);
      
        documentWordDAO.addDocumentWord(this.document.getDocumentID(), 
                documentWord.getWord().getWordID(), 
                documentWord.isAuthorKeyword(),
                documentWord.isSourceKeyword(),
                documentWord.isAddedKeyword(), true);
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
    DocumentWordDAO documentWordDAO;
    
    try {
      
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      
      for (i = 0; i < this.documentWords.size(); i++) {
      
        documentWordDAO.removeDocumentWord(this.document.getDocumentID(), 
                this.documentWords.get(i).getWord().getWordID(), true);
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
