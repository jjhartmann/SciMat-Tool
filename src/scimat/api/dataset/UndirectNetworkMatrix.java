/*
 * UndirectNetworkMatrix.java
 *
 * Created on 08-feb-2011, 14:10:56
 */
package scimat.api.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class respreents an undirect network built from a dataset. The network
 * can be based on co-occurrence data or on coupling data. Each edge has a value.
 *
 * The networlk is represented as an adjacency matrix. Particularly, the matrix
 * is a square sparse matrix. Due to the adjacency matrix of an undirect graph
 * is symmetric, the value of the edge i-j will be the same that the edge j-i.
 * 
 * @author mjcobo
 */
public class UndirectNetworkMatrix implements Serializable, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private TreeMap<Integer, TreeMap<Integer, Double>> matrix;
  private TreeSet<Integer> nodesList;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Constructs a new {@code UndirectNetworkMatrix} empty.
   *
   * @param nodesList A list with the nodes' IDs.
   */
  public UndirectNetworkMatrix(ArrayList<Integer> nodesList) {

    // Build a square matrix.
    this.matrix = new TreeMap<Integer, TreeMap<Integer, Double>>();

    this.nodesList = new TreeSet<Integer>(nodesList);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Set the matrix cell at ccordinate [nodeSource,nodeTarget] to a specific value.
   *
   * NOTE: due to the matrix is symmetric, this method will set the same value
   * in both [nodeSource,nodeTarget] and [nodeTarget,nodeSource] cells.
   *
   * @param nodeSource The nodeA's ID.
   * @param nodeTarget The nodeB's ID.
   * @param value The value to be set.
   *
   * @return true if the edge was correctly added
   */
  public boolean addEdge(Integer nodeSource, Integer nodeTarget, double value) {

    if (this.nodesList.contains(nodeSource) && this.nodesList.contains(nodeTarget)) {

      addSingleEdge(nodeSource, nodeTarget, value);
      addSingleEdge(nodeTarget, nodeSource, value);

      return true;

    } else {

      return false;
    }
  }

  /**
   * Set the matrix cell at ccordinate [nodeSource,nodeTarget] to a 0 value. 
   * This means, that the edge between the nodeA and nodeB is deleted.
   *
   * NOTE: due to the matrix is symmetric, this method will set the same value
   * in both [nodeSource,nodeTarget] and [nodeTarget,nodeSource] cells.
   * 
   * @param nodeSource The nodeA's ID.
   * @param nodeTarget The nodeB's ID.
   * @throws IndexOutOfBoundsException If the nodes do not exist in the network.
   */
  public boolean removeEdge(Integer nodeSource, Integer nodeTarget) {

    if (this.nodesList.contains(nodeSource) && this.nodesList.contains(nodeTarget)) {

      // The value returned for the two calls are the same.
      removeSingleEdge(nodeSource, nodeTarget);
      return removeSingleEdge(nodeTarget, nodeSource);

    } else {

      return false;
    }
  }

  /**
   * Return the value stored in the position [nodeA,nodeB]
   * 
   * @param nodeSource The nodeA's ID.
   * @param nodeTarget The nodeB's ID.
   *
   * @return the value at the position [nodeSource,nodeTarget] or null if the
   * edge does not exist.
   */
  public double getEdge(Integer nodeSource, Integer nodeTarget) {

    TreeMap<Integer, Double> tmp;
    Double value;

    tmp = this.matrix.get(nodeSource);

    if (tmp != null) {

      value = tmp.get(nodeTarget);

      if (value != null) {

        return value;

      } else {

        return 0.0;
      }
      
    } else {

      return 0.0;
    }
  }

  /**
   * Return a list with the {@link NetworkPairItem} of this network. Due to
   * fact that the matrix is symmetric, the pairs returned will be only those one
   * of the superior triangular network.
   *
   * @return a list with the {@link NetworkPairItem} of this network.
   */
  public ArrayList<NetworkPair> getNetworkPairs() {

    Integer firstElementID, secondElementID;
    SortedMap<Integer, Double> tmpMap;
    Iterator<Integer> rowIterator, columnIterator;
    ArrayList<NetworkPair> pairList;

    pairList = new ArrayList<NetworkPair>();

    rowIterator = this.matrix.keySet().iterator();

    while (rowIterator.hasNext()) {

      firstElementID = rowIterator.next();

      tmpMap = this.matrix.get(firstElementID).tailMap(firstElementID, false);

      columnIterator = tmpMap.keySet().iterator();

      while (columnIterator.hasNext()) {

        secondElementID = columnIterator.next();

        pairList.add(new NetworkPair(firstElementID, secondElementID, tmpMap.get(secondElementID)));

      }
    }

    return pairList;
  }

  /**
   *
   * @param nodes
   * @return
   */
  public ArrayList<NetworkPair> getInternalPairs(ArrayList<Integer> nodes) {

    int i;
    Integer nodeSourceID, nodeTargetID;
    TreeMap<Integer, Double> tmpMap;
    TreeSet<Integer> nodesToDelete;
    TreeSet<Integer> possiblePair;
    ArrayList<NetworkPair> pairs = new ArrayList<NetworkPair>();
    Iterator<Integer> iterator;

    nodesToDelete = new TreeSet<Integer>();

    // For each node in the list:
    for (i = 0; i < nodes.size(); i++) {

      // Get the ID of the first element.
      nodeSourceID = nodes.get(i);

      // Get the neighbours of the first element.
      tmpMap = this.matrix.get(nodeSourceID);

      // Add the first element as node to delete. In this way, we will only check
      // the neighbours that have not been add as pairs.
      nodesToDelete.add(nodeSourceID);

      // The possible pairs are the neighbours of the first element that belong
      // to the node list passed as parameter and have not been yet added.
      possiblePair = new TreeSet<Integer>(tmpMap.keySet());

      // Keep only the nodes contained in the list.
      possiblePair.retainAll(nodes);

      // Keep only the nodes that have not been yet analyzed.
      possiblePair.removeAll(nodesToDelete);

      // For each correct neighbours, we create the NetworkPair.
      iterator = possiblePair.iterator();

      while (iterator.hasNext()) {

        nodeTargetID = iterator.next();

        pairs.add(new NetworkPair(nodeSourceID, nodeTargetID, tmpMap.get(nodeTargetID)));
      }
    }

    return pairs;
  }

  /**
   *
   * @param nodesList
   * @return
   */
  public ArrayList<NetworkPair> getExternalPairs(ArrayList<Integer> nodes) {

    int i;
    Integer nodeSourceID, nodeTargetID;
    TreeMap<Integer, Double> tmpMap;
    TreeSet<Integer> possiblePair;
    ArrayList<NetworkPair> pairs = new ArrayList<NetworkPair>();
    Iterator<Integer> iterator;

    // For each node in the list:
    for (i = 0; i < nodes.size(); i++) {

      // Get the ID of the first element.
      nodeSourceID = nodes.get(i);

      // Get the neighbours of the first element.
      tmpMap = this.matrix.get(nodeSourceID);

      // The possible pairs are the neighbours of the first element that do not 
      // belong to the node list passed as parameter.
      possiblePair = new TreeSet<Integer>(tmpMap.keySet());

      // Keep only the nodes non-contained in the list.
      possiblePair.removeAll(nodes);

      // For each correct neighbours, we create the NetworkPair.
      iterator = possiblePair.iterator();

      while (iterator.hasNext()) {

        nodeTargetID = iterator.next();

        pairs.add(new NetworkPair(nodeSourceID, nodeTargetID, tmpMap.get(nodeTargetID)));
      }
    }

    return pairs;

  }
  
  /**
   * Return a list with the pairs between the nodes of the two clusters.
   *
   * @param cluster1 the position of the first cluster in the cluster set.
   * @param cluster2 the position of the second cluster in the cluster set.
   *
   * @return the list with the pairs between the two cluster.
   */
  public ArrayList<NetworkPair> getIntraNodesPairs(ArrayList<Integer> nodesSource, 
          ArrayList<Integer> nodesTarget) {

    int i, j;
    Integer nodeCluster1ID, nodeCluster2ID;
    Double value;
    ArrayList<NetworkPair> pairs;
    
    pairs = new ArrayList<NetworkPair>();

    for (i = 0; i < nodesSource.size(); i++) {

      nodeCluster1ID = nodesSource.get(i);

      for (j = 0; j < nodesTarget.size(); j++) {

        nodeCluster2ID = nodesTarget.get(j);

        //value = getEdge(nodeCluster1ID, nodeCluster2ID);

        if (containsEdge(nodeCluster1ID, nodeCluster2ID)) {

          pairs.add(new NetworkPair(nodeCluster1ID, nodeCluster2ID, getEdge(nodeCluster1ID, nodeCluster2ID)));
        }
      }
    }

    return pairs;
  }

  /**
   * Return the number of nodes of the network.
   *
   * @return the number of nodes.
   */
  public int getNodesCount() {

    return this.nodesList.size();
  }

  /**
   * Return an array with the nodes' IDs of the network.
   * @return
   */
  public ArrayList<Integer> getNodes() {

    return new ArrayList<Integer>(this.nodesList);
  }

  /**
   * Returns true if the network contains this node.
   *
   * @param nodeID The node identifier.
   *
   * @return true if the networks contains the node.
   */
  public boolean containsNode(Integer nodeID) {

    return this.nodesList.contains(nodeID);
  }

  /**
   * 
   * @param nodeSource
   * @param nodeTarget
   * @return
   */
  public boolean containsEdge(Integer nodeSource, Integer nodeTarget) {

    TreeMap<Integer, Double> tmpMap;

    tmpMap = this.matrix.get(nodeSource);

    if (tmpMap != null) {

      return tmpMap.containsKey(nodeTarget);

    } else {

      return false;
    }
  }

  /**
   * 
   * @param node
   * @return
   */
  public ArrayList<Integer> getNeighbours(Integer node) {

    if (this.matrix.containsKey(node)) {

      return new ArrayList<Integer>(this.matrix.get(node).keySet());
      
    } else {

      return new ArrayList<Integer>();
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * Add an edge in only one direction.
   * 
   * @param nodeSource
   * @param nodeTarget
   * @param value
   */
  private void addSingleEdge(Integer nodeSource, Integer nodeTarget, double value) {

    TreeMap<Integer, Double> tmp;

    tmp = this.matrix.get(nodeSource);

    if (tmp == null) {

      tmp = new TreeMap<Integer, Double>();

      this.matrix.put(nodeSource, tmp);
    }

    tmp.put(nodeTarget, value);
  }

  /**
   *
   * @param nodeSource
   * @param nodeTarget
   */
  private boolean removeSingleEdge(Integer nodeSource, Integer nodeTarget) {

    boolean flag;
    TreeMap<Integer, Double> tmp;

    tmp = this.matrix.get(nodeSource);

    if (tmp != null) {

      if (tmp.remove(nodeTarget) != null) {

        flag = true;

      } else {

        flag = false;
      }

      // If after removing the nodeSource is not linked with any node, we drop
      // the nodeSource from the matrix.
      if (tmp.isEmpty()) {

        this.matrix.remove(nodeSource);
      }

    } else {

      flag = false;
    }

    return flag;
  }

  /**
   * 
   * @return
   * @throws CloneNotSupportedException
   */
  @Override
  protected UndirectNetworkMatrix clone() throws CloneNotSupportedException {

    UndirectNetworkMatrix undirectNetworkMatrix;

    undirectNetworkMatrix = new UndirectNetworkMatrix(new ArrayList<Integer>(this.nodesList));
    undirectNetworkMatrix.matrix = new TreeMap<Integer, TreeMap<Integer, Double>>(this.matrix);

    return undirectNetworkMatrix;
  }

}
