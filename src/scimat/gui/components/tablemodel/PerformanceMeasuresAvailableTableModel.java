/*
 * ClusterTableModel.java
 *
 * Created on 05-abr-2011, 13:27:43
 */
package scimat.gui.components.tablemodel;

import scimat.analysis.PerformanceMeasuresAvailable;

/**
 *
 * @author mjcobo
 */
public class PerformanceMeasuresAvailableTableModel extends GenericTableModel<PerformanceMeasuresAvailable> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public PerformanceMeasuresAvailableTableModel() {
    super(new String[] {"Mapper","Property"});
    
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

      switch (columnIndex) {

        case 0:

          return getItem(rowIndex).getMapper();

        case 1:

          return getItem(rowIndex).getPropertyKey();

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
