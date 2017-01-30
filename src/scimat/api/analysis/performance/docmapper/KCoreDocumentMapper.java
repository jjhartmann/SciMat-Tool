/*
 * KCoreDocumentMapper.java
 *
 * Created on 22-feb-2011, 20:30:06
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import scimat.api.dataset.AggregatedDataset;
import scimat.api.dataset.Dataset;

/**
 *
 * @author mjcobo
 */
public class KCoreDocumentMapper implements DocumentMapper {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private int k;

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param k the minimum number of items where the documents have to be present.
   * @param dataset 
   */
  public KCoreDocumentMapper(int k, Dataset dataset) {
    this.k = k;
    this.dataset = dataset;
  }

  /**
   *
   * @param k the minimum number of items where the documents have to be present.
   * @param aggregatedDataset
   */
  public KCoreDocumentMapper(int k, AggregatedDataset aggregatedDataset) {
    this.k = k;
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

    int i, j;
    Integer count, docID;
    // Key: docID. Value: number of items where the documents is present.
    TreeMap<Integer, Integer> docCounter;
    ArrayList<Integer> docList;
    Iterator<Integer> iterator;
    DocumentSet documentSet;

    docCounter = new TreeMap<Integer, Integer>();

    // Calculate the number of nodes where the documents are present.
    for (i = 0; i < itemsList.size(); i++) {

      docList = getDocuments(itemsList.get(i));

      for (j = 0; j < docList.size(); j++) {

        count = docCounter.get(docList.get(j));

        if (count != null) {

          docCounter.put(docList.get(j), count + 1);

        } else {

          docCounter.put(docList.get(j), 1);
        }
      }
    }

    documentSet = new DocumentSet();
    iterator = docCounter.keySet().iterator();

    while (iterator.hasNext()) {

      docID = iterator.next();

      if (docCounter.get(docID) >= this.k) {

        documentSet.addDocument(docID);
      }
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
