/*
 * ClusterSetDocumentsSetter.java
 *
 * Created on 30-mar-2011, 20:29:10
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;
import scimat.api.mapping.Node;
import scimat.api.mapping.WholeNetwork;
import scimat.api.utils.property.DocumentsProperty;

/**
 *
 * @author mjcobo
 */
public class WholeNetworkDocumentsSetter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private NodeDocumentMapper nodeDocumentMapper;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param documentMapper
   */
  public WholeNetworkDocumentsSetter(NodeDocumentMapper documentMapper) {
    this.nodeDocumentMapper = documentMapper;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public void execute(WholeNetwork wholeNetwork, String newProperty) {

    int i;
    ArrayList<Node> nodes;
    Node node;

    nodes = wholeNetwork.getNodes();

    for (i = 0; i < nodes.size(); i++) {

      node = nodes.get(i);

      node.getProperties().addProperty(newProperty,
              new DocumentsProperty(this.nodeDocumentMapper.executeMapper(node.getNodeID())));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
