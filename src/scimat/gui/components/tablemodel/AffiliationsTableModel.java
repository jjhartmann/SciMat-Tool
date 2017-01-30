/*
 * AffiliationsTableModel.java
 *
 * Created on 13-mar-2011, 17:18:59
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.Affiliation;

/**
 *
 * @author mjcobo
 */
public class AffiliationsTableModel extends GenericDynamicTableModel<Affiliation> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public AffiliationsTableModel() {
    super(new String[] {"ID", "Full affiliation", "Documents", "Authors"});
   
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

      Affiliation affiliation = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return affiliation.getAffiliationID();

        case 1:
          return affiliation.getFullAffiliation();

        case 2:
          return affiliation.getDocumentsCount();

        case 3:
          return affiliation.getAuthorsCount();

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
