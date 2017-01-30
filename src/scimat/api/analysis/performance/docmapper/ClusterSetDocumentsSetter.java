/*
 * ClusterSetDocumentsSetter.java
 *
 * Created on 30-mar-2011, 20:29:10
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.utils.property.DocumentsProperty;

/**
 *
 * @author mjcobo
 */
public class ClusterSetDocumentsSetter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private DocumentMapper documentMapper;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param documentMapper
   */
  public ClusterSetDocumentsSetter(DocumentMapper documentMapper) {
    this.documentMapper = documentMapper;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public void execute(ClusterSet clusterSet, String newProperty) {

    int i;
    ArrayList<Cluster> clusterList;
    Cluster cluster;

    clusterList = clusterSet.getClusters();

    for (i = 0; i < clusterList.size(); i++) {

      cluster = clusterList.get(i);

      cluster.getProperties().addProperty(newProperty,
              new DocumentsProperty(this.documentMapper.executeMapper(cluster.getNodes())));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
