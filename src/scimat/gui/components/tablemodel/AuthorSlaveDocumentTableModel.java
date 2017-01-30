/*
 * AuthorSlaveDocumentTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.DocumentAuthor;

/**
 *
 * @author mjcobo
 */
public class AuthorSlaveDocumentTableModel extends GenericTableModel<DocumentAuthor> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public AuthorSlaveDocumentTableModel() {
    super(new String[] {"ID", "Title", "Authors", "Year", "Citations", "Position"});
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
          return documentAuthor.getDocument().getDocumentID();

        case 1:
          return documentAuthor.getDocument().getTitle();

        case 2:
          return documentAuthor.getDocument().getAuthors() == null ? "" : documentAuthor.getDocument().getAuthors() ;

        case 3:
          return documentAuthor.getDocument().getYear() == null ? "" : documentAuthor.getDocument().getYear();

        case 4:
          return documentAuthor.getDocument().getCitationsCount();

        case 5:
          return documentAuthor.getPosition();

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
