/*
 * MinEdgeAggregation.java
 *
 * Created on 24-feb-2011, 0:57:58
 */
package scimat.api.analysis.network.aggregation;

import java.util.ArrayList;
import scimat.api.dataset.NetworkPair;

/**
 *
 * @author mjcobo
 */
public class MinEdgeAggregation implements EdgeAggregation {

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
    double min, tmp;

    min = 0.0;

    if (pairs.size() > 0) {

      min = pairs.get(0).getValue();

      for (i = 1; i < pairs.size(); i++) {

        tmp = pairs.get(i).getValue();

        if (min > tmp) {

          min = tmp;
        }
      }
    }

    return min;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
