/*
 * DocumentSlaveAuthorTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.DocumentAuthor;

/**
 *
 * @author mjcobo
 */
public class DocumentSlaveAuthorTableModel extends GenericTableModel<DocumentAuthor> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public DocumentSlaveAuthorTableModel() {
    super(new String[] {"ID", "Name", "Full name", "Position", "Documents"});
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

      DocumentAuthor documentAuthor = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return documentAuthor.getAuthor().getAuthorID();

        case 1:
          return documentAuthor.getAuthor().getAuthorName();

        case 2:
          return documentAuthor.getAuthor().getFullAuthorName();

        case 3:
          return documentAuthor.getPosition();

        case 4:
          return documentAuthor.getAuthor().getDocumentsCount();

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
