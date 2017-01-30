/*
 * CompleteLinkClustering.java
 *
 * Created on 17-may-2011, 11:52:50
 */
package scimat.api.mapping.clustering;

import java.util.ArrayList;
import scimat.api.analysis.network.statistics.MaxAverageSimilarNode;
import scimat.api.dataset.NetworkPair;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public class CompleteLinkClustering implements ClusteringAlgorithm {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   *
   */
  private int minNetworkSize;

  /**
   * 
   */
  private int maxNetworkSize;
  
  /**
   * 
   */
  private double cutOff;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param minNetworkSize
   * @param maxNetworkSize
   * @param cutOff 
   */
  public CompleteLinkClustering(int minNetworkSize, int maxNetworkSize, double cutOff) {
    this.minNetworkSize = minNetworkSize;
    this.maxNetworkSize = maxNetworkSize;
    this.cutOff = cutOff;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param network
   * @return 
   */
  public ClusterSet execute(UndirectNetworkMatrix network) {
    
    int i, j, clusterToJoinI, clusterToJoinJ;
    boolean joinedClusters;
    double minSimilarity, similarity;
    ArrayList<ArrayList<Integer>> clusters;
    ArrayList<Integer> clusterI, clusterJ;
    ArrayList<NetworkPair> pairs;
    ClusterSet clusterSet;
    
    clusterSet = new ClusterSet(network);
    
    clusters = eachNodeToCluster(network.getNodes());

    joinedClusters = true;

    while (joinedClusters) {

      minSimilarity = 0.0;
      clusterToJoinI = -1;
      clusterToJoinJ = -1;

      for (i = 0; i < clusters.size(); i++) {

        clusterI = clusters.get(i);

        for (j = i + 1; j < clusters.size(); j++) {

          clusterJ = clusters.get(j);

          pairs = network.getIntraNodesPairs(clusterI, clusterJ);

          if (pairs.size() > 0) {

            similarity = getMinPairsValue(pairs);

            if ((similarity < minSimilarity) && (similarity >= this.cutOff)) {

              minSimilarity = similarity;

              clusterToJoinI = i;
              clusterToJoinJ = j;
            }
          }
        }
      }

      if (clusterToJoinI != -1) {
        
        joinedClusters= joinClusters(clusters, clusterToJoinI, clusterToJoinJ);
        
      } else {
      
        joinedClusters = false;
      }
    }
      
    for (i = 0; i < clusters.size(); i++) {
    
      if (clusters.get(i).size() >= this.minNetworkSize) {
      
        clusterSet.addCluster(clusters.get(i), new MaxAverageSimilarNode().execute(network, clusters.get(i)));
      }
    }
    
    return clusterSet;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /**
   * 
   * @param nodes
   * @return 
   */
  private ArrayList<ArrayList<Integer>> eachNodeToCluster(ArrayList<Integer> nodes) {
  
    int i;
    ArrayList<Integer> cluster;
    ArrayList<ArrayList<Integer>> clusters;
    
    clusters = new ArrayList<ArrayList<Integer>>();
    
    for (i = 0; i < nodes.size(); i++) {
    
      cluster = new ArrayList<Integer>();
      cluster.add(nodes.get(i));
      clusters.add(cluster);
    }
    
    return clusters;
  }
  
  /**
   * 
   * @param clusters
   * @param i
   * @param j 
   */
  private boolean joinClusters(ArrayList<ArrayList<Integer>> clusters, int i, int j) {
  
    boolean flag;
    
    if ((clusters.get(i).size() + clusters.get(j).size()) <= this.maxNetworkSize) {
    
      clusters.get(i).addAll(clusters.get(j));
      clusters.remove(j);
      
      flag = true;
      
    } else {
    
      flag = false;
    }
    
    return flag;
  }
  
  /**
   * 
   * @param pairs
   * @return 
   */
  private double getMinPairsValue(ArrayList<NetworkPair> pairs) {
  
    int i;
    double minValue, tmpValue;
    
    minValue = Double.MAX_VALUE;
    
    if (pairs.size() > 0) {
      
      minValue = pairs.get(0).getValue();
      
      for (i = 1; i < pairs.size(); i++) {
        
        tmpValue = pairs.get(i).getValue();
        
        if (tmpValue < minValue) {
        
          minValue = tmpValue;
        }
      
      }
    }
    
    return minValue;
  }
}
