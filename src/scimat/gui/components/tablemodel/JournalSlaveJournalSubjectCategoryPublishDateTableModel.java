/*
 * JournalSlaveJournalSubjectCategoryPublishDateTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;

/**
 *
 * @author mjcobo
 */
public class JournalSlaveJournalSubjectCategoryPublishDateTableModel extends GenericTableModel<JournalSubjectCategoryPublishDate> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public JournalSlaveJournalSubjectCategoryPublishDateTableModel() {
    super(new String[] {"Subject category ID", "Subject category name",
    "Publish date ID", "Year", "Date"});
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

      JournalSubjectCategoryPublishDate jscpd = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return jscpd.getSubjectCategory().getSubjectCategoryID();

        case 1:
          return jscpd.getSubjectCategory().getSubjectCategoryName();

        case 2:
          return jscpd.getPublishDate().getPublishDateID().toString();

        case 3:
          return jscpd.getPublishDate().getYear();

        case 4:
          return jscpd.getPublishDate().getDate();

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
