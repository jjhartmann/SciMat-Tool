/*
 * StrategicDiagramBuildier.java
 *
 * Created on 09-may-2011, 18:59:10
 */
package scimat.api.analysis.category;

import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public class StrategicDiagramBuildier {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private String xAxisPropertyName;
  private String yAxisPropertyName;
  private String labelPropertyName;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param xAxisPropertyName
   * @param yAxisPropertyName
   * @param labelPropertyName
   */
  public StrategicDiagramBuildier(String xAxisPropertyName,
          String yAxisPropertyName, String labelPropertyName) {
    this.xAxisPropertyName = xAxisPropertyName;
    this.yAxisPropertyName = yAxisPropertyName;
    this.labelPropertyName = labelPropertyName;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param clusterSet
   * @param volumePropertyName
   * @return
   */
  public StrategicDiagram buildStrategicDiagram(ClusterSet clusterSet, String volumePropertyName) {

    int i;
    double x, y, volume;
    String label;
    Cluster cluster;
    StrategicDiagram strategicDiagram = new StrategicDiagram();

    for (i = 0; i < clusterSet.getClustersCount(); i++) {

      cluster = clusterSet.getCluster(i);

      x = (Double) cluster.getProperties().getProperty(this.xAxisPropertyName).getValue();
      y = (Double) cluster.getProperties().getProperty(this.yAxisPropertyName).getValue();
      
      if (volumePropertyName != null) {
      
        volume = (Double) cluster.getProperties().getProperty(volumePropertyName).getValue();
        
      } else {
      
        volume = 0.0;
      }
      
      label = (String) cluster.getProperties().getProperty(this.labelPropertyName).getValue();

      strategicDiagram.addItem(label, x, y, volume);
    }

    return strategicDiagram;
  }
  

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
