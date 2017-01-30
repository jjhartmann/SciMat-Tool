/*
 * DocumentWord.java
 *
 * Created on 11-nov-2010, 18:07:41
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 *
 * @author mjcobo
 */
public class DocumentWord implements Serializable, Comparable<DocumentWord>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Document document;
  private Word word;

  /**
   *
   */
  private boolean authorKeyword;

  /**
   *
   */
  private boolean sourceKeyword;

  /**
   *
   */
  private boolean addedKeyword;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param document
   * @param word
   * @param authorKeyword
   * @param sourceKeyword
   * @param addedKeyword
   */
  public DocumentWord(Document document, Word word, boolean authorKeyword, boolean sourceKeyword,
          boolean addedKeyword) {

    this.document = document;
    this.word = word;
    this.authorKeyword = authorKeyword;
    this.sourceKeyword = sourceKeyword;
    this.addedKeyword = addedKeyword;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @return
   */
  public Document getDocument() {
    return document;
  }

  /**
   * 
   * @return
   */
  public Word getWord() {
    return word;
  }

  /**
   * @return the authorKeyword
   */
  public boolean isAuthorKeyword() {
    return authorKeyword;
  }

  /**
   * @return the sourceKeyword
   */
  public boolean isSourceKeyword() {
    return sourceKeyword;
  }

  /**
   * @return the addedKeyword
   */
  public boolean isAddedKeyword() {
    return addedKeyword;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "Document: " + this.document + ", ";
    result += "Word: " + this.word + ", ";
    result += "authorKeyword: " + this.authorKeyword + ", ";
    result += "sourceKeyword: " + this.sourceKeyword + ", ";
    result += "addedKeyword: " + this.addedKeyword;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(DocumentWord o) {
    
    int result;

    // First compare the document. If the documents are the same,
    // compare the DocumentWord

    result = this.document.compareTo(o.document);

    if (result == 0) {

      result = this.word.compareTo(o.word);
    }

    return result;
  }  

  /**
   *
   * @return
   */
  @Override
  protected DocumentWord clone() {

    return new DocumentWord(document, word, authorKeyword, sourceKeyword, addedKeyword);
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

    DocumentWord item = (DocumentWord)obj;
    
    return this.document.equals(item.document) && 
           this.word.equals(item.word);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(87, 3).append(this.document).append(this.word).toHashCode();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
