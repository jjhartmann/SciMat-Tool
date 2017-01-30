/*
 * ReferencesTableModel.java
 *
 * Created on 13-mar-2011, 17:20:41
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.Reference;

/**
 *
 * @author mjcobo
 */
public class ReferencesTableModel extends GenericDynamicTableModel<Reference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public ReferencesTableModel() {
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

      Reference reference = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return reference.getReferenceID();

        case 1:
          return reference.getFullReference();

        case 2:
          return reference.getDocumentsCount();

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
