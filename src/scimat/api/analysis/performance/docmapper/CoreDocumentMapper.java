/*
 * CoreDocumentMapper.java
 *
 * Created on 22-feb-2011, 20:29:28
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import scimat.api.dataset.AggregatedDataset;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.NetworkPair;
import scimat.api.mapping.WholeNetwork;

/**
 *
 * @author mjcobo
 */
public class CoreDocumentMapper implements DocumentMapper {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private WholeNetwork wholeNetwork;

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param wholeNetwork
   */
  public CoreDocumentMapper(WholeNetwork wholeNetwork, Dataset dataset) {
    this.wholeNetwork = wholeNetwork;
    this.dataset = dataset;
  }

  /**
   *
   * @param wholeNetwork
   */
  public CoreDocumentMapper(WholeNetwork wholeNetwork, AggregatedDataset aggregatedDataset) {
    this.wholeNetwork = wholeNetwork;
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
   * @return A list with the identifiers of the documents which are associated with the items.
   */
  public DocumentSet executeMapper(ArrayList<Integer> itemsList) {

    int i;
    ArrayList<NetworkPair> pairs;
    TreeSet<Integer> result, tmpUnion;
    DocumentSet documentSet;

    result = new TreeSet<Integer>();
    tmpUnion = new TreeSet<Integer>();

    // Get the internal pairs of the items list.
    pairs = this.wholeNetwork.getInternalPairs(itemsList);

    // For each pair calculate the intersection of its documents. The final result
    // will be the union of these intersections. That is, the final result will
    // be the union of the documents that belong to each pair.
    for (i = 0; i < pairs.size(); i++) {

      tmpUnion.clear();

      // Intersection between the document of the pairs.
      tmpUnion.addAll(getDocuments(pairs.get(i).getID().getElementA()));
      tmpUnion.retainAll(getDocuments(pairs.get(i).getID().getElementB()));

      // Result union.
      result.addAll(tmpUnion);
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
