/*
 * AddWordsToDocumentEdit.java
 *
 * Created on 25-may-2011, 18:30:11
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentWordDAO;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddWordsToDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Document document;
  private ArrayList<Word> words;
  private boolean authorKeyword = false;
  private boolean sourceKeyword = false;
  private boolean addedKeyword = true;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param document
   * @param journal 
   */
  public AddWordsToDocumentEdit(Document document, ArrayList<Word> words) {
    this.document = document;
    this.words = words;
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
      
      for (i = 0; i < this.words.size(); i++) {
      
        documentWordDAO.addDocumentWord(this.document.getDocumentID(), 
                this.words.get(i).getWordID(), 
                this.authorKeyword, 
                this.sourceKeyword, 
                this.addedKeyword, 
                true);
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
    
    try {
      
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      
      for (i = 0; i < this.words.size(); i++) {
      
        documentWordDAO.removeDocumentWord(this.document.getDocumentID(), 
                this.words.get(i).getWordID(), true);
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
      
      for (i = 0; i < this.words.size(); i++) {
      
        documentWordDAO.addDocumentWord(this.document.getDocumentID(), 
                this.words.get(i).getWordID(), 
                this.authorKeyword, 
                this.sourceKeyword, 
                this.addedKeyword,
                true);
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
