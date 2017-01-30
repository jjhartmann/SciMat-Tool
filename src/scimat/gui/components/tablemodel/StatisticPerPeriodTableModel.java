/*
 * StatisticPerPeriodTableModel.java
 *
 * Created on 25-ene-2012, 18:51:37
 */
package scimat.gui.components.tablemodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import scimat.model.statistic.entity.StatisticPerPeriod;

/**
 *
 * @author mjcobo
 */
public class StatisticPerPeriodTableModel extends GenericTableModel<StatisticPerPeriod> {
  
  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private final NumberFormat numberFormatter = new DecimalFormat("0.##");
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public StatisticPerPeriodTableModel() {
    super(new String[] {"Period", "Documents", "Units", "Max", "Min", "Mean", 
      "Median", "Standard desviation", "Variance"});
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {

    if ((rowIndex >= 0) && (rowIndex < getRowCount())) {
      
      StatisticPerPeriod stat = getItem(rowIndex);
      
      switch (columnIndex) {

        case 0:
          return stat.getPeriod().getName();

        case 1:
          return stat.getPeriod().getDocumentsCount();
          
        case 2:
          return stat.getUniqueGroupsCount();
          
        case 3:
          return stat.getMax();
        
        case 4:
          return stat.getMin();
          
        case 5:
          return this.numberFormatter.format(stat.getMean());
          
        case 6:
          return this.numberFormatter.format(stat.getMedian());
          
        case 7:
          return this.numberFormatter.format(stat.getStandardDesviation());
          
        case 8:
          return this.numberFormatter.format(stat.getVariance());
          
        default:
          return "";
      }

    } else {

      return "";

    }
  }
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
