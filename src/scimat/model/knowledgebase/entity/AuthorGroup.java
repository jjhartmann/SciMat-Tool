/*
 * AuthorGroup.java
 *
 * Created on 21-oct-2010, 17:48:36
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 * <p>This class represents a set of <code>Author<code> that represent the same
 * author.</p>
 * 
 * <p>Each <code>AuthorGroup</code> has one single one identifier, a name and
 * a boolean field which indicates if the group is a stop group. A value of
 * true indicates that it is a group that has no meaning and therefore should
 * not be taken into account in the analysis.<p>
 *
 * @author mjcobo
 */
public class AuthorGroup implements Serializable, Comparable<AuthorGroup>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer authorGroupID;

  /**
   * 
   */
  private String groupName;

  /**
   * 
   */
  private boolean stopGroup;
  private int itemsCount;
  private int documentsCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorGroupID
   * @param authorName
   * @param stopGroup
   */
  public AuthorGroup(Integer authorGroupID, String authorName,
          boolean stopGroup, int itemsCount, int documentsCount) {
    
    this.authorGroupID = authorGroupID;
    this.groupName = authorName;
    this.stopGroup = stopGroup;
    this.itemsCount = itemsCount;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the AuthorGroup's ID
   */
  public Integer getAuthorGroupID() {
    return this.authorGroupID;
  }

  /**
   * @return the groupName
   */
  public String getGroupName() {
    return this.groupName;
  }

  /**
   * @return the stopGroup
   */
  public boolean isStopGroup() {
    return this.stopGroup;
  }

  /**
   * @return the itemsCount
   */
  public int getItemsCount() {
    return itemsCount;
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
    result += "idAuthorGroupID: " + this.authorGroupID + ", ";
    result += "groupName: " + this.groupName + ", ";
    result += "stopGroup: " + this.stopGroup + ", ";
    result += "itemsCount: " + this.itemsCount + ", ";
    result += "documentsCount: " + this.documentsCount;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(AuthorGroup o) {
    return this.authorGroupID.compareTo(o.authorGroupID);
  }

  /**
   *
   * @return
   */
  @Override
  protected AuthorGroup clone() {

    return new AuthorGroup(authorGroupID, groupName, stopGroup, this.itemsCount, this.documentsCount);
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

    AuthorGroup item = (AuthorGroup)obj;
    
    return this.authorGroupID.equals(item.authorGroupID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.authorGroupID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
