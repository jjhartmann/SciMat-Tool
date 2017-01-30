/*
 * AddWordGroupEdit.java
 *
 * Created on 14-mar-2011, 17:37:48
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
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
public class AddWordGroupEdit extends KnowledgeBaseEdit {

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
  private ArrayList<WordGroup> wordGroupsAdded;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param groupName
   * @param stopGroup
   */
  public AddWordGroupEdit(String groupName, boolean stopGroup) {
    super();
    
    this.groupName = groupName;
    this.stopGroup = stopGroup;
    this.wordGroupsAdded = new ArrayList<WordGroup>();
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

      if (this.groupName == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().checkWordGroup(groupName)) {

        successful = false;
        this.errorMessage = "A word group yet exists with this name.";

      } else {

        this.wordGroupID = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().addWordGroup(groupName, stopGroup, true);

        if (this.wordGroupID != null) {

          CurrentProject.getInstance().getKnowledgeBase().commit();

          this.wordGroupsAdded.add(CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().getWordGroup(wordGroupID));

          KnowledgeBaseEventsReceiver.getInstance().fireEvents();

          successful = true;

          UndoStack.addEdit(this);

        } else {

          CurrentProject.getInstance().getKnowledgeBase().rollback();

          successful = false;
          this.errorMessage = "An error happened.";
        }
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

      flag = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().removeWordGroup(wordGroupID, true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().addWordGroup(wordGroupID, groupName, stopGroup, true);

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
