/*
 * CollectionProperty.java
 *
 * Created on 15-feb-2011, 19:07:53
 */
package scimat.api.analysis.performance.docmapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author mjcobo
 */
public class DocumentSet implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private TreeMap<Integer, Double> docs;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param elementsList
   */
  public DocumentSet() {
    
    this.docs = new TreeMap<Integer, Double>();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param docID
   * @param weight
   */
  public void addDocument(Integer docID, double weight) {

    this.docs.put(docID, weight);
  }

  /**
   * 
   * @param docID
   */
  public void addDocument(Integer docID) {

    this.docs.put(docID, 0.0);
  }

  /**
   * 
   * @return
   */
  public ArrayList<Integer> getDocuments() {

    return new ArrayList<Integer>(this.docs.keySet());
  }

  /**
   *
   * @return
   */
  public int getDocumentsCount() {

    return this.docs.size();
  }

  /**
   * Return the weight associated with the document. A 0.0 value can mean
   * that the document is not present in the list.
   *
   * @param docID the document identifier
   * @return
   */
  public double getDocumentWeight(Integer docID) {
    
    Double value = this.docs.get(docID);

    if (value != null) {

      return value;

    } else {

      return 0.0;
    }
  }

  /**
   * 
   * @return
   */
  @Override
  public String toString() {
    
    return this.docs.toString();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
