/*
 * EvolutionMapBuilder.java
 *
 * Created on 09-may-2011, 23:57:59
 */
package scimat.api.analysis.temporal;

import java.util.ArrayList;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public class EvolutionMapBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private OverlappingMeasure measure;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param measure
   */
  public EvolutionMapBuilder(OverlappingMeasure measure) {
    this.measure = measure;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param clusterSets
   * @return
   */
  public EvolutionMap buildEvolutionMap(ArrayList<ClusterSet> clusterSets) {

    int i, j, k, evolutionGap;
    double weight;
    boolean shareMainNode;
    ClusterSet clusterSetSource, clusterSetTarget;
    Cluster clusterSource, clusterTarget;
    EvolutionMapNexus nexus;
    EvolutionMap evolutionMap;
    
    evolutionMap = new EvolutionMap(clusterSets.size() - 1);
    
    if (clusterSets.size() > 1) {
    
      clusterSetSource = clusterSets.get(0);
      
      evolutionGap = 0;
      
      for (i = 1; i < clusterSets.size(); i++) {
        
        clusterSetTarget = clusterSets.get(i);
        
        for (j = 0; j < clusterSetSource.getClustersCount(); j++) {
          
          clusterSource = clusterSetSource.getCluster(j);
          
          for (k = 0; k < clusterSetTarget.getClustersCount(); k++) {
            
            clusterTarget = clusterSetTarget.getCluster(k);
            
            weight = measure.calculateOverlapping(clusterSource.getNodes(), clusterTarget.getNodes());
            
            if (weight > 0.0000) {

              shareMainNode = clusterSource.getNodes().contains(clusterTarget.getMainNode()) || 
                              clusterTarget.getNodes().contains(clusterSource.getMainNode());
            
              nexus = new EvolutionMapNexus(shareMainNode, weight);
              
              evolutionMap.addNexus(evolutionGap, j, k, nexus);
            }
            
          }
        
        }
        
        clusterSetSource = clusterSetTarget;
        evolutionGap ++;
      }
    }

    for (i = 0; i < clusterSets.size(); i++) {
      
      clusterSetSource = clusterSets.get(i);

      for (j = i + 1; j < clusterSets.size(); j++) {
      
        clusterSetTarget = clusterSets.get(j);
      }
    }

    return evolutionMap;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
