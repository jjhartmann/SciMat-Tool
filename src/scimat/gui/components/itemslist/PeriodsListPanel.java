/*
 * PeriodsListPanel.java
 *
 * Created on 17-nov-2011, 19:38:33
 */
package scimat.gui.components.itemslist;

import java.util.ArrayList;
import scimat.gui.components.tablemodel.PeriodsTableModel;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.EntityObserver;

/**
 *
 * @author mjcobo
 */
public class PeriodsListPanel 
extends GenericDynamicItemsListPanel<Period> 
implements EntityObserver<Period> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/  

  /**
   * 
   * @param tableModel 
   */
  public PeriodsListPanel() {
    super(new PeriodsTableModel());
    
    CurrentProject.getInstance().getKbObserver().addPeriodObserver(this);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityAdded(ArrayList<Period> items) throws KnowledgeBaseException {
    addItems(items);
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityRemoved(ArrayList<Period> items) throws KnowledgeBaseException {
    removeItems(items);
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityUpdated(ArrayList<Period> items) throws KnowledgeBaseException {
    updateItems(items);
  }

  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void entityRefresh() throws KnowledgeBaseException {
    refreshItems(CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPeriods());
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
