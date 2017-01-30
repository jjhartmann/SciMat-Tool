/*
 * ReferenceSource.java
 *
 * Created on 28-oct-2010, 20:10:46
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class ReferenceSource implements Serializable, Comparable<ReferenceSource>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceSourceID;

  /**
   *
   */
  private String source;
  private int referencesCount;
  private int documentsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referenceSourceID
   * @param source
   */
  public ReferenceSource(Integer referenceSourceID, String source, int referencesCount, int documentsCount) {
    
    this.referenceSourceID = referenceSourceID;
    this.source = source;
    this.referencesCount = referencesCount;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idReferenceSource
   */
  public Integer getReferenceSourceID() {
    return this.referenceSourceID;
  }

  /**
   * @return the source
   */
  public String getSource() {
    return this.source;
  }

  /**
   * @return the referencesCount
   */
  public int getReferencesCount() {
    return referencesCount;
  }

  /**
   * @return the documentsCount
   */
  public int getDocumentsCount() {
    return documentsCount;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "idReferenceSource: " + this.referenceSourceID + ", ";
    result += "source: " + this.source + ", ";
    result += "referencesCount: " + this.referencesCount + ", ";
    result += "documentsCount: " + this.documentsCount;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(ReferenceSource o) {

    return this.referenceSourceID.compareTo(o.referenceSourceID);
  }

  /**
   *
   * @return
   */
  @Override
  public ReferenceSource clone() {

    return new ReferenceSource(this.referenceSourceID, this.source, 
            this.referencesCount, this.documentsCount);
  }
  
  /**
   * 
   * @param obj
   * @return 
   */
  @Override
  public boolean equals(Object obj) {
    
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    ReferenceSource item = (ReferenceSource)obj;
    
    return this.referenceSourceID.equals(item.referenceSourceID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.referenceSourceID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
