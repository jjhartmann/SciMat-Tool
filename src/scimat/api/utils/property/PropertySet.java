/*
 * PropertySet.java
 *
 * Created on 15-feb-2011, 18:54:03
 */
package scimat.api.utils.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author mjcobo
 */
public class PropertySet implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private TreeMap<String, Property> properties = new TreeMap<String, Property>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public PropertySet() {
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param key
   * @param property
   * @throws DuplicatePropertyException
   */
  public boolean addProperty(String key, Property property) {

    if (! this.properties.containsKey(key)) {

      this.properties.put(key, property);

      return true;

    } else {

      return false;
      
      /*throw new DuplicatePropertyException("The properties set contains yet "
              + "a property with the same key.");*/
    }
  }

  /**
   * 
   * @param key
   * @return
   */
  public Property getProperty(String key) {

    return this.properties.get(key);
  }

  /**
   * 
   * @return
   */
  public ArrayList<String> getKeyList() {

    return new ArrayList<String>(this.properties.keySet());
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
