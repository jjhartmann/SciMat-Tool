/*
 * WordSlaveDocumentTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.DocumentWord;

/**
 *
 * @author mjcobo
 */
public class WordSlaveDocumentTableModel extends GenericTableModel<DocumentWord> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public WordSlaveDocumentTableModel() {
    super(new String[] {"ID", "Title", "Authors", "Year","Author keyword",
    "Source keyword", "Added keyword"});
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

      DocumentWord documentWord = getItem(rowIndex);

      switch (columnIndex) {

        case 0:
          return documentWord.getDocument().getDocumentID();

        case 1:
          return documentWord.getDocument().getTitle();

        case 2:
          return documentWord.getDocument().getAuthors() == null ? "" : documentWord.getDocument().getAuthors() ;

        case 3:
          return documentWord.getDocument().getYear() == null ? "" : documentWord.getDocument().getYear();

        case 4:
          return String.valueOf(documentWord.isAuthorKeyword());

        case 5:
          return String.valueOf(documentWord.isSourceKeyword());

        case 6:
          return String.valueOf(documentWord.isAddedKeyword());

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
