/*
 * OverlappingMeasure.java
 *
 * Created on 08-may-2011, 19:01:01
 */
package scimat.api.analysis.temporal;

import java.io.Serializable;
import java.util.ArrayList;
import scimat.api.similaritymeasure.direct.DirectSimilarityMeasure;

/**
 *
 * @author mjcobo
 */
public class OverlappingMeasure implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private DirectSimilarityMeasure measure;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public OverlappingMeasure(DirectSimilarityMeasure measure) {
    this.measure = measure;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param itemsSource
   * @param itemsTarget
   * @return
   */
  public double calculateOverlapping(ArrayList<Integer> itemsSource, ArrayList<Integer> itemsTarget) {

    ArrayList<Integer> tmp = new ArrayList<Integer>(itemsSource);

    tmp.retainAll(itemsTarget);

    return this.measure.calculateMeasure(itemsSource.size(), itemsTarget.size(), tmp.size());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
