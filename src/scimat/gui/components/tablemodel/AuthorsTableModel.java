/*
 * AuthorsTableModel.java
 *
 * Created on 13-mar-2011, 17:19:12
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.Author;

/**
 *
 * @author mjcobo
 */
public class AuthorsTableModel extends GenericDynamicTableModel<Author> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public AuthorsTableModel() {
    super(new String[] {"ID", "Name", "Full Name", "Documents", "Affiliations"});
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
  public Object getValueAt(int rowIndex, int columnIndex) {

    if ((rowIndex >= 0) && (rowIndex < getRowCount())) {

      Author author = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return author.getAuthorID();

        case 1:
          return author.getAuthorName();

        case 2:
          return author.getFullAuthorName();

        case 3:
          return author.getDocumentsCount();

        case 4:
          return author.getAffiliationsCount();

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
