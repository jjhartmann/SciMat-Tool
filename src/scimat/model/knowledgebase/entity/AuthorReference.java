/*
 * AuthorReference.java
 *
 * Created on 04-nov-2010, 13:43:02
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class AuthorReference implements Serializable, Comparable<AuthorReference>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer authorReferenceID;

  /**
   *
   */
  private String authorName;
  private int referencesCount;
  private int documentsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorReferenceID
   * @param authorName
   * @param documentsCount
   */
  public AuthorReference(Integer authorReferenceID, String authorName, int referencesCount, int documentsCount) {

    this.authorReferenceID = authorReferenceID;
    this.authorName = authorName;
    this.referencesCount = referencesCount;
    this.documentsCount = documentsCount;

  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idAuthorReference
   */
  public Integer getAuthorReferenceID() {
    return authorReferenceID;
  }

  /**
   * @return the authorName
   */
  public String getAuthorName() {
    return authorName;
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
    result += "idAuthorReference: " + this.authorReferenceID + ", ";
    result += "authorName: " + this.authorName + ", ";
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
  public int compareTo(AuthorReference o) {
    return this.authorReferenceID.compareTo(o.authorReferenceID);
  }

  /**
   *
   * @return
   */
  @Override
  protected AuthorReference clone() {

    return new AuthorReference(this.authorReferenceID, this.authorName, this.referencesCount, this.documentsCount);
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

    AuthorReference item = (AuthorReference)obj;
    
    return this.authorReferenceID.equals(item.authorReferenceID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.authorReferenceID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
