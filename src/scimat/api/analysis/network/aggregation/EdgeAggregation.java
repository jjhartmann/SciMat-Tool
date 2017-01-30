/*
 * EdgeAggregation.java
 *
 * Created on 24-feb-2011, 0:57:19
 */
package scimat.api.analysis.network.aggregation;

import java.util.ArrayList;
import scimat.api.dataset.NetworkPair;

/**
 *
 * @author mjcobo
 */
public interface EdgeAggregation {

  /**
   * 
   * @param pairs
   * @return
   */
  public double aggregate(ArrayList<NetworkPair> pairs);
}
