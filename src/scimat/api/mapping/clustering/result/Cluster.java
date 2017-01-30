/*
 * Cluster.java
 *
 * Created on 15-feb-2011, 19:51:41
 */
package scimat.api.mapping.clustering.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;
import scimat.api.utils.property.PropertySet;

/**
 *
 * @author mjcobo
 */
public class Cluster implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private TreeSet<Integer> nodesList;

  /**
   * The principal node of the cluster. If it is null, the cluster does not have
   * a principal node.
   */
  private Integer mainNode;

  /**
   * 
   */
  private PropertySet properties;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param nodes
   */
  public Cluster(ArrayList<Integer> nodes, Integer mainNode) {

    this.nodesList = new TreeSet<Integer>(nodes);
    this.mainNode = mainNode;
    this.properties = new PropertySet();
  }

  /**
   * 
   * @param nodes
   */
  public Cluster(ArrayList<Integer> nodes) {

    this.nodesList = new TreeSet<Integer>(nodes);
    this.mainNode = null;
    this.properties = new PropertySet();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public ArrayList<Integer> getNodes() {

    return new ArrayList<Integer>(this.nodesList);
  }

  /**
   * 
   */
  public int getNodesCount() {

    return this.nodesList.size();
  }

  /**
   * The main node or null if the cluster does not have a main node.
   * 
   * @return the main node or null the cluster does not have a main node.
   */
  public Integer getMainNode() {

    return this.mainNode;
  }

  /**
   * 
   * @return
   */
  public PropertySet getProperties() {
    return properties;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
