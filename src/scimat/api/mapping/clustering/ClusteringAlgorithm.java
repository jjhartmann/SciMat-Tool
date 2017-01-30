/*
 * ClusteringAlgorithm.java
 *
 * Created on 15-feb-2011, 18:52:14
 */
package scimat.api.mapping.clustering;

import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.dataset.UndirectNetworkMatrix;

/**
 *
 * @author mjcobo
 */
public interface ClusteringAlgorithm {

  /**
   * 
   * @param network
   * @return
   */
  public ClusterSet execute(UndirectNetworkMatrix network);
}
