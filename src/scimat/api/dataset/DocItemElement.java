/*
 * DocItemElement.java
 *
 * Created on 11-ene-2011, 13:44:16
 */
package scimat.api.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author mjcobo
 */
public class DocItemElement implements Serializable, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Document Identifier
   */
  private Integer docID;

  /**
   * Citations acheived by the document.
   */
  private int timesCited;

  /**
   * List with the document's items
   */
  private TreeSet<Integer> items = new TreeSet<Integer>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param docID
   */
  public DocItemElement(Integer docID) {
    
    this.docID = docID;
    this.timesCited = 0;
  }

  /**
   * 
   */
  public DocItemElement(Integer docID, int timesCited) {

    this.docID = docID;
    this.timesCited = timesCited;
  }

  /**
   * 
   * @param docItemElement
   */
  public DocItemElement(DocItemElement docItemElement) {

    this.docID = docItemElement.docID;
    this.timesCited = docItemElement.timesCited;
    this.items = new TreeSet<Integer>(docItemElement.items);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the docID
   */
  public Integer getDocID() {
    return docID;
  }

  /**
   * 
   * @return
   */
  public int getTimesCited() {
    return timesCited;
  }

  /**
   * Add a new item to the document.
   *
   * @param itemID the item's identifier
   *
   * @return {@code true} if this document do not already contain the item
   */
  public boolean addItem(Integer itemID) {

    return this.items.add(itemID);
  }

  /**
   * Returns {@code true} if this document contains the specified item.
   *
   * @param itemID the item's identifie
   *
   * @return {@code true} if this document contains the specified item
   */
  public boolean containsItem(Integer itemID) {

    return this.items.contains(itemID);
  }

  /**
   * Remove the item from the document.
   *
   * @param itemID
   *
   * @return {@code true} if this document contain the item
   */
  public boolean removeItem(Integer itemID) {

    return this.items.remove(itemID);
  }

  /**
   * Return the number of items of the document.
   *
   * @return the numner of items of the document.
   */
  public int getItemsCount() {

    return this.items.size();
  }

  /**
   * 
   * @return
   */
  public ArrayList<Integer> getItemsList() {

    return new ArrayList<Integer>(items);
  }

  @Override
  protected DocItemElement clone() {

    DocItemElement docItemElement;
    
    docItemElement = new DocItemElement(docID, timesCited);
    docItemElement.items = new TreeSet<Integer>(items);

    return docItemElement;
  }

  /**
   * 
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "DocID: " + this.docID;
    result += "Times cited: " + this.timesCited;
    result += "Items: " + this.items;
    result += ")";

    return result;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
