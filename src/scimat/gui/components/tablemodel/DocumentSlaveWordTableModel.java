/*
 * DocumentSlaveWordTableModel.java
 *
 * Created on 21-mar-2011, 17:44:50
 */
package scimat.gui.components.tablemodel;

import scimat.model.knowledgebase.entity.DocumentWord;

/**
 *
 * @author mjcobo
 */
public class DocumentSlaveWordTableModel extends GenericTableModel<DocumentWord> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public DocumentSlaveWordTableModel() {
    super(new String[] {"ID", "Name","Documents" ,"Author keyword",
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
          return documentWord.getWord().getWordID();

        case 1:
          return documentWord.getWord().getWordName();
          
        case 2:
          return documentWord.getWord().getDocumentsCount();

        case 3:
          return String.valueOf(documentWord.isAuthorKeyword());

        case 4:
          return String.valueOf(documentWord.isSourceKeyword());

        case 5:
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
