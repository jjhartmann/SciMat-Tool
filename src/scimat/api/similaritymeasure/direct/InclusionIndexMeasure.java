/*
 * InclusionIndexMeasure.java
 *
 * Created on 14-feb-2011, 20:06:56
 */
package scimat.api.similaritymeasure.direct;

/**
 *
 * @author mjcobo
 */
public class InclusionIndexMeasure implements DirectSimilarityMeasure {

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

    return coOccurrenceIJ / Math.min(frequencyNodeI, frequencyNodeJ);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
