/*
 * ItemDocElement.java
 *
 * Created on 11-ene-2011, 13:44:25
 */
package scimat.api.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class ItemDocElement implements Serializable, Cloneable{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The item's identifier.
   */
  private Integer itemID;

  /**
   * The item's label.
   */
  private String label;

  /**
   * List with the document associate with this item.
   */
  private TreeSet<Integer> docs = new TreeSet<Integer>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * <p>Create a new {@code ItemDocElement} with the specified parameters.</p>
   *
   * @param itemID the item's identifier
   * @param label the item's label
   */
  public ItemDocElement(Integer itemID, String label) {

    this.itemID = itemID;
    this.label = label;
  }

  /**
   * 
   * @param itemDocElement
   */
  public ItemDocElement(ItemDocElement itemDocElement) {

    this.itemID = itemDocElement.itemID;
    this.label = itemDocElement.label;
    this.docs = new TreeSet<Integer>(itemDocElement.docs);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the itemID
   */
  public Integer getItemID() {
    return itemID;
  }

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Add a new document to the item.
   *
   * @param docID the document's identifier
   *
   * @return {@code true} if this item did not already contain the document
   */
  public boolean addDocument(Integer docID) {

    return this.docs.add(docID);
  }

  /**
   * Returns {@code true} if this item contains the specified document.
   *
   * @param docID the item's identifie
   *
   * @return {@code true} if this item contains the specified document
   */
  public boolean containsDocument(Integer docID) {

    return this.docs.contains(docID);
  }

  /**
   * Remove the document from the item.
   *
   * @param docID
   *
   * @return {@code true} if this item contain the document
   */
  public boolean removeDocument(Integer docID) {

    return this.docs.remove(docID);
  }

  /**
   * Return the number of documents associated with the item (item's frequency).
   *
   * @return the numner of items of the document.
   */
  public int getDocumentsCount() {

    return this.docs.size();
  }
  
  /**
   *
   * @return
   */
  public ArrayList<Integer> getDocumentsList() {

    return new ArrayList<Integer>(this.docs);
  }

  /**
   * 
   * @return
   * @throws CloneNotSupportedException
   */
  @Override
  protected ItemDocElement clone() {

    ItemDocElement itemDocElement;

    itemDocElement = new ItemDocElement(itemID, label);
    itemDocElement.docs = new TreeSet<Integer>(docs);

    return itemDocElement;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "ItemID: " + this.itemID;
    result += "Label: " + this.label;
    result += "Docs: " + this.docs;
    result += ")";

    return result;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
