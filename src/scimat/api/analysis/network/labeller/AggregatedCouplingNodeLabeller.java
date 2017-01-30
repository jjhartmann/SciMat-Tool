/*
 * AggregatedCouplingNodeLabeller.java
 *
 * Created on 30-mar-2011, 17:09:09
 */
package scimat.api.analysis.network.labeller;

import scimat.api.dataset.AggregatedDataset;
import scimat.api.mapping.Node;

/**
 *
 * @author mjcobo
 */
public class AggregatedCouplingNodeLabeller implements NodeLabeller {

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
  public AggregatedCouplingNodeLabeller(AggregatedDataset dataset) {
    this.aggregatedDataset = dataset;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param node
   * @return
   */
  public String execute(Node node) {

    return this.aggregatedDataset.getHighLevelItemLabel(node.getNodeID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
