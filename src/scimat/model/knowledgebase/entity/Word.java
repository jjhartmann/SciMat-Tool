/*
 * Word.java
 *
 * Created on 21-oct-2010, 17:49:44
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class Word implements Serializable, Comparable<Word>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer wordID;

  /**
   *
   */
  private String wordName;
  private int documentsCount;
  private int roleAuthorsCount;
  private int roleSourcesCount;
  private int roleAddedCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param wordID
   * @param wordName
   * @param documentsCount
   */

  public Word(Integer wordID, String wordName, int documentsCount, int roleAuthorsCount, int roleSourcesCount, int roleAddedCount) {
    
    this.wordID = wordID;
    this.wordName = wordName;
    this.documentsCount = documentsCount;
    this.roleAuthorsCount = roleAuthorsCount;
    this.roleSourcesCount = roleSourcesCount;
    this.roleAddedCount = roleAddedCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idWord
   */
  public Integer getWordID() {
    return wordID;
  }

  /**
   * @return the word
   */
  public String getWordName() {
    return wordName;
  }

  /**
   * @return the documentsCount
   */
  public int getDocumentsCount() {
    return documentsCount;
  }

  /**
   * @return the roleAuthorsCount
   */
  public int getRoleAuthorsCount() {
    return roleAuthorsCount;
  }

  /**
   * @return the roleSourcesCount
   */
  public int getRoleSourcesCount() {
    return roleSourcesCount;
  }

  /**
   * @return the roleAddedCount
   */
  public int getRoleAddedCount() {
    return roleAddedCount;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "idWord: " + this.wordID + ", ";
    result += "wordName: " + this.wordName + ", ";
    result += "documensCount: " + this.documentsCount + ", ";
    result += "roleAuthorsCount: " + this.roleAuthorsCount + ", ";
    result += "roleSourceCount: " + this.roleSourcesCount + ", ";
    result += "roleAddedCount: " + this.roleAddedCount;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(Word o) {
    return this.wordID.compareTo(o.wordID);
  }

  /**
   * 
   * @return
   */
  @Override
  protected Word clone() {
    return new Word(this.wordID, 
            this.wordName, 
            this.documentsCount, 
            this.roleAuthorsCount, 
            this.roleSourcesCount, 
            this.roleAddedCount);
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

    Word item = (Word)obj;
    
    return this.wordID.equals(item.wordID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.wordID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
