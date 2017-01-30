/*
 * Affiliation.java
 *
 * Created on 28-feb-2011, 13:17:45
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 * <p>This class represent an author's affiliation.</p>
 *
 * <p>Each <code>Affiliation</code> contains one single Identifier and the
 * complete affiliation.</p>
 * 
 * @author mjcobo
 */
public class Affiliation implements Serializable, Comparable<Affiliation>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The ID of the Affiliation
   */
  private Integer affiliationID;

  /**
   * This attribute contains the complete affiliation.
   */
  private String fullAffiliation;
  private int documentsCount;
  private int authorsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Create a new Affiliation.
   *
   * @param affiliationID the ID
   * @param fullAffiliation the Affiliation
   * @param documentsCount the number of documents where the affiliation is present
   */
  public Affiliation(Integer affiliationID, String fullAffiliation, int documentsCount, int authorsCount) {

    this.affiliationID = affiliationID;
    this.fullAffiliation = fullAffiliation;
    this.documentsCount = documentsCount;
    this.authorsCount = authorsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the affiliationID
   */
  public Integer getAffiliationID() {
    return affiliationID;
  }

  /**
   * @return the fullAffilliation
   */
  public String getFullAffiliation() {
    return fullAffiliation;
  }

  /**
   * @return the documentsCount
   */
  public int getDocumentsCount() {
    return documentsCount;
  }

  /**
   * @return the authorsCount
   */
  public int getAuthorsCount() {
    return authorsCount;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "idAffiliation: " + this.affiliationID + ", ";
    result += "fullAffiliation: " + this.fullAffiliation + ", ";
    result += "documentsCount: " + this.documentsCount + ", ";
    result += "authorsCount: " + this.authorsCount;
    result += ")";

    return result;
  }

  /**
   * 
   * @param o
   * @return
   */
  public int compareTo(Affiliation o) {
    return this.affiliationID.compareTo(o.affiliationID);
  }

  /**
   * 
   * @return
   */
  @Override
  protected Affiliation clone() {

    return new Affiliation(affiliationID, fullAffiliation, this.documentsCount, this.authorsCount);
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

    Affiliation item = (Affiliation)obj;
    
    return this.affiliationID.equals(item.affiliationID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.affiliationID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
