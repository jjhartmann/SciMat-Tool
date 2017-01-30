/*
 * MoveReferencesToNewReferenceGroupEdit.java
 *
 * Created on 24-may-2011, 17:54:23
 */
package scimat.gui.commands.edit.move;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceGroupDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class MoveReferencesToNewReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<Reference> referencesToMove;
  private String groupName;
  private ReferenceGroup referenceGroup;
  private boolean groupNew; // true if the group has to be created.
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param referencesToMove 
   */
  public MoveReferencesToNewReferenceGroupEdit(ArrayList<Reference> referencesToMove, String groupName) {
    this.referencesToMove = referencesToMove;
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
    Integer referenceGroupID;
    Reference reference;
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceDAO referenceDAO;
    
    try {
      
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      
      referenceGroup = referenceGroupDAO.getReferenceGroup(this.groupName);
      
      if (referenceGroup == null) {

        this.groupNew = true;

        referenceGroupID = referenceGroupDAO.addReferenceGroup(this.groupName, false, true);

        referenceGroup = referenceGroupDAO.getReferenceGroup(referenceGroupID);

      } else {

        this.groupNew = false;
      }
      
      for (i = 0; i < this.referencesToMove.size(); i++) {
      
        reference = this.referencesToMove.get(i);
        
        successful = referenceDAO.setReferenceGroup(reference.getReferenceID(), referenceGroup.getReferenceGroupID(), true);
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
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceDAO referenceDAO;
    
    try {
      
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      
      for (i = 0; i < this.referencesToMove.size(); i++) {
        
        successful = referenceDAO.setReferenceGroup(this.referencesToMove.get(i).getReferenceID(), null, true);
        
      }
      
      if (this.groupNew) {

        successful = referenceGroupDAO.removeReferenceGroup(this.referenceGroup.getReferenceGroupID(), true);
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
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceDAO referenceDAO;
    
    try {
      
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      
      if (this.groupNew) {
        
        successful = referenceGroupDAO.addReferenceGroup(this.referenceGroup, true);
      }
      
      for (i = 0; i < this.referencesToMove.size(); i++) {
        
        successful = referenceDAO.setReferenceGroup(this.referencesToMove.get(i).getReferenceID(), 
                referenceGroup.getReferenceGroupID(), true);
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
