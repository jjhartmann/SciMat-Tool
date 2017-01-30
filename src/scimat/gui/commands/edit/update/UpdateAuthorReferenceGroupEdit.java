/*
 * UpdateAuthorReferenceGroupEdit.java
 *
 * Created on 14-mar-2011, 17:39:51
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateAuthorReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer authorReferenceGroupID;

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
  private AuthorReferenceGroup authorReferenceGroupOld;
  
  private AuthorReferenceGroup authorReferenceGroupUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdateAuthorReferenceGroupEdit(Integer authorReferenceGroupID, String groupName, boolean stopGroup) {
    super();

    this.authorReferenceGroupID = authorReferenceGroupID;
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

      this.authorReferenceGroupOld = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().getAuthorReferenceGroup(authorReferenceGroupID);
      
      if (this.authorReferenceGroupOld.getGroupName().equals(this.groupName)) {
      
        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().setStopGroup(authorReferenceGroupID, stopGroup, true);
        
        this.authorReferenceGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().getAuthorReferenceGroup(authorReferenceGroupID);
        
      } else if (this.groupName == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().checkAuthorReferenceGroup(groupName)) {

        successful = false;
        this.errorMessage = "An Author reference group yet exists with this name.";

      } else {

        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().updateAuthorReferenceGroup(authorReferenceGroupID, groupName, stopGroup, true);
        
        this.authorReferenceGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().getAuthorReferenceGroup(authorReferenceGroupID);
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

      if (this.authorReferenceGroupOld.getGroupName().equals(this.authorReferenceGroupUpdated.getGroupName())) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().setStopGroup(this.authorReferenceGroupOld.getAuthorReferenceGroupID(), this.authorReferenceGroupOld.isStopGroup(), true);
        
      } else {

        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().updateAuthorReferenceGroup(this.authorReferenceGroupOld.getAuthorReferenceGroupID(),
              this.authorReferenceGroupOld.getGroupName(), this.authorReferenceGroupOld.isStopGroup(), true);
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

      if (this.authorReferenceGroupOld.getGroupName().equals(this.groupName)) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().setStopGroup(this.authorReferenceGroupID, this.stopGroup, true);
        
      } else {
       
        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().updateAuthorReferenceGroup(this.authorReferenceGroupID, this.groupName, this.stopGroup, true);
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
