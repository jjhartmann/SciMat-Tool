/*
 * ReferenceSlaveAuthorReferenceTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.AuthorReferenceReference;

/**
 *
 * @author mjcobo
 */
public class ReferenceSlaveAuthorReferenceTableModel extends GenericTableModel<AuthorReferenceReference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public ReferenceSlaveAuthorReferenceTableModel() {
    super(new String[] {"ID", "Name", "Position"});
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
          return authorReferenceReference.getAuthorReference().getAuthorReferenceID();

        case 1:
          return authorReferenceReference.getAuthorReference().getAuthorName();

        case 2:
          return authorReferenceReference.getPosition();

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
