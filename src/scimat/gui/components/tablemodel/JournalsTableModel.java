/*
 * JournalsTableModel.java
 *
 * Created on 13-mar-2011, 17:19:45
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.Journal;

/**
 *
 * @author mjcobo
 */
public class JournalsTableModel extends GenericDynamicTableModel<Journal> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public JournalsTableModel() {
    super(new String[] {"ID", "Source", "Documents"});
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

      Journal journal = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return journal.getJournalID();

        case 1:
          return journal.getSource();

        case 2:
          return journal.getDocumentsCount();

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
