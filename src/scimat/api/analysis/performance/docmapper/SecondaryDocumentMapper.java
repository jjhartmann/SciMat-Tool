/*
 * SecondaryDocumentMapper.java
 *
 * Created on 22-feb-2011, 20:29:14
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import scimat.api.dataset.AggregatedDataset;
import scimat.api.dataset.Dataset;

/**
 * This class implements a documents assigner that assocites to a set of item
 * the set of document which only appears in one items. Mathematically is the
 * symmetric difference.
 * 
 * @author mjcobo
 */
public class SecondaryDocumentMapper implements DocumentMapper {

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
  public SecondaryDocumentMapper(Dataset dataset) {
    this.dataset = dataset;
  }

  /**
   *
   * @param aggregatedDataset
   */
  public SecondaryDocumentMapper(AggregatedDataset aggregatedDataset) {
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
    TreeSet<Integer> symmetricDifference, tmp;
    DocumentSet documentSet;

    symmetricDifference = new TreeSet<Integer>();

    // Calculate the symmetric difference: (A union B) / (A intersection B)
    if (itemsList.size() > 0) {

      symmetricDifference.addAll(getDocuments(itemsList.get(0)));

      for (i = 1; i < itemsList.size(); i++) {

        tmp = new TreeSet<Integer>(symmetricDifference);

        // Calculate the union
        symmetricDifference.addAll(getDocuments(itemsList.get(i)));

        // Calculate the intersection
        tmp.retainAll(getDocuments(itemsList.get(i)));

        // Calculate the difference
        symmetricDifference.removeAll(tmp);

      }
    }

    documentSet = new DocumentSet();

    Iterator<Integer> iterator = symmetricDifference.iterator();

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
