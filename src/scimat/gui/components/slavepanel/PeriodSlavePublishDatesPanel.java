/*
 * PublishDateSlaveDocumentsPanel.java
 *
 * Created on 21-mar-2011, 19:19:37
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.tablemodel.PublishDatesTableModel;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.EntityObserver;
import scimat.project.observer.PeriodRelationPublishDateObserver;

/**
 *
 * @author mjcobo
 */
public class PeriodSlavePublishDatesPanel 
        extends GenericSlaveListPanel<Period, PublishDate>
        implements PeriodRelationPublishDateObserver, EntityObserver<PublishDate>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public PeriodSlavePublishDatesPanel() {
    super(new PublishDatesTableModel());

    CurrentProject.getInstance().getKbObserver().addPeriodsRelationPublishDatesObservers(this);
    CurrentProject.getInstance().getKbObserver().addPublishDateObserver(this);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void setMasterItem(Period period) {

    this.masterItem = period;

    try {

      if (this.masterItem != null) {

        relationChanged();

      } else {

        this.refreshData(new ArrayList<PublishDate>());

      }

    } catch (KnowledgeBaseException e) {
    
      ErrorDialogManager.getInstance().showException(e);

    }
  }

  /**
   *
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityAdded(ArrayList<PublishDate> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   *
   * @param entity
   * @throws KnowledgeBaseException
   */
  public void entityRefresh() throws KnowledgeBaseException {

    relationChanged();
  }

  /**
   *
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityRemoved(ArrayList<PublishDate> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityUpdated(ArrayList<PublishDate> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @throws KnowledgeBaseException
   */
  public void relationChanged() throws KnowledgeBaseException {

    if (this.masterItem != null) {
      
      refreshData(CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPublishDates(this.masterItem.getPeriodID()));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
