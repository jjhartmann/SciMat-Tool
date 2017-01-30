/*
 * EvolutionMapPajek.java
 *
 * Created on 03-jun-2011, 16:36:50
 */
package scimat.api.visualization.temporal;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import scimat.api.analysis.temporal.EvolutionMap;
import scimat.api.analysis.temporal.EvolutionMapNexus;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.utils.property.DoubleProperty;

/**
 *
 * @author mjcobo
 */
public class EvolutionMapPajek {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<ClusterSet> clusterSets;
  private EvolutionMap evolutionMap;
  
  private NumberFormat numberFormat = new DecimalFormat ( "0.0000" );
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param clusterSets
   * @param evolutionMap 
   */
  public EvolutionMapPajek(ArrayList<ClusterSet> clusterSets, EvolutionMap evolutionMap) {
    this.clusterSets = clusterSets;
    this.evolutionMap = evolutionMap;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  public void execute(String path, String volumePropertyName, String labelPropertyName) throws FileNotFoundException {
  
    int periodIndex, clusterIndex, i, j, gap;
    double coordX, coordY, nodeSize, maxSize, tmpSize;
    Integer id;
    Cluster cluster, clusterPer1, clusterPer2;
    ArrayList<Cluster> clustersPer1, clustersPer2;
    ArrayList<TreeMap<Integer, Integer>> nodesIDs;
    PrintStream out = new PrintStream(path);
    EvolutionMapNexus nexus;

    // Cada nodo de cada tema de cada periodo tiene que tener un identificador
    // unico.
    nodesIDs = new ArrayList<TreeMap<Integer, Integer>>();

    id = 1;
    maxSize = 0;

    for (periodIndex = 0; periodIndex < clusterSets.size(); periodIndex ++) {

      // Creamos el mapa para los ids del periodo actual
      nodesIDs.add(new TreeMap<Integer, Integer>());

      for (clusterIndex = 0; clusterIndex < this.clusterSets.get(periodIndex).getClustersCount(); clusterIndex ++) {

        cluster = this.clusterSets.get(periodIndex).getCluster(clusterIndex);

        // Calculamos el tamaño maximo.
        tmpSize = ((DoubleProperty)cluster.getProperties().getProperty(volumePropertyName)).getValue();

        if (tmpSize > maxSize) {

          maxSize = tmpSize;
        }

        if (! nodesIDs.get(periodIndex).containsKey(cluster.getMainNode())) {

          nodesIDs.get(periodIndex).put(cluster.getMainNode(), id);

          id++;
        }
      }
    }

    // Pintamos la zona de vertices.
    out.println("*Vertices " + (id - 1));

    for (periodIndex = 0; periodIndex < this.clusterSets.size(); periodIndex ++) {

      coordY = 0.01;
      coordX = 0.01 + (1.0 / this.clusterSets.size()) * periodIndex;

      for (clusterIndex = 0; clusterIndex < this.clusterSets.get(periodIndex).getClustersCount(); clusterIndex++) {

        cluster = this.clusterSets.get(periodIndex).getCluster(clusterIndex);

        out.print(nodesIDs.get(periodIndex).get(cluster.getMainNode()));
        out.print(" ");
        out.print("\"Per" + periodIndex + "_" + clusterIndex + "_" + cluster.getProperties().getProperty(labelPropertyName).getValue() + "\"");
        out.print(" ");
        out.print(numberFormat.format(coordX));
        out.print(" ");
        out.print(numberFormat.format(coordY));
        out.print(" ");
        out.print("0.5000");
        out.print(" ");
        out.print("ic Blue");
        out.print(" ");
        out.print("bc Blue");
        out.print(" ");

        nodeSize = ((DoubleProperty)cluster.getProperties().getProperty(volumePropertyName)).getValue() * 10.0 / maxSize;

        out.print("x_fact " + Math.ceil(nodeSize));
        out.print(" ");
        out.println("y_fact " + Math.ceil(nodeSize));

        // Incrementamos la coordenada Y.
        coordY += (1.0 / this.clusterSets.get(periodIndex).getClustersCount());
      }
    }

    out.println("*edges");

    gap = 0;
    
    for (periodIndex = 0; periodIndex < (this.clusterSets.size() - 1); periodIndex++) {

      clustersPer1 = this.clusterSets.get(periodIndex).getClusters();
      clustersPer2 = this.clusterSets.get(periodIndex + 1).getClusters();

      for (i = 0; i < clustersPer1.size(); i++) {

        clusterPer1 = clustersPer1.get(i);

        for (j = 0; j < clustersPer2.size(); j++) {

          clusterPer2 = clustersPer2.get(j);

          nexus = this.evolutionMap.getEvolutionNexus(gap, i, j);

          if (nexus != null) {

            out.print(nodesIDs.get(periodIndex).get(clusterPer1.getMainNode()));
            out.print(" ");
            out.print(nodesIDs.get(periodIndex + 1).get(clusterPer2.getMainNode()));
            out.print(" ");
            out.print(numberFormat.format(nexus.getWeight() * 10));
            out.print(" ");

            if (nexus.isShareMainNode()) {

              out.println("c Blue");

            } else {

              out.println("c Gray95");

            }
          }
        }
      }
      
      gap ++;
    }

    out.close();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
