/*
 * NodeLabelSetter.java
 *
 * Created on 30-mar-2011, 17:44:44
 */
package scimat.api.analysis.network.labeller;

import java.util.ArrayList;
import scimat.api.mapping.Node;
import scimat.api.utils.property.StringProperty;

/**
 *
 * @author mjcobo
 */
public class NodeLabelSetter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private NodeLabeller labeller;



  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public NodeLabelSetter(NodeLabeller labeller) {
    this.labeller = labeller;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param dataset
   * @param nodes
   * @param newPropertyKey
   */
  public void execute(ArrayList<Node> nodes, String newPropertyKey) {

    int i;
    Node node;

    for (i = 0; i < nodes.size(); i++) {

      node = nodes.get(i);
      node.getProperties().addProperty(newPropertyKey,
              new StringProperty(this.labeller.execute(node)));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
