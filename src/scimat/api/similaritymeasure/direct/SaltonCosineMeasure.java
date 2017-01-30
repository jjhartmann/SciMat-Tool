/*
 * SaltonCosineMeasure.java
 *
 * Created on 14-feb-2011, 20:06:41
 */
package scimat.api.similaritymeasure.direct;

/**
 *
 * @author mjcobo
 */
public class SaltonCosineMeasure implements DirectSimilarityMeasure {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public double calculateMeasure(int frequencyNodeI, int frequencyNodeJ, double coOccurrenceIJ) {

    return (coOccurrenceIJ * coOccurrenceIJ) / Math.sqrt(frequencyNodeI * frequencyNodeJ);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
