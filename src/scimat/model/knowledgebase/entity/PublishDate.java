/*
 * PublishDate.java
 *
 * Created on 21-oct-2010, 17:49:37
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class PublishDate implements Serializable, Comparable<PublishDate>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer publishDateID;

  /**
   *
   */
  private String year;

  /**
   * 
   */
  private String date;
  private int documentsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param publishDateID
   * @param year
   * @param date
   */
  public PublishDate(Integer publishDateID, String year, String date, int documentsCount) {

    this.publishDateID = publishDateID;
    this.year = year;
    this.date = date;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the publishDateID
   */
  public Integer getPublishDateID() {
    return publishDateID;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @return the date
   */
  public String getDate() {
    return date;
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
    result += "idPublishDate: " + this.publishDateID + ", ";
    result += "year: " + this.year + ", ";
    result += "date: " + this.date + ", ";
    result += "documentsCount: " + this.documentsCount;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(PublishDate o) {
    return this.publishDateID.compareTo(o.publishDateID);
  }

  /**
   *
   * @return
   */
  @Override
  public PublishDate clone() {

    return new PublishDate(this.publishDateID, this.year, this.date, this.documentsCount);
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

    PublishDate item = (PublishDate)obj;
    
    return this.publishDateID.equals(item.publishDateID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.publishDateID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
