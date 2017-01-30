/*
 * PeriodsTableModel.java
 *
 * Created on 13-mar-2011, 17:19:30
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.Period;

/**
 *
 * @author mjcobo
 */
public class PeriodsTableModel extends GenericDynamicTableModel<Period> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public PeriodsTableModel() {
    super(new String[] {"ID", "Name", "Position", "Publish dates", "Documents"});
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

      Period period = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return period.getPeriodID();

        case 1:
          return period.getName();

        case 2:
          return period.getPosition();

        case 3:
          return period.getPublishDatesCount();

        case 4:
          return period.getDocumentsCount();

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
