/*
 * ClusterNode.java
 *
 * Created on 15-feb-2011, 20:06:20
 */
package scimat.api.mapping;

import java.io.Serializable;
import scimat.api.utils.property.PropertySet;

/**
 *
 * @author mjcobo
 */
public class Node implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The node's identifier.
   */
  private Integer nodeID;

  /**
   * The set of properties.
   */
  private PropertySet properties;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param nodeID
   */
  public Node(Integer nodeID) {
    this.nodeID = nodeID;
    this.properties = new PropertySet();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the nodeID
   */
  public Integer getNodeID() {
    return nodeID;
  }

  /**
   * @return the properties
   */
  public PropertySet getProperties() {
    return properties;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
