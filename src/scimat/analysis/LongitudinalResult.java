/*
 * LongitudinalResult.java
 *
 * Created on 09-may-2011, 13:18:42
 */
package scimat.analysis;

import java.io.Serializable;
import scimat.api.analysis.temporal.EvolutionMap;
import scimat.api.analysis.temporal.OverlappingMap;

/**
 *
 * @author mjcobo
 */
public class LongitudinalResult implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private EvolutionMap evolutionMap;
  private OverlappingMap overlappingMap;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param evolutionMap
   * @param overlappingMap
   */
  public LongitudinalResult(EvolutionMap evolutionMap, OverlappingMap overlappingMap) {
    this.evolutionMap = evolutionMap;
    this.overlappingMap = overlappingMap;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public EvolutionMap getEvolutionMap() {
    return evolutionMap;
  }

  public OverlappingMap getOverlappingMap() {
    return overlappingMap;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
