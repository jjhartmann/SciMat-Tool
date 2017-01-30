/*
 * NetworkMeasure.java
 *
 * Created on 17-feb-2011, 17:59:21
 */
package scimat.api.analysis.network.statistics;

import java.util.ArrayList;
import scimat.api.mapping.WholeNetwork;

/**
 *
 * @author mjcobo
 */
public interface NetworkMeasure {

  /**
   * 
   * @param network
   * @param nodeList
   * @return
   */
  public double calculateMeasure(WholeNetwork network, ArrayList<Integer> nodeList);
}
