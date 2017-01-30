/*
 * DeleteReferenceSourceFromReferenceEdit.java
 *
 * Created on 25-may-2011, 18:30:11
 */
package scimat.gui.commands.edit.delete;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteReferenceSourceFromReferenceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Reference reference;
  private ReferenceSource oldReferenceSource;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public DeleteReferenceSourceFromReferenceEdit(Reference reference) {
    this.reference = reference;
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
    ReferenceDAO referenceDAO;
    
    try {
      
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      
      this.oldReferenceSource = referenceDAO.getReferenceSource(this.reference.getReferenceID());
      
      if (this.oldReferenceSource != null) {
      
        referenceDAO.setReferenceSource(reference.getReferenceID(), null, true);
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
    ReferenceDAO referenceDAO;
    
    try {
      
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      
      if (this.oldReferenceSource != null) {
      
        referenceDAO.setReferenceSource(reference.getReferenceID(), 
                this.oldReferenceSource.getReferenceSourceID(), true);
        
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
    ReferenceDAO referenceDAO;
    
    try {
      
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      
      if (this.oldReferenceSource != null) {
      
        referenceDAO.setReferenceSource(reference.getReferenceID(), null, true);
        
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
