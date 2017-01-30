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
public class HighLevelItemDocElement implements Serializable, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Document Identifier
   */
  private Integer highLevelItemID;

  /**
   * The item's label.
   */
  private String label;

  /**
   * List with the document's items
   */
  private TreeSet<Integer> docs = new TreeSet<Integer>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param highLevelItemID
   * @param label
   */
  public HighLevelItemDocElement(Integer highLevelItemID, String label) {

    this.highLevelItemID = highLevelItemID;
    this.label = label;
  }

  

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public Integer getHighLevelItemID() {
    return highLevelItemID;
  }

  /**
   * 
   * @return
   */
  public String getLabel() {
    return this.label;
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
   * @return {@code true} if this high level item do not already contain the
   * document
   */
  public boolean addDocument(Integer docID) {

    return this.docs.add(docID);
  }

  /**
   * Returns {@code true} if this high level item contains the specified
   * document.
   *
   * @param docID the item's identifie
   *
   * @return {@code true} if this high level item contains the specified document
   */
  public boolean containsDocument(Integer docID) {

    return this.docs.contains(docID);
  }

  /**
   * Remove the document from the high level item.
   *
   * @param docID
   *
   * @return {@code true} if this item contain the document
   */
  public boolean removeDocument(Integer docID) {

    return this.docs.remove(docID);
  }

  /**
   * Return the number of documents associated with the high level item.
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
  protected HighLevelItemDocElement clone() {

    HighLevelItemDocElement highLevelItemDocElement;

    highLevelItemDocElement = new HighLevelItemDocElement(this.highLevelItemID, this.label);
    highLevelItemDocElement.docs = new TreeSet<Integer>(this.docs);

    return highLevelItemDocElement;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
