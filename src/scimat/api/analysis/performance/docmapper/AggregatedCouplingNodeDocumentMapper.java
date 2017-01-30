/*
 * BasicNodeDocumentMapper.java
 *
 * Created on 02-abr-2011, 12:34:50
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;
import scimat.api.dataset.AggregatedDataset;

/**
 *
 * @author mjcobo
 */
public class AggregatedCouplingNodeDocumentMapper implements NodeDocumentMapper {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private AggregatedDataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param dataset
   */
  public AggregatedCouplingNodeDocumentMapper(AggregatedDataset dataset) {
    this.dataset = dataset;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Execute the document mapper, returning a list with the identifiers of
   * the documents that belong to the items according to a specific
   * implementation.
   *
   * The document set will contain the documents associated with this items.
   *
   * @param itemsList A list with the identifier of the items.
   *
   * @return A list with the identifiers of the documents which are associated with the items.
   */
  public DocumentSet executeMapper(Integer item) {

    int i;
    ArrayList<Integer> docsInItems;
    DocumentSet documentSet;

    documentSet = new DocumentSet();

    docsInItems = this.dataset.getDocumentsInHighLevelItem(item);

    for (i = 0; i < docsInItems.size(); i++) {

      documentSet.addDocument(docsInItems.get(i));
    }

    return documentSet;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
