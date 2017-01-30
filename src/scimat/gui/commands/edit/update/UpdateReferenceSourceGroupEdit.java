/*
 * UpdateReferenceSourceGroupEdit.java
 *
 * Created on 14-mar-2011, 17:40:35
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateReferenceSourceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceSourceGroupID;

  /**
   *
   */
  private String groupName;

  /**
   *
   */
  private boolean stopGroup;

  /**
   * The elements added
   */
  private ReferenceSourceGroup referenceSourceGroupOld;
  
  private ReferenceSourceGroup referenceSourceGroupUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdateReferenceSourceGroupEdit(Integer referenceSourceGroupID, String groupName, boolean stopGroup) {
    super();

    this.referenceSourceGroupID = referenceSourceGroupID;
    this.groupName = groupName;
    this.stopGroup = stopGroup;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @throws KnowledgeBaseException
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {

    boolean successful = false;

    try {

      referenceSourceGroupOld = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().getReferenceSourceGroup(referenceSourceGroupID);

      if (referenceSourceGroupOld.getGroupName().equals(this.groupName)) {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().setStopGroup(referenceSourceGroupID, stopGroup, true);

        this.referenceSourceGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().getReferenceSourceGroup(referenceSourceGroupID);

      } else if (this.groupName == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().checkReferenceSourceGroup(groupName)) {

        successful = false;
        this.errorMessage = "A Reference source group yet exists with this name.";

      } else {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().updateReferenceSourceGroup(referenceSourceGroupID, groupName, stopGroup, true);

        this.referenceSourceGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().getReferenceSourceGroup(referenceSourceGroupID);

      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        successful = true;

        UndoStack.addEdit(this);

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
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

    boolean flag;

    try {

      if (this.referenceSourceGroupOld.getGroupName().equals(this.referenceSourceGroupUpdated.getGroupName())) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().setStopGroup(referenceSourceGroupOld.getReferenceSourceGroupID(),
              referenceSourceGroupOld.isStopGroup(), true);
        
      } else {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().updateReferenceSourceGroup(referenceSourceGroupOld.getReferenceSourceGroupID(),
              referenceSourceGroupOld.getGroupName(), referenceSourceGroupOld.isStopGroup(), true);
      }

      if (flag) {

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

    boolean flag;

    try {

      if (this.referenceSourceGroupOld.getGroupName().equals(groupName)) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().setStopGroup(referenceSourceGroupID, stopGroup, true);
        
      } else {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().updateReferenceSourceGroup(referenceSourceGroupID,
              groupName, stopGroup, true);
      }

      if (flag) {

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
