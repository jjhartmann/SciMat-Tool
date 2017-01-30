/*
 * SubjectCategorysTableModel.java
 *
 * Created on 13-mar-2011, 17:19:55
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.SubjectCategory;

/**
 *
 * @author mjcobo
 */
public class SubjectCategorysTableModel extends GenericDynamicTableModel<SubjectCategory> {
  
  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public SubjectCategorysTableModel() {
    super(new String[] {"ID", "Name", "Journals", "Documents"});
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

      SubjectCategory subjectCategory = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return subjectCategory.getSubjectCategoryID();

        case 1:
          return subjectCategory.getSubjectCategoryName();

        case 2:
          return subjectCategory.getJournalsCount();

        case 3:
          return subjectCategory.getDocumentsCount();

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
