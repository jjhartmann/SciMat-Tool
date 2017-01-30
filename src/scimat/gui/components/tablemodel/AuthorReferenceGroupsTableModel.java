/*
 * AuthorReferenceGroupsTableModel.java
 *
 * Created on 13-mar-2011, 17:20:12
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.AuthorReferenceGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupsTableModel extends GenericDynamicTableModel<AuthorReferenceGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public AuthorReferenceGroupsTableModel() {
    super(new String[] {"ID", "Group name", "Stop group", "Items", "Documents"});
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

      AuthorReferenceGroup group = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return group.getAuthorReferenceGroupID();

        case 1:
          return group.getGroupName();

        case 2:
          return String.valueOf(group.isStopGroup());

        case 3:
          return group.getItemsCount();

        case 4:
          return group.getDocumentsCount();

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
