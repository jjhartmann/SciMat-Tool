/*
 * ClusterLabeller.java
 *
 * Created on 30-mar-2011, 16:59:09
 */
package scimat.api.analysis.network.labeller;

import scimat.api.mapping.clustering.result.Cluster;

/**
 *
 * @author mjcobo
 */
public interface ClusterLabeller {

  public String execute(Cluster cluster);
}
