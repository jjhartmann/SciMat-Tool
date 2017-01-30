/*
 * UpdatePublishDateEdit.java
 *
 * Created on 14-mar-2011, 17:38:33
 */
package scimat.gui.commands.edit.update;

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
public class UpdatePublishDateEdit extends KnowledgeBaseEdit {

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
  private PublishDate publishDateOld;
  
  private PublishDate publishDateUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdatePublishDateEdit(Integer publishDateID, String year, String date) {
    super();

    this.publishDateID = publishDateID;
    this.year = year;
    this.date = date;
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

        this.publishDateOld = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().getPublishDate(publishDateID);

        successful = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().updatePublishDate(publishDateID, year, date, true);
        
        this.publishDateUpdated = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().getPublishDate(publishDateID);

        if (successful) {

          CurrentProject.getInstance().getKnowledgeBase().commit();
          
          KnowledgeBaseEventsReceiver.getInstance().fireEvents();

          successful = true;

          UndoStack.addEdit(this);

        } else {

          CurrentProject.getInstance().getKnowledgeBase().rollback();
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

      flag = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().updatePublishDate(publishDateOld.getPublishDateID(),
              publishDateOld.getYear(), publishDateOld.getYear(), true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().updatePublishDate(publishDateID, year, date, true);

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
