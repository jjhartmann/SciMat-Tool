/*
 * ClusterSet.java
 *
 * Created on 15-feb-2011, 18:52:41
 */
package scimat.api.mapping.clustering.result;

import java.io.Serializable;
import scimat.api.mapping.Node;
import scimat.api.mapping.WholeNetwork;
import java.util.ArrayList;
import java.util.TreeMap;
import scimat.api.dataset.NetworkPair;
import scimat.api.dataset.UndirectNetworkMatrix;

/**
 *
 * @author mjcobo
 */
public class ClusterSet implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The whole network. This network is an undirect weighted network, and it is
   * represented as an adjacency matrix.
   */
  private WholeNetwork wholeNetwork;

  /**
   * The clusters list.
   */
  private ArrayList<Cluster> clusterList;

  /**
   * 
   */
  private TreeMap<Integer, Integer> clusterOfNodes;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param network
   */
  public ClusterSet(UndirectNetworkMatrix network) {

    this.wholeNetwork = new WholeNetwork(network);

    this.clusterList = new ArrayList<Cluster>();

    this.clusterOfNodes = new TreeMap<Integer, Integer>();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public WholeNetwork getWholeNetwork() {

    return this.wholeNetwork;
  }

  /**
   * 
   * @return
   */
  public int getClustersCount() {

    return this.clusterList.size();
  }

  /**
   * 
   * @return
   */
  public ArrayList<Cluster> getClusters() {

    return new ArrayList<Cluster>(clusterList);
  }

  /**
   * 
   * @param cluster
   * @return
   */
  public Cluster getCluster(int cluster) {

    return this.clusterList.get(cluster);
  }

  /**
   * 
   * @param nodes
   * @return
   */
  public boolean addCluster(ArrayList<Integer> nodes) {

    int i, clusterPosition;
    Node node;
    Cluster cluter;

    // Check if all the nodes belong to the wholenetwork and the nodes do not
    // belong yet to any cluster.
    for (i = 0; i < nodes.size(); i++) {
      
      node = this.wholeNetwork.getNode(nodes.get(i));

      if ((node == null) || (getClusterOfNode(node.getNodeID()) != -1)) {

        return false;
      }
    }

    cluter = new Cluster(nodes);
    this.clusterList.add(cluter);
    clusterPosition = this.clusterList.size() - 1;

    for (i = 0; i < nodes.size(); i++) {

      addClusterToNode(nodes.get(i), clusterPosition);
    }

    return true;
  }

  /**
   *
   * @param nodes
   * @return
   */
  public boolean addCluster(ArrayList<Integer> nodes, Integer mainNode) {

    int i, clusterPosition;
    boolean containMainNode;
    Node node;
    Cluster cluter;

    containMainNode = false;

    // Check if all the nodes belong to the wholenetwork and the nodes do not
    // belong yet to any cluster.
    for (i = 0; i < nodes.size(); i++) {

      node = this.wholeNetwork.getNode(nodes.get(i));

      if ((node == null) || (getClusterOfNode(node.getNodeID()) != -1)) {

        return false;
      }

      if (node.getNodeID().intValue() == mainNode.intValue()) {

        containMainNode = true;
      }
    }

    if (containMainNode) {

      cluter = new Cluster(nodes, mainNode);
      this.clusterList.add(cluter);
      clusterPosition = this.clusterList.size() - 1;

      for (i = 0; i < nodes.size(); i++) {

        addClusterToNode(nodes.get(i), clusterPosition);
      }

      return true;

    } else {

      return false;
    }
  }

  /**
   * 
   * @param node
   * @return the cluster index of the Node of -1 if the node is not 
   * associated with any cluster.
   */
  public int getClusterOfNode(int node) {

    Integer cluster;

    cluster = this.clusterOfNodes.get(node);

    if (cluster != null) {

      return cluster;
      
    } else {

      return -1;
    }
  }

  /**
   * 
   * @param cluster
   * @return
   */
  public ArrayList<NetworkPair> getClusterInternalPairs(int cluster) {

    return this.wholeNetwork.getInternalPairs(this.clusterList.get(cluster).getNodes());
  }

  /**
   * 
   * @param cluster
   * @return
   */
  public ArrayList<NetworkPair> getClusterExternalPairs(int cluster) {

    return this.wholeNetwork.getExternalPairs(this.clusterList.get(cluster).getNodes());
  }

  /**
   * Return a list with the pairs between the nodes of the two clusters.
   *
   * @param cluster1 the position of the first cluster in the cluster set.
   * @param cluster2 the position of the second cluster in the cluster set.
   *
   * @return the list with the pairs between the two cluster.
   */
  public ArrayList<NetworkPair> getIntraClusterPairs(int cluster1, int cluster2) {

    return this.wholeNetwork.getIntraNodesPairs(this.getCluster(cluster1).getNodes(), 
            this.getCluster(cluster2).getNodes());
    
    /*int i, j;
    Integer nodeCluster1ID, nodeCluster2ID;
    Double value;
    ArrayList<Integer> nodesCluster1, nodesCluster2;
    ArrayList<NetworkPair> pairs;
    
    pairs = new ArrayList<NetworkPair>();

    nodesCluster1 = this.getCluster(cluster1).getNodes();
    nodesCluster2 = this.getCluster(cluster2).getNodes();

    for (i = 0; i < nodesCluster1.size(); i++) {

      nodeCluster1ID = nodesCluster1.get(i);

      for (j = 0; j < nodesCluster2.size(); j++) {

        nodeCluster2ID = nodesCluster2.get(j);

        value = this.wholeNetwork.getEdge(nodeCluster1ID, nodeCluster2ID);

        if (value != null) {

          pairs.add(new NetworkPair(nodeCluster1ID, nodeCluster2ID, value));
        }
      }
    }

    return pairs;*/
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * 
   * @param nodeID
   * @param cluster
   */
  private void addClusterToNode(Integer nodeID, int cluster) {

    this.clusterOfNodes.put(nodeID, cluster);
  }
}
