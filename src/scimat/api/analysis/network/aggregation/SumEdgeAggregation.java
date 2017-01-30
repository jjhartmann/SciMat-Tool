/*
 * SumEdgeAggregation.java
 *
 * Created on 24-feb-2011, 0:58:17
 */
package scimat.api.analysis.network.aggregation;

import java.util.ArrayList;
import scimat.api.dataset.NetworkPair;

/**
 *
 * @author mjcobo
 */
public class SumEdgeAggregation implements EdgeAggregation {

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
   * @param pairs
   * @return
   */
  public double aggregate(ArrayList<NetworkPair> pairs) {

    int i;
    double sum;

    sum = 0.0;

    for (i = 0; i < pairs.size(); i++) {

      sum += pairs.get(i).getValue();
    }

    return sum;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
