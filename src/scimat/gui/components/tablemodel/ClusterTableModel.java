/*
 * ClusterTableModel.java
 *
 * Created on 05-abr-2011, 13:27:43
 */
package scimat.gui.components.tablemodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.utils.property.Property;
import scimat.analysis.KeyProperties;

/**
 *
 * @author mjcobo
 */
public class ClusterTableModel extends GenericTableModel<Cluster> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private final NumberFormat numberFormatter = new DecimalFormat("0.##");

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public ClusterTableModel() {
    super(new String[] {"Cluster", "Centrality", "Centrality range", "Density", "Density range"});
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {

    if ((rowIndex >= 0) && (rowIndex < getRowCount())) {

      Property property;

      switch (columnIndex) {

        case 0:

          property = getItem(rowIndex).getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL);

          return (property != null) ? property.toString() : "";

        case 1:

          property = getItem(rowIndex).getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY);
                    
          return (property != null) ? this.numberFormatter.format((Double)property.getValue()) : "";

        case 2:

          property = getItem(rowIndex).getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY_RANGE);
          
          return (property != null) ? this.numberFormatter.format((Double)property.getValue()) : "";

        case 3:

          property = getItem(rowIndex).getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY);

          return (property != null) ? this.numberFormatter.format((Double)property.getValue()) : "";

        case 4:

          property = getItem(rowIndex).getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY_RANGE);

          return (property != null) ? this.numberFormatter.format((Double)property.getValue()) : "";

        default:
          return "";
      }

    } else {

      return "";

    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
