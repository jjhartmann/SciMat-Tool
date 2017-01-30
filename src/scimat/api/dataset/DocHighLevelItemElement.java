/*
 * HighLevelItemDocElement.java
 *
 * Created on 01-abr-2011, 17:42:53
 */
package scimat.api.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * This class represent an high level item which aggregates a set of documents.
 * 
 * @author mjcobo
 */
public class DocHighLevelItemElement implements Serializable, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Document Identifier
   */
  private Integer docID;

  /**
   * List with the document's items
   */
  private TreeSet<Integer> highLevelItems = new TreeSet<Integer>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param docID
   */
  public DocHighLevelItemElement(Integer docID) {

    this.docID = docID;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public Integer getDocID() {
    return docID;
  }

  /**
   * Add a new high level item to the document.
   *
   * @param highLevelItemID the high level item's identifier
   *
   * @return {@code true} if this document do not already contain the high level item.
   */
  public boolean addHighLevelItem(Integer highLevelItemID) {

    return this.highLevelItems.add(highLevelItemID);
  }

  /**
   * Returns {@code true} if this document contains the specified high level item.
   *
   * @param highLevelItemID the item's identifie
   *
   * @return {@code true} if this document contains the specified high level item
   */
  public boolean containsHighLevelItem(Integer highLevelItemID) {

    return this.highLevelItems.contains(highLevelItemID);
  }

  /**
   * Remove the high level item from the document.
   *
   * @param highLevelItemID
   *
   * @return {@code true} if this document contain the high level item
   */
  public boolean removeHighLevelItem(Integer highLevelItemID) {

    return this.highLevelItems.remove(highLevelItemID);
  }

  /**
   * Return the number of high level items associated with the document.
   *
   * @return the number of high level item of the documents.
   */
  public int getHighLevelItemsCount() {

    return this.highLevelItems.size();
  }

  /**
   *
   * @return
   */
  public ArrayList<Integer> getHighLevelItemsList() {

    return new ArrayList<Integer>(this.highLevelItems);
  }

  /**
   * 
   * @return
   */
  @Override
  protected DocHighLevelItemElement clone() {

    DocHighLevelItemElement docHighLevelItemElement;

    docHighLevelItemElement = new DocHighLevelItemElement(this.docID);
    docHighLevelItemElement.highLevelItems = new TreeSet<Integer>(this.highLevelItems);

    return docHighLevelItemElement;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
