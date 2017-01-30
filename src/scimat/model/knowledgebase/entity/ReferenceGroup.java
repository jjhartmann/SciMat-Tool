/*
 * ReferenceGroup.java
 *
 * Created on 21-oct-2010, 17:48:58
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class ReferenceGroup implements Serializable, Comparable<ReferenceGroup>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceGroupID;

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
   * @param referenceGroupID
   * @param groupName
   * @param stopGroup
   */
  public ReferenceGroup(Integer referenceGroupID, String groupName,
          boolean stopGroup, int itemsCount, int documentsCount) {

    this.referenceGroupID = referenceGroupID;
    this.groupName = groupName;
    this.stopGroup = stopGroup;
    this.itemsCount = itemsCount;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idReferenceGroup
   */
  public Integer getReferenceGroupID() {
    return referenceGroupID;
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
    result += "idReferenceGroup: " + this.referenceGroupID + ", ";
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
  public int compareTo(ReferenceGroup o) {
    return this.referenceGroupID.compareTo(o.referenceGroupID);
  }

  /**
   *
   * @return
   */
  @Override
  public ReferenceGroup clone() {

    return new ReferenceGroup(this.referenceGroupID, this.groupName, 
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

    ReferenceGroup item = (ReferenceGroup)obj;
    
    return this.referenceGroupID.equals(item.referenceGroupID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.referenceGroupID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
