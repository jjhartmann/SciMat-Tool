/*
 * ReferenceSourcesTableModel.java
 *
 * Created on 13-mar-2011, 17:21:03
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.ReferenceSource;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourcesTableModel extends GenericDynamicTableModel<ReferenceSource> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public ReferenceSourcesTableModel() {
    super(new String[] {"ID", "Source", "References", "Documents"});
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

      ReferenceSource referenceSource = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return referenceSource.getReferenceSourceID();

        case 1:
          return referenceSource.getSource();

        case 2:
          return referenceSource.getReferencesCount();

        case 3:
          return referenceSource.getDocumentsCount();

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
