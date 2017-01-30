/*
 * AddAffiliationsToDocumentEdit.java
 *
 * Created on 25-may-2011, 18:30:11
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentAffiliationDAO;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddAffiliationsToDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Document document;
  private ArrayList<Affiliation> affiliations;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param document
   * @param journal 
   */
  public AddAffiliationsToDocumentEdit(Document document, ArrayList<Affiliation> affiliations) {
    this.document = document;
    this.affiliations = affiliations;
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
    DocumentAffiliationDAO documentAffiliationDAO;
    
    try {
      
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();
      
      for (i = 0; i < this.affiliations.size(); i++) {
      
        documentAffiliationDAO.addDocumentAffiliation(this.document.getDocumentID(), this.affiliations.get(i).getAffiliationID(), true);
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        CurrentProject.getInstance().getKbObserver().fireDocumentsRelationAffiliationsChanged();

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
    DocumentAffiliationDAO documentAffiliationDAO;
    
    try {
      
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();
      
      for (i = 0; i < this.affiliations.size(); i++) {
      
        documentAffiliationDAO.removeDocumentAffiliation(this.document.getDocumentID(), this.affiliations.get(i).getAffiliationID(), true);
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
    DocumentAffiliationDAO documentAffiliationDAO;
    
    try {
      
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();
      
      for (i = 0; i < this.affiliations.size(); i++) {
      
        documentAffiliationDAO.addDocumentAffiliation(this.document.getDocumentID(), this.affiliations.get(i).getAffiliationID(), true);
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
