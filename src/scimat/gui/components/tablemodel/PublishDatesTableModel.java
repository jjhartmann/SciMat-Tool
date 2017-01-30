/*
 * PublishDatesTableModel.java
 *
 * Created on 13-mar-2011, 17:19:37
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.PublishDate;

/**
 *
 * @author mjcobo
 */
public class PublishDatesTableModel extends GenericDynamicTableModel<PublishDate> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public PublishDatesTableModel() {
    super(new String[] {"ID", "Year", "Date", "Documents"});
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

      PublishDate publishDate = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return publishDate.getPublishDateID();

        case 1:
          return publishDate.getYear();

        case 2:
          return publishDate.getDate() != null ? publishDate.getDate() : "";

        case 3:
          return publishDate.getDocumentsCount();

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
