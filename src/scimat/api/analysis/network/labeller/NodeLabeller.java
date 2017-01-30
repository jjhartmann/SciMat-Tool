/*
 * NodeLabeller.java
 *
 * Created on 30-mar-2011, 16:59:18
 */
package scimat.api.analysis.network.labeller;

import scimat.api.mapping.Node;

/**
 *
 * @author mjcobo
 */
public interface NodeLabeller {

  public String execute(Node node);
}
