/*
 * SubjectCategory.java
 *
 * Created on 21-oct-2010, 17:49:07
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class SubjectCategory implements Serializable, Comparable<SubjectCategory>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer subjectCategoryID;

  /**
   * 
   */
  private String subjectCategoryName;
  private int journalsCount;
  private int documentsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param subjectCategoryID
   * @param subjectCategoryName
   */
  public SubjectCategory(Integer subjectCategoryID, String subjectCategoryName, int journalsCount, int documentsCount) {
    
    this.subjectCategoryID = subjectCategoryID;
    this.subjectCategoryName = subjectCategoryName;
    this.journalsCount = journalsCount;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idSubjectCategory
   */
  public Integer getSubjectCategoryID() {
    return this.subjectCategoryID;
  }

  /**
   * @return the subjectCategoryName
   */
  public String getSubjectCategoryName() {
    return subjectCategoryName;
  }

  /**
   * @return the journalsCount
   */
  public int getJournalsCount() {
    return journalsCount;
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
    result += "idSubjectCategory: " + this.subjectCategoryID + ", ";
    result += "subjectCategoryName: " + this.subjectCategoryName + ", ";
    result += "journalsCount: " + this.journalsCount + ", ";
    result += "documentsCount: " + this.documentsCount;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(SubjectCategory o) {
    return this.subjectCategoryID.compareTo(o.subjectCategoryID);
  }

  /**
   *
   * @return
   */
  @Override
  public SubjectCategory clone() {

    return new SubjectCategory(this.subjectCategoryID, this.subjectCategoryName, 
            this.journalsCount, this.documentsCount);
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

    SubjectCategory item = (SubjectCategory)obj;
    
    return this.subjectCategoryID.equals(item.subjectCategoryID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.subjectCategoryID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
