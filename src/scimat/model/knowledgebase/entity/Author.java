/*
 * Author.java
 *
 * Created on 21-oct-2010, 17:48:42
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class Author implements Serializable, Comparable<Author>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The author's ID
   */
  private Integer authorID;

  /**
   * The author's name.
   */
  private String authorName;

  /**
   * The author's full name when available
   */
  private String fullAuthorName;
  private int documentsCount;
  private int affiliationsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorID
   * @param authorName
   * @param fullAuthorName
   */
  public Author(Integer authorID, String authorName, String fullAuthorName, int documentsCount, int affiliationsCount) {

    this.authorID = authorID;
    this.authorName = authorName;
    this.fullAuthorName = fullAuthorName;
    this.documentsCount = documentsCount;
    this.affiliationsCount = affiliationsCount;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the author's ID
   */
  public Integer getAuthorID() {
    return this.authorID;
  }

  /**
   * @return the authorName
   */
  public String getAuthorName() {
    return this.authorName;
  }

  /**
   * @return the fullAuthorName
   */
  public String getFullAuthorName() {
    return this.fullAuthorName;
  }

  /**
   * @return the documentsCount
   */
  public int getDocumentsCount() {
    return documentsCount;
  }

  /**
   * @return the affiliationsCount
   */
  public int getAffiliationsCount() {
    return affiliationsCount;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "idAuthor: " + this.authorID + ", ";
    result += "authorName: " + this.authorName + ", ";
    result += "fullAuthorName: " + this.fullAuthorName + ", ";
    result += "documentsCount: " + this.documentsCount + ", ";
    result += "affiliationsCount: " + this.affiliationsCount;
    result += ")";

    return result;
  }

  

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(Author o) {
    return this.authorID.compareTo(o.authorID);
  }
  

  /**
   *
   * @return
   */
  @Override
  protected Author clone() {

    return new Author(this.authorID, this.authorName, this.fullAuthorName, this.documentsCount, this.affiliationsCount);
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

    Author item = (Author)obj;
    
    return this.authorID.equals(item.authorID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.authorID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
