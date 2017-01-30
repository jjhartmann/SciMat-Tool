/*
 * EquivalenceIndexMeasure.java
 *
 * Created on 14-feb-2011, 20:11:18
 */
package scimat.api.similaritymeasure.direct;

/**
 *
 * @author mjcobo
 */
public class EquivalenceIndexMeasure implements DirectSimilarityMeasure {



  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param frequencyNodeI
   * @param frequencyNodeJ
   * @param coOccurrenceIJ
   * @return
   */
  public double calculateMeasure(int frequencyNodeI, int frequencyNodeJ, double coOccurrenceIJ) {
    
    return (coOccurrenceIJ * coOccurrenceIJ) / (frequencyNodeI * frequencyNodeJ);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
