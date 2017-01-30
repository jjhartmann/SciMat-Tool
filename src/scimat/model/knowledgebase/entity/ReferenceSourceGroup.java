/*
 * ReferenceSourceGroup.java
 *
 * Created on 28-oct-2010, 20:10:57
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceGroup implements Serializable, Comparable<ReferenceSourceGroup>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceSourceGroupID;

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
   * @param referenceSourceGroupID
   * @param groupName
   * @param stopGroup
   */
  public ReferenceSourceGroup(Integer referenceSourceGroupID, String groupName,
          boolean stopGroup, int itemsCount, int documentsCount) {
    
    this.referenceSourceGroupID = referenceSourceGroupID;
    this.groupName = groupName;
    this.stopGroup = stopGroup;
    this.itemsCount = itemsCount;
    this.documentsCount = documentsCount;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idReferenceSourceGroup
   */
  public Integer getReferenceSourceGroupID() {
    return referenceSourceGroupID;
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
    result += "idReferenceSourceGroup: " + this.referenceSourceGroupID + ", ";
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
  public int compareTo(ReferenceSourceGroup o) {

    return this.referenceSourceGroupID.compareTo(o.referenceSourceGroupID);
  }

  /**
   *
   * @return
   */
  @Override
  public ReferenceSourceGroup clone() {

    return new ReferenceSourceGroup(this.referenceSourceGroupID, this.groupName, 
            this.stopGroup, this.itemsCount, this.documentsCount);
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

    ReferenceSourceGroup item = (ReferenceSourceGroup)obj;
    
    return this.referenceSourceGroupID.equals(item.referenceSourceGroupID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.referenceSourceGroupID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
