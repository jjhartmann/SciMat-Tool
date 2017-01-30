/*
 * ClusterTableModel.java
 *
 * Created on 05-abr-2011, 13:27:43
 */
package scimat.gui.components.tablemodel;

import scimat.analysis.PropertyDocumentSetItem;

/**
 *
 * @author mjcobo
 */
public class ClusterPropertiesTableModel extends GenericTableModel<PropertyDocumentSetItem> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public ClusterPropertiesTableModel() {
    super(new String[] {"Mapper","Property", "Value"});
    
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

        case 2:

          return getItem(rowIndex).getValue();

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
