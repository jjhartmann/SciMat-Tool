/*
 * UnionDocumentMapper.java
 *
 * Created on 22-feb-2011, 20:29:44
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import scimat.api.dataset.AggregatedDataset;
import scimat.api.dataset.Dataset;

/**
 *
 * @author mjcobo
 */
public class UnionDocumentMapper implements DocumentMapper {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param dataset
   */
  public UnionDocumentMapper(Dataset dataset) {
    this.dataset = dataset;
  }

  /**
   *
   * @param aggregatedDataset
   */
  public UnionDocumentMapper(AggregatedDataset aggregatedDataset) {
    this.dataset = aggregatedDataset;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Execute the document mapper, returning a list with the identifiers of
   * the documents that belong to the items according to a specific
   * implementation.
   * 
   * @param itemsList A list with the identifier of the items.
   *
   * @return A document set with list of documents which are associated with the items.
   */
  public DocumentSet executeMapper(ArrayList<Integer> itemsList) {

    int i;
    TreeSet<Integer> result;
    DocumentSet documentSet;

    result = new TreeSet<Integer>();

    // Calculate the union
    for (i = 0; i < itemsList.size(); i++) {
        
      result.addAll(getDocuments(itemsList.get(i)));
    }

    documentSet = new DocumentSet();

    Iterator<Integer> iterator = result.iterator();

    while (iterator.hasNext()) {

      documentSet.addDocument(iterator.next());
    }

    return documentSet;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   *
   * @param itemID
   * @return
   */
  private ArrayList<Integer> getDocuments(Integer itemID) {

    if (this.dataset instanceof AggregatedDataset) {

      return ((AggregatedDataset)this.dataset).getDocumentsInHighLevelItem(itemID);

    } else {

      return this.dataset.getDocumentsInItem(itemID);
    }
  }
}
