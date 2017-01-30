/*
 * EvolutionMap.java
 *
 * Created on 08-may-2011, 17:18:03
 */
package scimat.api.analysis.temporal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author mjcobo
 */
public class EvolutionMap implements Serializable {



  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  //private ArrayList<ClusterSet> clusterSets;
  
  private ArrayList<TreeMap<Integer, TreeMap<Integer, EvolutionMapNexus>>> nexusList;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param clusterSets
   * @param overlappingMeasure
   */
  public EvolutionMap(int evolutionGap) {

    int i;
    //this.clusterSets = new ArrayList<ClusterSet>();
    this.nexusList = new ArrayList<TreeMap<Integer, TreeMap<Integer, EvolutionMapNexus>>>();
    
    for (i = 0; i < evolutionGap; i++) {
    
      this.nexusList.add(new TreeMap<Integer, TreeMap<Integer, EvolutionMapNexus>>());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param evolutionGap
   * @param clusterSource
   * @param clusterTarget
   * @param nexus 
   */
  public void addNexus(int evolutionGap, int clusterSource, int clusterTarget, 
          EvolutionMapNexus nexus) {
  
    TreeMap<Integer, EvolutionMapNexus> nexuses;
    
    nexuses = this.nexusList.get(evolutionGap).get(clusterSource);
    
    if (nexuses == null) {
    
      nexuses = new TreeMap<Integer, EvolutionMapNexus>();
      this.nexusList.get(evolutionGap).put(clusterSource, nexuses);
    }
    
    nexuses.put(clusterTarget, nexus);
  }

  /**
   * 
   * @param clusterSetSource
   * @param clusterSource
   * @param clusterTarget
   * @return
   */
  public EvolutionMapNexus getEvolutionNexus(int evolutionGap, int clusterSource,
                                  int clusterTarget) {

    EvolutionMapNexus nexus = null;
    TreeMap<Integer, EvolutionMapNexus> internalMap;
    
    
    
    if (this.nexusList.size() > evolutionGap) {
    
      internalMap = this.nexusList.get(evolutionGap).get(clusterSource);
      
      if (internalMap != null) {
      
        nexus = internalMap.get(clusterTarget);
      }
    }

    return nexus;
  }
  
  /**
   * 
   * @return 
   */
  public int getEvolutionGapCount() {
  
    return this.nexusList.size();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
