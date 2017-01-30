/*
 * CouplingDocumentMapper.java
 *
 * Created on 02-abr-2011, 12:25:39
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;

/**
 *
 * @author mjcobo
 */
public class CouplingDocumentMapper implements DocumentMapper {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param dataset
   */
  public CouplingDocumentMapper() {
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Execute the documents mapper, returning a list with the identifiers of
   * the documents that belong to the items according to a specific
   * implementation.
   *
   * This mapper is specifically defined to work with networks built from basic
   * coupling data. That is, the items of the itemsList (the network's nodes)
   * will take into account as document.
   *
   * @param itemsList A list with the identifier of the items.
   *
   * @return A document set with list of documents which are associated with the items.
   */
  public DocumentSet executeMapper(ArrayList<Integer> itemsList) {

    int i;
    DocumentSet documentSet;
    
    documentSet = new DocumentSet();

    // Calculate the union
    for (i = 0; i < itemsList.size(); i++) {

      documentSet.addDocument(itemsList.get(i));
    }

    return documentSet;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
