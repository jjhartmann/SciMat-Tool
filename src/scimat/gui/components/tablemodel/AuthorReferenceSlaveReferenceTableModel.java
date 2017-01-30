/*
 * AuthorReferenceSlaveReferenceTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.AuthorReferenceReference;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceSlaveReferenceTableModel extends GenericTableModel<AuthorReferenceReference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public AuthorReferenceSlaveReferenceTableModel() {
    super(new String[] {"ID", "Full reference", "Documents"});
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

      AuthorReferenceReference authorReferenceReference = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return authorReferenceReference.getReference().getReferenceID();

        case 1:
          return authorReferenceReference.getReference().getFullReference();

        case 2:
          return authorReferenceReference.getReference().getDocumentsCount();

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
