/*
 * Property.java
 *
 * Created on 15-feb-2011, 18:54:14
 */
package scimat.api.utils.property;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public abstract class Property<T> implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private T value;

  /**
   * 
   */
  private PropertyTypes type;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param type
   */
  public Property(T value, PropertyTypes type) {
    
    this.value = value;
    this.type = type;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public PropertyTypes getType() {

    return this.type;
  }

  /**
   * Return the value of the property.
   * @return
   */
  public T getValue() {
    return value;
  }

  /**
   * 
   * @return
   */
  @Override
  public String toString() {
    
    return String.valueOf(this.value);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
