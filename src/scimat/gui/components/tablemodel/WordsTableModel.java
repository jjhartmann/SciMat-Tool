/*
 * WordsTableModel.java
 *
 * Created on 13-mar-2011, 17:19:25
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.Word;

/**
 *
 * @author mjcobo
 */
public class WordsTableModel extends GenericDynamicTableModel<Word> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public WordsTableModel() {
    super(new String[] {"ID", "Name", "Documents", "Role author", "Role source", "Role added"});
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

      Word word = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return word.getWordID();

        case 1:
          return word.getWordName();

        case 2:
          return word.getDocumentsCount();

        case 3:
          return word.getRoleAuthorsCount();

        case 4:
          return word.getRoleSourcesCount();

        case 5:
          return word.getRoleAddedCount();

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
