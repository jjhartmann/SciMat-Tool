/*
 * UpdateWordGroupEdit.java
 *
 * Created on 14-mar-2011, 17:37:48
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateWordGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer wordGroupID;

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
  private WordGroup wordGroupOld;
  
  private WordGroup wordGroupUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param wordGroupID
   * @param groupName
   * @param stopGroup
   */
  public UpdateWordGroupEdit(Integer wordGroupID, String groupName, boolean stopGroup) {
    super();

    this.wordGroupID = wordGroupID;
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

      this.wordGroupOld = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().getWordGroup(this.wordGroupID);

      if (this.wordGroupOld.getGroupName().equals(this.groupName)) {

        successful = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().setStopGroup(wordGroupID, stopGroup, true);

        this.wordGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().getWordGroup(wordGroupID);

      } else if (this.groupName == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().checkWordGroup(this.groupName)) {

        successful = false;
        this.errorMessage = "A word group yet exists with this name.";

      } else {

        successful = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().updateWordGroup(wordGroupID, groupName, stopGroup, true);

        this.wordGroupUpdated = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().getWordGroup(wordGroupID);
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

      if (this.wordGroupOld.getGroupName().equals(this.wordGroupUpdated.getGroupName())) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().setStopGroup(wordGroupOld.getWordGroupID(),
                wordGroupOld.isStopGroup(), true);
        
      } else {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().updateWordGroup(wordGroupOld.getWordGroupID(),
              wordGroupOld.getGroupName(), wordGroupOld.isStopGroup(), true);
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

      if (this.wordGroupOld.getGroupName().equals(groupName)) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().setStopGroup(wordGroupID, stopGroup, true);
        
      } else {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().updateWordGroup(wordGroupID, groupName, stopGroup, true);
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
