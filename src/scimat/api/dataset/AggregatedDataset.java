/*
 * AggregatedDataset.java
 *
 * Created on 01-abr-2011, 17:52:23
 */
package scimat.api.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public class AggregatedDataset extends Dataset implements Serializable, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Map with the document list and their associated items.
   */
  private TreeMap<Integer, HighLevelItemDocElement> highLevelItemDocElement = new TreeMap<Integer, HighLevelItemDocElement>();

  /**
   *
   */
  private TreeMap<Integer, DocHighLevelItemElement> docHighLevelItemElement = new TreeMap<Integer, DocHighLevelItemElement>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param dataset 
   */
  public AggregatedDataset(Dataset dataset) {
    super(dataset);

    int i;
    Integer docID;
    ArrayList<Integer> docs;

    docs = dataset.getDocuments();

    for (i = 0; i < docs.size(); i++) {

      docID = docs.get(i);

      this.docHighLevelItemElement.put(docID, new DocHighLevelItemElement(docID));
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param highLevelItemID
   * @param label
   * @return
   */
  public boolean addHighLevelItem(Integer highLevelItemID, String label) {

    // The high level item will be added if it is not already on the aggregated
    // dataset.
    if (this.highLevelItemDocElement.containsKey(highLevelItemID)) {

      return false;

    } else {

      this.highLevelItemDocElement.put(highLevelItemID, new HighLevelItemDocElement(highLevelItemID, label));

      return true;
    }
  }



  /**
   *
   * @return
   */
  public ArrayList<Integer> getHighLevelItems() {

    return new ArrayList<Integer>(this.highLevelItemDocElement.keySet());
  }

  /**
   * 
   * @return
   */
  public int getHighLevelItemsCount() {

    return this.highLevelItemDocElement.size();
  }

  /**
   *
   * @param highLevelItemID
   * @return
   */
  public String getHighLevelItemLabel(Integer highLevelItemID) {

    HighLevelItemDocElement aggregatedDocItem;

    aggregatedDocItem = this.highLevelItemDocElement.get(highLevelItemID);

    if (aggregatedDocItem != null) {

      return aggregatedDocItem.getLabel();



    } else {

      throw new NotExistsItemException("The high level item " + highLevelItemID + " does "
              + "not exist in the aggregated dataset.");
    }
  }

  /**
   *
   * @param highLevelItemID
   * @param docID
   * @return
   * @throws NotExistsItemException
   */
  public boolean addDocumentToHighLevelItem(Integer highLevelItemID,
          Integer docID) throws NotExistsItemException {

    HighLevelItemDocElement highLevelItem;
    DocHighLevelItemElement doc;

    highLevelItem = this.highLevelItemDocElement.get(highLevelItemID);

    // If the high level item is in the aggregated dataset.
    if (highLevelItem != null) {

      doc = this.docHighLevelItemElement.get(docID);

      // If the dataset contains the document.
      if (doc != null) {

        if (highLevelItem.addDocument(docID)) {

          doc.addHighLevelItem(highLevelItemID);

          return true;

        } else {

          return false;
        }

      } else {

        throw new NotExistsItemException("The document " + docID + " does "
              + "not exist in the aggregated dataset.");
      }

    } else {

      throw new NotExistsItemException("The high level item " + highLevelItemID + " does "
              + "not exist in the aggregated dataset.");
    }
  }

  /**
   * 
   * @param highLevelItemID
   * @return
   */
  public ArrayList<Integer> getDocumentsInHighLevelItem(Integer highLevelItemID) {

    HighLevelItemDocElement aggregatedDocItem;

    aggregatedDocItem = this.highLevelItemDocElement.get(highLevelItemID);

    if (aggregatedDocItem != null) {

      return aggregatedDocItem.getDocumentsList();

    } else {

      throw new NotExistsItemException("The high level item " + highLevelItemID + " does "
              + "not exist in the aggregated dataset.");
    }
  }

  /**
   *
   * @param highLevelItemID
   * @return
   */
  public int getDocumentsInHighLevelItemCount(Integer highLevelItemID) {

    HighLevelItemDocElement aggregatedDocItem;

    aggregatedDocItem = this.highLevelItemDocElement.get(highLevelItemID);

    if (aggregatedDocItem != null) {

      return aggregatedDocItem.getDocumentsCount();

    } else {

      throw new NotExistsItemException("The high level item " + highLevelItemID + " does "
              + "not exist in the aggregated dataset.");
    }
  }

  /**
   *
   * @param docID
   * @throws NotExistsItemException
   */
  @Override
  public void removeDocument(Integer docID) throws NotExistsItemException {
    super.removeDocument(docID);

    int i;
    DocHighLevelItemElement doc;
    ArrayList<Integer> highLevelItemIDs;

    doc = this.docHighLevelItemElement.remove(docID);

    highLevelItemIDs = doc.getHighLevelItemsList();

    // Remove the document from each high level item.
    for (i = 0; i < highLevelItemIDs.size(); i++) {

      this.highLevelItemDocElement.get(highLevelItemIDs.get(i)).removeDocument(docID);
    }
  }

  /**
   * 
   * @return
   * @throws CloneNotSupportedException
   */
  @Override
  public AggregatedDataset clone() {

    AggregatedDataset aggregatedDataset;
    Integer id;
    Iterator<Integer> it;

    aggregatedDataset = new AggregatedDataset(super.clone());

    it = this.docHighLevelItemElement.keySet().iterator();

    while (it.hasNext()) {

      id = it.next();

      aggregatedDataset.docHighLevelItemElement.put(id, this.docHighLevelItemElement.get(id).clone());
    }

    it = this.highLevelItemDocElement.keySet().iterator();

    while (it.hasNext()) {

      id = it.next();

      aggregatedDataset.highLevelItemDocElement.put(id, this.highLevelItemDocElement.get(id).clone());
    }

    return aggregatedDataset;
  }



  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
