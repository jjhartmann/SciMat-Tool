/*
 * AuthorReferenceGroup.java
 *
 * Created on 04-nov-2010, 13:44:07
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroup implements Serializable, Comparable<AuthorReferenceGroup>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer authorReferenceGroupID;

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
   * @param AuthorReferenceGroupID
   * @param groupName
   * @param stopGroup
   */
  public AuthorReferenceGroup(Integer AuthorReferenceGroupID, String groupName,
          boolean stopGroup, int itemsCount, int documentsCount) {
    
    this.authorReferenceGroupID = AuthorReferenceGroupID;
    this.groupName = groupName;
    this.stopGroup = stopGroup;
    this.itemsCount = itemsCount;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idAuthorReferenceGroup
   */
  public Integer getAuthorReferenceGroupID() {
    return authorReferenceGroupID;
  }

  /**
   * @return the groupName
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * @return the stopGroup
   */
  public boolean isStopGroup() {
    return stopGroup;
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
    result += "idAuthorReferenceGroup: " + this.authorReferenceGroupID + ", ";
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
  public int compareTo(AuthorReferenceGroup o) {
    return this.authorReferenceGroupID.compareTo(o.authorReferenceGroupID);
  }

  /**
   *
   * @return
   */
  @Override
  protected AuthorReferenceGroup clone() {

    return new AuthorReferenceGroup(this.authorReferenceGroupID, this.groupName, this.stopGroup, this.itemsCount, this.documentsCount);
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

    AuthorReferenceGroup item = (AuthorReferenceGroup)obj;
    
    return this.authorReferenceGroupID.equals(item.authorReferenceGroupID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.authorReferenceGroupID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
