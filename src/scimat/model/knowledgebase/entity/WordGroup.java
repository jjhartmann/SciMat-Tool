/*
 * WordGroup.java
 *
 * Created on 21-oct-2010, 17:48:16
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class WordGroup implements Serializable, Comparable<WordGroup>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer wordGroupID;

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
   * @param wordGroupID
   * @param groupName
   * @param stopGroup
   * @param kbm
   */
  public WordGroup(Integer wordGroupID, String groupName, boolean stopGroup, int itemsCount, int documentsCount) {
    
    this.wordGroupID = wordGroupID;
    this.groupName = groupName;
    this.stopGroup = stopGroup;
    this.itemsCount = itemsCount;
    this.documentsCount = documentsCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idWordGroup
   */
  public Integer getWordGroupID() {
    return this.wordGroupID;
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
    result += "idWordGroup: " + this.wordGroupID + ", ";
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
  public int compareTo(WordGroup o) {
    return this.wordGroupID.compareTo(o.wordGroupID);
  }

  /**
   *
   * @return
   */
  @Override
  protected WordGroup clone() {
    return new WordGroup(this.wordGroupID, this.groupName, this.stopGroup, 
            this.itemsCount, this.documentsCount);
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

    WordGroup item = (WordGroup)obj;
    
    return this.wordGroupID.equals(item.wordGroupID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.wordGroupID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
