/*
 * UpdateAuthorGroupEdit.java
 *
 * Created on 14-mar-2011, 17:42:43
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateAuthorGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer authorGroupID;

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
  private AuthorGroup authorGroupOld;
  private AuthorGroup authorGroupUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdateAuthorGroupEdit(Integer authorGroupID, String groupName, boolean stopGroup) {
    super();

    this.authorGroupID = authorGroupID;
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

      this.authorGroupOld = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().getAuthorGroup(authorGroupID);
      
      if (this.authorGroupOld.getGroupName().equals(this.groupName)) {
      
        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().setStopGroup(authorGroupID, stopGroup, true);
        
        this.authorGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().getAuthorGroup(authorGroupID);
        
      } else if (this.groupName == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().checkAuthorGroup(groupName)) {

        successful = false;
        this.errorMessage = "An Author group yet exists with this name.";

      } else {

        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().updateAuthorGroup(authorGroupID, groupName, stopGroup, true);
        
        this.authorGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().getAuthorGroup(authorGroupID);
        
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        successful = true;

        UndoStack.addEdit(this);

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();

        successful = false;
        this.errorMessage = "An error happened.";
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

      if (this.authorGroupOld.getGroupName().equals(this.authorGroupUpdated.getGroupName())) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().setStopGroup(authorGroupOld.getAuthorGroupID(), authorGroupOld.isStopGroup(), true);
        
      } else {

        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().updateAuthorGroup(authorGroupOld.getAuthorGroupID(),
              authorGroupOld.getGroupName(), authorGroupOld.isStopGroup(), true);
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

      if (this.authorGroupOld.getGroupName().equals(this.groupName)) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().setStopGroup(authorGroupID, stopGroup, true);
        
      } else {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().updateAuthorGroup(authorGroupID, groupName, stopGroup, true);
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
