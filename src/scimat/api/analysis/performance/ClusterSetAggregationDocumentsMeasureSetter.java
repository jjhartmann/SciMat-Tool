/*
 * ClusterSetDocumentsSetter.java
 *
 * Created on 30-mar-2011, 20:29:10
 */
package scimat.api.analysis.performance;

import java.util.ArrayList;
import scimat.api.dataset.Dataset;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.utils.property.DocumentsProperty;
import scimat.api.utils.property.DoubleProperty;
import scimat.api.utils.property.Property;
import scimat.api.utils.property.PropertyTypes;

/**
 *
 * @author mjcobo
 */
public class ClusterSetAggregationDocumentsMeasureSetter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private DocumentAggregationMeasure documentAggregationMeasure;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param documentAggregationMeasure
   */
  public ClusterSetAggregationDocumentsMeasureSetter(DocumentAggregationMeasure documentAggregationMeasure) {
    this.documentAggregationMeasure = documentAggregationMeasure;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public boolean execute(ClusterSet clusterSet, final String propertyKey,
          final String newProperty) {

    int i;
    ArrayList<Cluster> clusterList;
    Cluster cluster;
    Property property;
    DocumentsProperty documentsProperty;
    boolean flag;

    clusterList = clusterSet.getClusters();
    flag = true;

    // Check if all clusters have the propertyKey and if the type of this
    // property is Double. Moreover, check if the cluster d
    for (i = 0; i < clusterList.size(); i++) {

      property = clusterList.get(i).getProperties().getProperty(propertyKey);

      if ((property == null) || (! property.getType().equals(PropertyTypes.DocumentsProperty))) {

        flag = false;
      }
    }

    if (flag) {

      for (i = 0; i < clusterList.size(); i++) {

        cluster = clusterList.get(i);
        documentsProperty = (DocumentsProperty)cluster.getProperties().getProperty(propertyKey);

        cluster.getProperties().addProperty(newProperty, 
                new DoubleProperty(this.documentAggregationMeasure.calculateMeasure(documentsProperty.getValue())));
      }

      flag = true;
    }

    return flag;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
