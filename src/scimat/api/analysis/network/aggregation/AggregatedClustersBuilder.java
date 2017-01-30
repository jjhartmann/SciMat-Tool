/*
 * AggregateClustersBuilder.java
 *
 * Created on 25-feb-2011, 12:16:45
 */
package scimat.api.analysis.network.aggregation;

import java.util.ArrayList;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public class AggregatedClustersBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param clusterSet
   * @param aggregationFunction
   * @return
   */
  public AggregatedClusters buildAggregatedClusters(ClusterSet clusterSet, EdgeAggregation aggregationFunction) {

    int i, j;
    double aggregatedEdges;
    ArrayList<Cluster> clusterList;
    AggregatedClusters aggregated;

    aggregated = new AggregatedClusters(clusterSet.getClustersCount());
    clusterList = clusterSet.getClusters();

    for (i = 0; i < clusterList.size(); i++) {

      for (j = i + 1; j < clusterList.size(); j++) {

        aggregatedEdges = aggregationFunction.aggregate(clusterSet.getIntraClusterPairs(i, j));

        aggregated.setEdge(i, j, aggregatedEdges);

      }
    }

    return aggregated;

  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
