/*
 * Journal.java
 *
 * Created on 21-oct-2010, 17:49:15
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class Journal implements Serializable, Comparable<Journal>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer journalID;

  /**
   *
   */
  private String source;

  /**
   * 
   */
  private String conferenceInformation;
  private int documentsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param journalID
   * @param source
   * @param conferenceInformation
   */
  public Journal(Integer journalID, String source, String conferenceInformation, int documentsCount) {

    this.journalID = journalID;
    this.source = source;
    this.conferenceInformation = conferenceInformation;
    this.documentsCount = documentsCount;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idJournal
   */
  public Integer getJournalID() {
    return journalID;
  }

  /**
   * @return the source
   */
  public String getSource() {
    return source;
  }

  /**
   * @return the conferenceInformation
   */
  public String getConferenceInformation() {
    return conferenceInformation;
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
    result += "idJournal: " + this.journalID + ", ";
    result += "source: " + this.source + ", ";
    result += "conferenceInformation: " + this.conferenceInformation + ", ";
    result += "documentsCount: " + this.documentsCount ;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(Journal o) {
    return this.journalID.compareTo(o.journalID);
  }

  /**
   *
   * @return
   */
  @Override
  public Journal clone() {

    return new Journal(this.journalID, this.source, this.conferenceInformation, this.documentsCount);
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

    Journal item = (Journal)obj;
    
    return this.journalID.equals(item.journalID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.journalID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
