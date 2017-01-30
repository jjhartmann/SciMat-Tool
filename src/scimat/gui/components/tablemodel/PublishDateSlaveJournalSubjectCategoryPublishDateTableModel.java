/*
 * PublishDateSlaveJournalSubjectCategoryPublishDateTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;

/**
 *
 * @author mjcobo
 */
public class PublishDateSlaveJournalSubjectCategoryPublishDateTableModel extends GenericTableModel<JournalSubjectCategoryPublishDate> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public PublishDateSlaveJournalSubjectCategoryPublishDateTableModel() {
    super(new String[] {"Journal ID", "Source", "Subject category ID", "Name"});
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
          return jscpd.getJournal().getJournalID();

        case 1:
          return jscpd.getJournal().getSource();

        case 2:
          return jscpd.getSubjectCategory().getSubjectCategoryID();

        case 3:
          return jscpd.getSubjectCategory().getSubjectCategoryName();

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
