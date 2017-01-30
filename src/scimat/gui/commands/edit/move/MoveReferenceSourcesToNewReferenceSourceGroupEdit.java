/*
 * MoveReferenceSourcesToNewReferenceSourceGroupEdit.java
 *
 * Created on 24-may-2011, 17:54:23
 */
package scimat.gui.commands.edit.move;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceGroupDAO;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class MoveReferenceSourcesToNewReferenceSourceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<ReferenceSource> referenceSourcesToMove;
  private String groupName;
  private ReferenceSourceGroup referenceSourceGroup;
  private boolean groupNew; // true if the group has to be created.
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param referenceSourcesToMove
   * @param groupName 
   */
  public MoveReferenceSourcesToNewReferenceSourceGroupEdit(ArrayList<ReferenceSource> referenceSourcesToMove, String groupName) {
    this.referenceSourcesToMove = referenceSourcesToMove;
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
    Integer referenceSourceGroupID;
    ReferenceSource referenceSource;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceDAO referenceSourceDAO;
    
    try {
      
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      
      referenceSourceGroup = referenceSourceGroupDAO.getReferenceSourceGroup(this.groupName);

      if (referenceSourceGroup == null) {

        this.groupNew = true;

        referenceSourceGroupID = referenceSourceGroupDAO.addReferenceSourceGroup(this.groupName, false, true);

        referenceSourceGroup = referenceSourceGroupDAO.getReferenceSourceGroup(referenceSourceGroupID);

      } else {

        this.groupNew = false;
      }
      
      for (i = 0; i < this.referenceSourcesToMove.size(); i++) {
      
        referenceSource = this.referenceSourcesToMove.get(i);
        
        successful = referenceSourceDAO.setReferenceSourceGroup(referenceSource.getReferenceSourceID(), 
                referenceSourceGroup.getReferenceSourceGroupID(), true);
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
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceDAO referenceSourceDAO;
    
    try {
      
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      
      for (i = 0; i < this.referenceSourcesToMove.size(); i++) {
        
        successful = referenceSourceDAO.setReferenceSourceGroup(this.referenceSourcesToMove.get(i).getReferenceSourceID(), null, true);
      }
      
      if (this.groupNew) {
        
        successful = referenceSourceGroupDAO.removeReferenceSourceGroup(this.referenceSourceGroup.getReferenceSourceGroupID(), true);
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
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceDAO referenceSourceDAO;
    
    try {
      
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      
      if (groupNew) {
        
        successful = referenceSourceGroupDAO.addReferenceSourceGroup(this.referenceSourceGroup, true);
      }
      
      for (i = 0; i < this.referenceSourcesToMove.size(); i++) {
        
        successful = referenceSourceDAO.setReferenceSourceGroup(this.referenceSourcesToMove.get(i).getReferenceSourceID(), 
                referenceSourceGroup.getReferenceSourceGroupID(), true);
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
