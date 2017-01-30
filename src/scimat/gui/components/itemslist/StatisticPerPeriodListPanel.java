/*
 * StatisticPerPeriodListPanel.java
 *
 * Created on 25-ene-2012, 19:05:22
 */
package scimat.gui.components.itemslist;

import java.util.ArrayList;
import scimat.gui.components.tablemodel.StatisticPerPeriodTableModel;
import scimat.model.statistic.entity.StatisticPerPeriod;

/**
 *
 * @author mjcobo
 */
public class StatisticPerPeriodListPanel extends GenericItemsListPanel<StatisticPerPeriod> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  public StatisticPerPeriodListPanel() {
    super(new StatisticPerPeriodTableModel());
  }
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  @Override
  public void refreshItems(ArrayList<StatisticPerPeriod> items) {
    super.refreshItems(items);
  }
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
    
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
