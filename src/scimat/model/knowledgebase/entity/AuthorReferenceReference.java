/*
 * AuthorReferenceReference.java
 *
 * Created on 11-nov-2010, 18:07:30
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceReference implements Serializable, Comparable<AuthorReferenceReference>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private AuthorReference authorReference;
  private Reference reference;

  /**
   *
   */
  private int position;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param authorReference
   * @param reference
   * @param position
   */
  public AuthorReferenceReference(AuthorReference authorReference,
          Reference reference, int position) {

    this.authorReference = authorReference;
    this.reference = reference;
    this.position = position;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the authorReference
   */
  public AuthorReference getAuthorReference() {
    return authorReference;
  }

  /**
   * @return the reference
   */
  public Reference getReference() {
    return reference;
  }

  /**
   * @return the position
   */
  public int getPosition() {
    return this.position;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "AuthorReference: " + this.authorReference + ", ";
    result += "Reference: " + this.reference + ", ";
    result += "position: " + this.position;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(AuthorReferenceReference o) {
    
    int result;

    // First compare the reference. If the references are the same,
    // compare the authorReference

    result = this.reference.compareTo(o.reference);

    if (result == 0) {

      result = this.authorReference.compareTo(o.authorReference);
    }

    return result;
  }

  /**
   *
   * @return
   */
  @Override
  protected AuthorReferenceReference clone() {

    return new AuthorReferenceReference(authorReference, reference, position);
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

    AuthorReferenceReference item = (AuthorReferenceReference)obj;
    
    return this.reference.equals(item.reference) && 
           this.authorReference.equals(item.authorReference);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(87, 3).append(this.reference).append(this.authorReference).toHashCode();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
