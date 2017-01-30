/*
 * MaxAverageSimilarNode.java
 *
 * Created on 07-jun-2011, 18:02:10
 */
package scimat.api.analysis.network.statistics;

import java.util.ArrayList;
import scimat.api.dataset.UndirectNetworkMatrix;

/**
 * Calculate the node with max avergage similarity between a list of nodes
 * @author mjcobo
 */
public class MaxAverageSimilarNode {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  public Integer execute(UndirectNetworkMatrix matrix, ArrayList<Integer> nodes) {
  
    int i, j;
    Integer finalNode, sourceNode, targetNode;
    ArrayList<Integer> neighbours;
    double[] similarities;
    double currentSim;
    
    similarities = new double[nodes.size()];
    
    for (i = 0; i < nodes.size(); i++) {
    
      sourceNode = nodes.get(i);
      
      neighbours = matrix.getNeighbours(sourceNode);
      
      currentSim = 0.0;
      
      for (j = 0; j < neighbours.size(); j++) {
      
        targetNode = neighbours.get(j);
        
        currentSim += matrix.getEdge(sourceNode, targetNode);
      }
      
      similarities[i] = currentSim / neighbours.size();
    }
    
    finalNode = null;
    i = 0;
    
    if (similarities.length > 0) {
    
      finalNode = nodes.get(i);
      currentSim = similarities[i];
      
      for (i = 1; i < similarities.length; i++) {
      
        if (currentSim > similarities[i]) {
        
          finalNode = nodes.get(i);
        }
      }
    }
    
    return finalNode;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
