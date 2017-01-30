/*
 * AuthorGroupsTableModel.java
 *
 * Created on 13-mar-2011, 17:19:07
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.AuthorGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorGroupsTableModel extends GenericDynamicTableModel<AuthorGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public AuthorGroupsTableModel() {
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

      AuthorGroup group = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return group.getAuthorGroupID();

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
