/*
 * EvolutionMapNexus.java
 *
 * Created on 10-may-2011, 19:09:43
 */
package scimat.api.analysis.temporal;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class EvolutionMapNexus implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private boolean shareMainNode;
  private double weight;

  
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param shareMainNode
   * @param weight 
   */
  public EvolutionMapNexus(boolean shareMainNode, double weight) {
    this.shareMainNode = shareMainNode;
    this.weight = weight;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @return 
   */
  public double getWeight() {
    return weight;
  }

  /**
   * 
   * @return 
   */
  public boolean isShareMainNode() {
    return shareMainNode;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
