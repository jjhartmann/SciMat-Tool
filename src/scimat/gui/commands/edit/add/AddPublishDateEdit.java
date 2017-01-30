/*
 * AddPublishDateEdit.java
 *
 * Created on 14-mar-2011, 17:38:33
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddPublishDateEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer publishDateID;

  /**
   *
   */
  private String year;

  /**
   *
   */
  private String date;

  /**
   * The elements added
   */
  private ArrayList<PublishDate> publishDatesAdded;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AddPublishDateEdit(String year, String date) {
    super();
    
    this.year = year;
    this.date = date;
    this.publishDatesAdded = new ArrayList<PublishDate>();
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

      if ((this.year == null) || (this.date == null)) {

        successful = false;
        this.errorMessage = "The year and date can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().checkPublishDate(year, date)) {

        successful = false;
        this.errorMessage = "A Publish date yet exists with this year.";

      } else {

        this.publishDateID = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().addPublishDate(year, date, true);

        if (this.publishDateID != null) {

          CurrentProject.getInstance().getKnowledgeBase().commit();

          this.publishDatesAdded.add(CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().getPublishDate(publishDateID));

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

      flag = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().removePublishDate(publishDateID, true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().addPublishDate(publishDateID, year, date, true);

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
