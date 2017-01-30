/*
 * WholeNetwork.java
 *
 * Created on 17-feb-2011, 18:34:58
 */
package scimat.api.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;
import scimat.api.dataset.NetworkPair;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public class WholeNetwork implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The whole network. This network is an undirect weighted network, and it is
   * represented as an adjacency matrix.
   */
  private UndirectNetworkMatrix network;

  /**
   * The nodes list.
   */
  private TreeMap<Integer, Node> nodeList;



  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param network
   */
  public WholeNetwork(UndirectNetworkMatrix network) {

    int i;
    Integer nodeID;
    ArrayList<Integer> nodes;

    this.network = network;

    this.nodeList = new TreeMap<Integer, Node>();

    nodes = this.network.getNodes();

    for (i = 0; i < nodes.size(); i++) {

      nodeID = nodes.get(i);

      this.nodeList.put(nodeID, new Node(nodeID));
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param nodeID
   * @return
   */
  public Node getNode(Integer nodeID) {

    return this.nodeList.get(nodeID);
  }

  /**
   *
   * @param nodeID
   * @return
   */
  public ArrayList<Node> getNodes() {

    return new ArrayList<Node>(this.nodeList.values());
  }

  /**
   *
   * @param nodeID
   * @return
   */
  public ArrayList<Integer> getNodesID() {

    return new ArrayList<Integer>(this.nodeList.keySet());
  }

  /**
   * 
   * @param nodeID
   * @return
   */
  public boolean containsNode(Integer nodeID) {

    return this.nodeList.containsKey(nodeID);
  }

  /**
   * 
   * @param nodeA
   * @param nodeB
   * @return
   */
  public boolean containsEdge(Integer nodeA, Integer nodeB) {

    return this.network.containsEdge(nodeA, nodeB);
  }

  /**
   *
   * @return the number of nodes.
   */
  public int getNodesCount() {

    return this.network.getNodesCount();
  }

  /**
   * 
   * @param nodeSource
   * @param nodeTarget
   * @return
   * @throws IndexOutOfBoundsException if the nodes do not exist in the network.
   */
  public double getEdge(Integer nodeSource, Integer nodeTarget)
          throws NotExistsItemException {

    return this.network.getEdge(nodeSource, nodeTarget);
  }
  
  /**
   * 
   */
  public ArrayList<NetworkPair> getNetworkPairs() {
  
    return this.network.getNetworkPairs();
  }

  /**
   * 
   * @param nodesList
   * @return
   */
  public ArrayList<NetworkPair> getInternalPairs(ArrayList<Integer> nodesList) {

    return this.network.getInternalPairs(nodesList);
  }

  /**
   *
   * @param nodesList
   * @return
   */
  public ArrayList<NetworkPair> getExternalPairs(ArrayList<Integer> nodesList) {

    return this.network.getExternalPairs(nodesList);
  }
  
  /**
   * 
   * @param nodesSource
   * @param nodesTarget
   * @return 
   */
  public ArrayList<NetworkPair> getIntraNodesPairs(ArrayList<Integer> nodesSource, 
          ArrayList<Integer> nodesTarget) {
    
    return this.network.getIntraNodesPairs(nodesSource, nodesTarget);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
