/*
 * BasicClusterLabellerBasedOnMainNode.java
 *
 * Created on 30-mar-2011, 17:01:53
 */
package scimat.api.analysis.network.labeller;

import scimat.api.dataset.AggregatedDataset;
import scimat.api.mapping.clustering.result.Cluster;

/**
 *
 * @author mjcobo
 */
public class AggregatedCouplingBasicClusterLabellerBasedOnMainNode implements ClusterLabeller {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private AggregatedDataset aggregatedDataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param dataset
   */
  public AggregatedCouplingBasicClusterLabellerBasedOnMainNode(AggregatedDataset dataset) {
    this.aggregatedDataset = dataset;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param cluster
   * @return
   */
  public String execute(Cluster cluster) {

    String label;
    Integer mainNode;

    label = "";

    mainNode = cluster.getMainNode();

    if (mainNode != null) {

      label = this.aggregatedDataset.getHighLevelItemLabel(mainNode);
    }

    return label;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
