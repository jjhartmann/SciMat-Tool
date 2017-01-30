/*
 * AuthorReferencesTableModel.java
 *
 * Created on 13-mar-2011, 17:20:33
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.AuthorReference;

/**
 *
 * @author mjcobo
 */
public class AuthorReferencesTableModel extends GenericDynamicTableModel<AuthorReference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public AuthorReferencesTableModel() {
    super(new String[] {"ID", "Name", "References", "Documents"});
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

      AuthorReference authorReference = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return authorReference.getAuthorReferenceID();

        case 1:
          return authorReference.getAuthorName();

        case 2:
          return authorReference.getReferencesCount();

        case 3:
          return authorReference.getDocumentsCount();

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
