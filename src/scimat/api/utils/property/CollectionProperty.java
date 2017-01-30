/*
 * CollectionProperty.java
 *
 * Created on 15-feb-2011, 19:07:53
 */
package scimat.api.utils.property;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author mjcobo
 */
public class CollectionProperty<T> extends Property<Collection<T>> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param elementsList
   */
  public CollectionProperty(Collection<T> collection) {
    super(collection, PropertyTypes.Collection);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
