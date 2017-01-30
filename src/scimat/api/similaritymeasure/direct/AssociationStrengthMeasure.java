/*
 * AssociationStrengthMeasure.java
 *
 * Created on 14-feb-2011, 20:06:25
 */
package scimat.api.similaritymeasure.direct;

/**
 *
 * @author mjcobo
 */
public class AssociationStrengthMeasure implements DirectSimilarityMeasure {

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

    return coOccurrenceIJ / (frequencyNodeI * frequencyNodeJ);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
