/*
 * Reference.java
 *
 * Created on 21-oct-2010, 17:48:49
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class Reference implements Serializable, Comparable<Reference>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceID;

  /**
   *
   */
  private String fullReference;

  /**
   *
   */
  private String volume;

  /**
   *
   */
  private String issue;

  /**
   *
   */
  private String page;

  /**
   *
   */
  private String year;

  /**
   *
   */
  private String doi;

  /**
   *
   */
  private String format;
  private int documentsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referenceID
   * @param fullReference
   * @param volume
   * @param issue
   * @param page
   * @param year
   * @param doi
   * @param format
   */
  public Reference(Integer referenceID, String fullReference, String volume,
          String issue, String page, String year, String doi, String format, 
          int documentsCount) {

    this.referenceID = referenceID;
    this.fullReference = fullReference;
    this.volume = volume;
    this.issue = issue;
    this.page = page;
    this.year = year;
    this.doi = doi;
    this.format = format;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idReference
   */
  public Integer getReferenceID() {
    return referenceID;
  }

  /**
   * @return the fullReference
   */
  public String getFullReference() {
    return fullReference;
  }

  /**
   * @return the volume
   */
  public String getVolume() {
    return volume;
  }

  /**
   * @return the issue
   */
  public String getIssue() {
    return issue;
  }

  /**
   * @return the page
   */
  public String getPage() {
    return page;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @return the doi
   */
  public String getDoi() {
    return doi;
  }

  /**
   * @return the format
   */
  public String getFormat() {
    return format;
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
    result += "idReference: " + this.referenceID + ", ";
    result += "fullReference: " + this.fullReference + ", ";
    result += "volume: " + this.volume + ", ";
    result += "issue: " + this.issue + ", ";
    result += "page: " + this.page + ", ";
    result += "doi: " + this.doi + ", ";
    result += "format: " + this.format + ", ";
    result += "year: " + this.year + ", ";
    result += "documentsCount: " + this.documentsCount;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(Reference o) {
    return this.referenceID.compareTo(o.referenceID);
  }

  /**
   *
   * @return
   */
  @Override
  public Reference clone() {

    return new Reference(this.referenceID, this.fullReference, this.volume, 
            this.issue, this.page, this.year, this.doi, this.format, 
            this.documentsCount);
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

    Reference item = (Reference)obj;
    
    return this.referenceID.equals(item.referenceID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.referenceID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
