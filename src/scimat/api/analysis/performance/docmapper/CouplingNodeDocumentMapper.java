/*
 * CouplingNodeDocumentMapper.java
 *
 * Created on 02-abr-2011, 12:34:50
 */
package scimat.api.analysis.performance.docmapper;

import scimat.api.dataset.Dataset;

/**
 *
 * @author mjcobo
 */
public class CouplingNodeDocumentMapper implements NodeDocumentMapper {

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
  public CouplingNodeDocumentMapper() {
    
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Execute the document mapper, returning a list with the identifiers of
   * the documents that belong to the items according to a specific
   * implementation.
   *
   * This mapper works with networks built from basic coupling. In this case,
   * the item will be consider as a document, so the document set will have only
   * one documents (the item).
   *
   * @param itemsList A list with the identifier of the items.
   *
   * @return A list with the identifiers of the documents which are associated with the items.
   */
  public DocumentSet executeMapper(Integer item) {

    
    DocumentSet documentSet = new DocumentSet();

    documentSet.addDocument(item);

    return documentSet;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
