/*
 * DocumentAuthor.java
 *
 * Created on 11-nov-2010, 17:57:20
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author mjcobo
 */
public class DocumentAuthor implements Serializable, Comparable<DocumentAuthor>, Cloneable  {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Document document;

  /**
   * 
   */
  private Author author;

  /**
   *
   */
  private int position;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param document
   * @param author
   * @param position
   */
  public DocumentAuthor(Document document, Author author, int position) {

    this.author = author;
    this.document = document;
    this.position = position;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @return the position
   */
  public int getPosition() {
    return this.position;
  }

  /**
   *
   * @return
   */
  public Author getAuthor() {
    return this.author;
  }

  /**
   * 
   * @return
   */
  public Document getDocument() {
    return this.document;
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
    result += "Author: " + this.author + ", ";
    result += "position: " + this.position;
    result += ")";
    
    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(DocumentAuthor o) {
    
    int result;

    // First compare the document. If the documents are the same,
    // compare the DocumentAuthor

    result = this.document.compareTo(o.document);

    if (result == 0) {

      result = this.author.compareTo(o.author);
    }

    return result;
  }

  /**
   *
   * @return
   */
  @Override
  protected DocumentAuthor clone() {

    return new DocumentAuthor(document, author, position);
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

    DocumentAuthor item = (DocumentAuthor)obj;
    
    return this.document.equals(item.document) && 
           this.author.equals(item.author);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(87, 3).append(this.document).append(this.author).toHashCode();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
