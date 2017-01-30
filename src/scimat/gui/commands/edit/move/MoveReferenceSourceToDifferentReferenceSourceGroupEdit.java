
/*
 * MoveReferenceSourceToDifferentReferenceSourceGroupEdit.java
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
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class MoveReferenceSourceToDifferentReferenceSourceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<ReferenceSource> referenceSourcesToMove;
  private ReferenceSourceGroup[] referenceSourceGroups;
  private ArrayList<ReferenceSourceGroup> referenceSourceGroupsAdded;
  private boolean[] groupNew; // true if the group i has to be created.
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public MoveReferenceSourceToDifferentReferenceSourceGroupEdit(ArrayList<ReferenceSource> referenceSourcesToMove) {
    this.referenceSourcesToMove = referenceSourcesToMove;
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
    ReferenceSourceGroup referenceSourceGroup;
    ReferenceSource referenceSource;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceDAO referenceSourceDAO;
    
    try {
      
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      this.referenceSourceGroupsAdded = new ArrayList<ReferenceSourceGroup>();
      
      this.referenceSourceGroups = new ReferenceSourceGroup[this.referenceSourcesToMove.size()];
      this.groupNew = new boolean[this.referenceSourcesToMove.size()];
      
      for (i = 0; i < this.referenceSourcesToMove.size(); i++) {
      
        referenceSource = this.referenceSourcesToMove.get(i);
        
        referenceSourceGroup = referenceSourceGroupDAO.getReferenceSourceGroup(referenceSource.getSource());
        
        if (referenceSourceGroup == null) {
        
          this.groupNew[i] = true;
          
          referenceSourceGroupID = referenceSourceGroupDAO.addReferenceSourceGroup(referenceSource.getSource(), false, true);
          
          referenceSourceGroup = referenceSourceGroupDAO.getReferenceSourceGroup(referenceSourceGroupID);
          
          this.referenceSourceGroupsAdded.add(referenceSourceGroup);
          
        } else {
        
          this.groupNew[i] = false;
        }
        
        this.referenceSourceGroups[i] = referenceSourceGroup;
        
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
        
        if (this.groupNew[i]) {
        
          successful = referenceSourceGroupDAO.removeReferenceSourceGroup(this.referenceSourceGroups[i].getReferenceSourceGroupID(), true);
        }
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
      
      for (i = 0; i < this.referenceSourcesToMove.size(); i++) {
        
        if (groupNew[i]) {
        
          successful = referenceSourceGroupDAO.addReferenceSourceGroup(this.referenceSourceGroups[i], true);
        }
        
        successful = referenceSourceDAO.setReferenceSourceGroup(this.referenceSourcesToMove.get(i).getReferenceSourceID(), 
                referenceSourceGroups[i].getReferenceSourceGroupID(), true);
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
