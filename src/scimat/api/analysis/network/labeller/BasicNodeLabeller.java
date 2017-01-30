/*
 * BasicNodeLabeller.java
 *
 * Created on 30-mar-2011, 17:09:09
 */
package scimat.api.analysis.network.labeller;

import scimat.api.dataset.Dataset;
import scimat.api.mapping.Node;

/**
 *
 * @author mjcobo
 */
public class BasicNodeLabeller implements NodeLabeller {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param dataset
   */
  public BasicNodeLabeller(Dataset dataset) {
    this.dataset = dataset;
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

    return this.dataset.getItemLabel(node.getNodeID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
