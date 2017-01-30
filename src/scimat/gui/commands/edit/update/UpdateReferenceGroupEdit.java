/*
 * UpdateReferenceGroupEdit.java
 *
 * Created on 14-mar-2011, 17:40:14
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceGroupID;

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
  private ReferenceGroup referenceGroupOld;
  
  private ReferenceGroup referenceGroupUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param groupName
   * @param stopGroup
   */
  public UpdateReferenceGroupEdit(Integer referenceGroupID, String groupName, boolean stopGroup) {
    super();

    this.referenceGroupID = referenceGroupID;
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

      this.referenceGroupOld = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().getReferenceGroup(referenceGroupID);

      if (this.referenceGroupOld.getGroupName().equals(this.groupName)) {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().setStopGroup(referenceGroupID, stopGroup, true);

        this.referenceGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().getReferenceGroup(referenceGroupID);

      } else if (this.groupName == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().checkReferenceGroup(groupName)) {

        successful = false;
        this.errorMessage = "A reference group yet exists with this name.";

      } else {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().updateReferenceGroup(referenceGroupID, groupName, stopGroup, true);

        this.referenceGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().getReferenceGroup(referenceGroupID);

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

      if (this.referenceGroupOld.getGroupName().equals(this.referenceGroupUpdated.getGroupName())) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().setStopGroup(referenceGroupOld.getReferenceGroupID(), 
                referenceGroupOld.isStopGroup(), true);
        
      } else {

        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().updateReferenceGroup(referenceGroupOld.getReferenceGroupID(),
              referenceGroupOld.getGroupName(), referenceGroupOld.isStopGroup(), true);
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

      if (this.referenceGroupOld.getGroupName().equals(this.groupName)) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().setStopGroup(referenceGroupID, stopGroup, true);
        
      } else {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().updateReferenceGroup(referenceGroupID, groupName, stopGroup, true);
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
