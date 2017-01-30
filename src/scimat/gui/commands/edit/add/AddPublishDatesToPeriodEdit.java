/*
 * AddReferencesToDocumentEdit.java
 *
 * Created on 25-may-2011, 18:30:11
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.PublishDatePeriodDAO;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddPublishDatesToPeriodEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Period period;
  private ArrayList<PublishDate> publishDates;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param period
   * @param publishDates 
   */
  public AddPublishDatesToPeriodEdit(Period period, ArrayList<PublishDate> publishDates) {
    this.period = period;
    this.publishDates = publishDates;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {
    
    boolean successful = true;
    int i;
    PublishDatePeriodDAO publishDatePeriodDAO;
    
    try {
      
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();
      
      for (i = 0; i < this.publishDates.size(); i++) {
      
        publishDatePeriodDAO.addPublishDatePeriod(this.period.getPeriodID(), 
                this.publishDates.get(i).getPublishDateID(), true);
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
    
    boolean successful = true;
    int i;
    PublishDatePeriodDAO publishDatePeriodDAO;
    
    try {
      
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();
      
      for (i = 0; i < this.publishDates.size(); i++) {
      
        publishDatePeriodDAO.removePublishDatePeriod(this.period.getPeriodID(), 
                this.publishDates.get(i).getPublishDateID(), true);
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
    int i;
    PublishDatePeriodDAO publishDatePeriodDAO;
    
    try {
      
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();
      
      for (i = 0; i < this.publishDates.size(); i++) {
      
        publishDatePeriodDAO.addPublishDatePeriod(this.period.getPeriodID(), 
                this.publishDates.get(i).getPublishDateID(), true);
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
