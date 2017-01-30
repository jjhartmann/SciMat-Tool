/*
 * DocumentsTableModel.java
 *
 * Created on 10-mar-2011, 12:40:44
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.Document;

/**
 *
 * @author mjcobo
 */
public class DocumentsTableModel extends GenericDynamicTableModel<Document> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DocumentsTableModel() {
    super(new String[] {"ID", "Title", "Authors", "Year", "Citations", "words", "Author words", "Source words", "Added word"});
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

      Document document = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return document.getDocumentID();

        case 1:
          return document.getTitle();

        case 2:
          return document.getAuthors() == null ? "" : document.getAuthors() ;

        case 3:
          return document.getYear() == null ? "" : document.getYear();

        case 4:
          return document.getCitationsCount();

        case 5:
          return document.getWordsCount();

        case 6:
          return document.getAuthorWordsCount();

        case 7:
          return document.getSourceWordsCount();

        case 8:
          return document.getAddedWordsCount();

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
