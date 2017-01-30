/*
 * DeletePeriodEdit.java
 *
 * Created on 14-mar-2011, 17:38:24
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.PeriodDAO;
import scimat.model.knowledgebase.dao.PublishDatePeriodDAO;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeletePeriodEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<Period> periodsToDelete;
  private ArrayList<ArrayList<PublishDate>> publishDates = new ArrayList<ArrayList<PublishDate>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param periodsToDelete
   */
  public DeletePeriodEdit(ArrayList<Period> periodsToDelete) {
    super();
    
    this.periodsToDelete = periodsToDelete;
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

    boolean successful = true;
    int i;
    PeriodDAO periodDAO;
    Period period;

    try {

      i = 0;
      periodDAO = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO();

      while ((i < this.periodsToDelete.size()) && (successful)) {

        period = this.periodsToDelete.get(i);

        // Retrieve its relation
        this.publishDates.add(periodDAO.getPublishDates(period.getPeriodID()));

        successful = periodDAO.removePeriod(period.getPeriodID(), true);

        i++;
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

    int i, j;
    boolean successful = true;
    PeriodDAO periodDAO;
    PublishDatePeriodDAO publishDatePeriodDAO;
    Period period;

    try {

      periodDAO = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();

      i = 0;

      while ((i < this.periodsToDelete.size()) && (successful)) {

        period = this.periodsToDelete.get(i);

        successful = periodDAO.addPeriod(period, true);

        j = 0;

        while ((j < this.publishDates.get(i).size()) && (successful)) {

          successful = publishDatePeriodDAO.addPublishDatePeriod(period.getPeriodID(), 
                                                                   this.publishDates.get(i).get(j).getPublishDateID(), true);

          j++;
        }

        i++;
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

    int i;
    boolean successful = true;
    PeriodDAO periodDAO;
    Period period;

    try {

      i = 0;
      periodDAO = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO();

      while ((i < this.periodsToDelete.size()) && (successful)) {

        period = this.periodsToDelete.get(i);

        successful = periodDAO.removePeriod(period.getPeriodID(), true);

        i++;
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
