/*
 * ClusterDeriveMeasure.java
 *
 * Created on 17-feb-2011, 20:45:37
 */
package scimat.api.analysis.network.statistics;

import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public interface ClusterDeriveMeasure {

  /**
   * Calculate a measure for all the cluster taking into account the value of
   * a specific property of all the cluster.
   *
   * @param clusterSet The cluster set
   * @param propertyKey The property's key used to derive the new measure
   * @param newProperty The new property's key where the new measure is located in each cluster.
   */
  public boolean calculateMeasures(ClusterSet clusterSet, String propertyKey,
          String newProperty);
}
