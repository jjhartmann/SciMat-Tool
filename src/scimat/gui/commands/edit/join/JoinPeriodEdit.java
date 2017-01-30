/*
 * JoinPeriodEdit.java
 *
 * Created on 11-abr-2011, 20:42:49
 */
package scimat.gui.commands.edit.join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.PeriodDAO;
import scimat.model.knowledgebase.dao.PublishDatePeriodDAO;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JoinPeriodEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Period> periodsToMove;
  private Period targetPeriod;
  
  private ArrayList<ArrayList<PublishDate>> publishDatesOfSources = new ArrayList<ArrayList<PublishDate>>();
  
  private TreeSet<PublishDate> publishDatesOfTarget = new TreeSet<PublishDate>();
  

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param periodsToMove
   * @param targetPeriod
   */
  public JoinPeriodEdit(ArrayList<Period> periodsToMove, Period targetPeriod) {

    this.periodsToMove = periodsToMove;
    this.targetPeriod = targetPeriod;
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
    int i, j;
    PeriodDAO periodDAO;
    Period period;
    PublishDatePeriodDAO publishDatePeriodDAO;
    PublishDate publishDate;
    ArrayList<PublishDate> publishDates;

    try {

      i = 0;
      periodDAO = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();

      // Retrieve the realations of the target item.
      this.publishDatesOfTarget = new TreeSet<PublishDate>(periodDAO.getPublishDates(this.targetPeriod.getPeriodID()));

      while ((i < this.periodsToMove.size()) && (successful)) {

        period = this.periodsToMove.get(i);
        
        publishDates = periodDAO.getPublishDates(period.getPeriodID());
        this.publishDatesOfSources.add(publishDates);

        // Do the join
        j = 0;

        successful = periodDAO.removePeriod(period.getPeriodID(), true);
        
        while ((j < publishDates.size()) && (successful)) {

          publishDate = publishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! publishDatePeriodDAO.checkPublishDatePeriod(publishDate.getPublishDateID(), 
                  this.targetPeriod.getPeriodID())) {

            successful = publishDatePeriodDAO.addPublishDatePeriod(this.targetPeriod.getPeriodID(), 
                    publishDate.getPublishDateID(), true);
          }

          j ++;
        }

        i ++;
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
    TreeSet<PublishDate> tmpPublishDates;
    PeriodDAO periodDAO;
    PublishDatePeriodDAO publishDatePeriodDAO;
    Period period;
    Iterator<PublishDate> itPublishDate;

    try {

      periodDAO = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();
      
      tmpPublishDates = new TreeSet<PublishDate>(periodDAO.getPublishDates(this.targetPeriod.getPeriodID()));
      tmpPublishDates.removeAll(this.publishDatesOfTarget);

      itPublishDate = tmpPublishDates.iterator();

      while ((itPublishDate.hasNext()) && (successful)) {
        
        successful = publishDatePeriodDAO.removePublishDatePeriod(this.targetPeriod.getPeriodID(),
                itPublishDate.next().getPublishDateID(), true);
      }

      i = 0;

      while ((i < this.periodsToMove.size()) && (successful)) {

        period = this.periodsToMove.get(i);

        successful = periodDAO.addPeriod(period, true);

        j = 0;

        while ((j < this.publishDatesOfSources.get(i).size()) && (successful)) {
          
          successful = publishDatePeriodDAO.addPublishDatePeriod(period.getPeriodID(),
                  this.publishDatesOfSources.get(i).get(j).getPublishDateID(), true);

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

    boolean successful = true;
    int i, j;
    PeriodDAO periodDAO;
    Period period;
    PublishDatePeriodDAO publishDatePeriodDAO;
    PublishDate publishDate;
    ArrayList<PublishDate> publishDates;

    try {

      i = 0;
      periodDAO = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();

      while ((i < this.periodsToMove.size()) && (successful)) {

        period = this.periodsToMove.get(i);
        
        publishDates = this.publishDatesOfSources.get(i);

        // Do the join
        j = 0;

        successful = periodDAO.removePeriod(period.getPeriodID(), true);
        
        while ((j < publishDates.size()) && (successful)) {

          publishDate = publishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! publishDatePeriodDAO.checkPublishDatePeriod(publishDate.getPublishDateID(), 
                  this.targetPeriod.getPeriodID())) {

            successful = publishDatePeriodDAO.addPublishDatePeriod(this.targetPeriod.getPeriodID(), 
                    publishDate.getPublishDateID(), true);
          }

          j ++;
        }

        i ++;
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
