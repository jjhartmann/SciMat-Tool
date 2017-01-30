/*
 * Dataset.java
 *
 * Created on 11-ene-2011, 13:42:35
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
public class Dataset implements Serializable, Cloneable{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Map with the document list and their associated items.
   */
  private TreeMap<Integer, DocItemElement> docs = new TreeMap<Integer, DocItemElement>();

  /**
   * Map with the items and their associated documents.
   */
  private TreeMap<Integer, ItemDocElement> items = new TreeMap<Integer, ItemDocElement>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /*
   * Create a new dataset empty.
   */
  public Dataset() {
    
  }
  
  /**
   * 
   * @param dataset 
   */
  public Dataset(Dataset dataset) {
  
    Integer id;
    Iterator<Integer> it;

    it = dataset.docs.keySet().iterator();

    while (it.hasNext()) {

      id = it.next();

      this.docs.put(id, dataset.docs.get(id).clone());
    }

    it = dataset.items.keySet().iterator();

    while (it.hasNext()) {

      id = it.next();

      this.items.put(id, dataset.items.get(id).clone());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Add a new document to the dataset with the specified parameters.
   *
   * @param docID the document's identifier
   *
   * @return {@code true} if the document has been added
   */
  public boolean addDocument(Integer docID, int timesCited) {

    // The document will be added if it is not already on the dataset.
    if (this.docs.containsKey(docID)) {

      return false;

    } else {

      this.docs.put(docID, new DocItemElement(docID, timesCited));

      return true;
    }
  }

  /**
   * Add a new document to the dataset with the specified parameters.
   * 
   * @param docID the document's identifier
   *
   * @return {@code true} if the document has been added
   */
  public boolean addDocument(Integer docID) {

    // The document will be added if it is not already on the dataset.
    if (this.docs.containsKey(docID)) {

      return false;
    
    } else {

      this.docs.put(docID, new DocItemElement(docID));

      return true;
    }
  }

  /**
   * Return an array with all the documents' ID of the dataset.
   *
   * @return an array with the documents' ID.
   */
  public ArrayList<Integer> getDocuments() {

    return new ArrayList<Integer>(this.docs.keySet());
  }

  /**
   * 
   * @param docID
   * @return
   */
  public boolean containsDocument(Integer docID) {

    return this.docs.containsKey(docID);
  }

  /**
   * Return an array with all the itmes' ID of the dataset.
   *
   * @return an array with the items' ID.
   */
  public ArrayList<Integer> getItems() {

    return new ArrayList<Integer>(this.items.keySet());
  }

  /**
   *
   * @param docID
   * @return
   */
  public boolean containsItem(Integer itemID) {

    return this.items.containsKey(itemID);
  }

  /**
   *
   * @param docID
   * @return am array with the items associated with the document.
   */
  public ArrayList<Integer> getItemsInDocument(Integer docID) 
          throws NotExistsItemException {

    DocItemElement doc = this.docs.get(docID);

    if (doc != null) {

      return new ArrayList<Integer>(doc.getItemsList());

    } else {

      throw new NotExistsItemException("The document " + docID + " does "
              + "not exist in the dataset.");
    }

  }

  /**
   *
   * @param itemID
   * @return am array with the documents associated with the item.
   */
  public ArrayList<Integer> getDocumentsInItem(Integer itemID)
          throws NotExistsItemException {

    ItemDocElement item = this.items.get(itemID);

    if (item != null) {

      return new ArrayList<Integer>(item.getDocumentsList());

    } else {

      throw new NotExistsItemException("The item " + itemID + " does "
              + "not exist in the dataset.");
    }

  }

  /**
   *
   * @param itemID
   * @return the number of documents associatied with the item.
   */
  public int getDocumentsInItemCount(Integer itemID)
          throws NotExistsItemException {

    ItemDocElement item = this.items.get(itemID);

    if (item != null) {

      return item.getDocumentsCount();

    } else {

      throw new NotExistsItemException("The item " + itemID + " does "
              + "not exist in the dataset.");
    }

  }
  
  /**
   * 
   * @param docID
   * @throws NotExistsItemException
   */
  public void removeDocument(Integer docID) throws NotExistsItemException {

    int i;
    DocItemElement doc;
    ItemDocElement item;
    ArrayList<Integer> itemList;

    doc = this.docs.remove(docID);

    // If the document exists in the data set:
    // - Remove the document from the docMap
    // - Remove the document from the itemMap
    if (doc != null) {

      itemList = doc.getItemsList();

      for (i = 0; i < itemList.size(); i++) {

        item = this.items.get(itemList.get(i));

        item.removeDocument(docID);

      }

    } else {

      throw new NotExistsItemException("The document " + docID + " does "
              + "not exist in the dataset.");

    }
  }

  /**
   * 
   * @param itemID
   * @throws NotExistsItemException
   */
  public void removeItem(Integer itemID) throws NotExistsItemException {

    int i;
    DocItemElement doc;
    ItemDocElement item;
    ArrayList<Integer> docList;

    item = this.items.remove(itemID);

    // If the item exists in the data set:
    // - Remove the item from the itemMap
    // - Remove the item from the docMap
    if (item != null) {

      docList = item.getDocumentsList();

      for (i = 0; i < docList.size(); i++) {

        doc = this.docs.get(docList.get(i));

        doc.removeItem(itemID);
        
      }

    } else {

      throw new NotExistsItemException("The item " + itemID + " does not "
              + "exist in the dataset.");
    }
  }

  /**
   *
   * @param docID
   * @param itemID
   * @param itemLabel
   * @return
   */
  public boolean addItemToDocument(Integer docID, Integer itemID,
          String itemLabel) throws NotExistsItemException {

    DocItemElement doc;
    ItemDocElement item;

    doc = this.docs.get(docID);

    // It the document already exists in the dataset.
    if (doc != null) {

      item = this.items.get(itemID);

      // If the item does not yet exist in the dataset
      if (item == null) {

        item = new ItemDocElement(itemID, itemLabel);

        this.items.put(itemID, item);
      }

      if (! doc.containsItem(itemID)) {

        doc.addItem(itemID);
        item.addDocument(docID);

        return true;

      } else {

        return false;
      }

    } else {

      throw new NotExistsItemException("The document " + docID + " does "
              + "not exist in the dataset.");
    }

  }

  /**
   * 
   * @param docID
   * @return
   * @throws NotExistsItemException
   */
  public int getDocumentCitations(Integer docID) throws NotExistsItemException {

    DocItemElement doc;

    doc = this.docs.get(docID);

    if (doc != null) {

      return doc.getTimesCited();

    } else {

      throw new NotExistsItemException("The document " + docID + " does "
              + "not exist in the dataset.");
    }
  }

  /**
   *
   * @return
   */
  public int getDocumentsCount() {

    return this.docs.size();
  }

  /**
   *
   * @param docID
   * @return
   * @throws NotExistsItemException
   */
  public int getDocumentFrequency(Integer docID) throws NotExistsItemException {

    DocItemElement doc;

    doc = this.docs.get(docID);

    if (doc != null) {

      return doc.getItemsCount();

    } else {

      throw new NotExistsItemException("The document " + docID + " does not "
              + "exist in the dataset.");
    }
  }

  /**
   *
   * @return
   */
  public int getItemsCount() {

    return this.items.size();
  }

  /**
   * 
   * @param itemID
   * @return
   * @throws NotExistsItemException
   */
  public int getItemFrequency(Integer itemID) throws NotExistsItemException {

    ItemDocElement item;

    item = this.items.get(itemID);

    if (item != null) {

      return item.getDocumentsCount();

    } else {

      throw new NotExistsItemException("The item " + itemID + " does not "
              + "exist in the dataset.");
    }
  }

  /**
   *
   * @param itemID
   * @return
   * @throws NotExistsItemException
   */
  public String getItemLabel(Integer itemID) throws NotExistsItemException {

    ItemDocElement item;

    item = this.items.get(itemID);

    if (item != null) {

      return item.getLabel();

    } else {

      throw new NotExistsItemException("The item " + itemID + " does not "
              + "exist in the dataset.");
    }
  }

  /**
   * 
   * @param docID
   * @return
   * @throws NotExistsItemException
   */
  public int getItemsInDocumentCount(Integer docID) throws NotExistsItemException {

    DocItemElement doc;

    doc = this.docs.get(docID);

    if (doc != null) {

      return doc.getItemsCount();

    } else {

      throw new NotExistsItemException("The document " + docID + " does "
              + "not exist in the dataset.");
    }

  }

  @Override
  public Dataset clone() {

    Dataset dataset = new Dataset();
    Integer id;
    Iterator<Integer> it;

    it = this.docs.keySet().iterator();

    while (it.hasNext()) {

      id = it.next();

      dataset.docs.put(id, this.docs.get(id).clone());
    }

    it = this.items.keySet().iterator();

    while (it.hasNext()) {

      id = it.next();

      dataset.items.put(id, this.items.get(id).clone());
    }

    return dataset;
  }



  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
