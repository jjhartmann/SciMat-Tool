/*
 * Period.java
 *
 * Created on 21-oct-2010, 17:49:30
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class Period implements Serializable, Comparable<Period>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer periodID;

  /**
   * 
   */
  private String name;

  private int position;
  private int publishDatesCount;
  private int documentsCount;
  
  private static final long serialVersionUID = -7912338566411582540L;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param periodID
   * @param name
   * @param position
   * @param publishDatesCount
   * @param documentsCount 
   */
  public Period(Integer periodID, String name, int position, int publishDatesCount, int documentsCount) {
    this.periodID = periodID;
    this.name = name;
    this.position = position;
    this.publishDatesCount = publishDatesCount;
    this.documentsCount = documentsCount;
  }
  
  /**
   * 
   * @param periodID
   * @param name
   * @param position 
   */
  public Period(Integer periodID, String name, int position) {
    this.periodID = periodID;
    this.name = name;
    this.position = position;
    this.publishDatesCount = 0;
    this.documentsCount = 0;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idPeriod
   */
  public Integer getPeriodID() {
    return periodID;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * 
   * @return
   */
  public int getPosition() {
    return position;
  }

  /**
   * @return the publishDatesCount
   */
  public int getPublishDatesCount() {
    return publishDatesCount;
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
    result += "idPeriod: " + this.periodID + ", ";
    result += "name: " + this.name + ", ";
    result += "position: " + this.position + ", ";
    result += "publishDatesCount: " + this.publishDatesCount + ", ";
    result += "documentsCount: " + this.documentsCount;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(Period o) {
    return this.periodID.compareTo(o.periodID);
  }

  /**
   *
   * @return
   */
  @Override
  public Period clone() {

    return new Period(this.periodID, this.name, this.position, this.publishDatesCount, this.documentsCount);
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

    Period item = (Period)obj;
    
    return this.periodID.equals(item.periodID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.periodID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
