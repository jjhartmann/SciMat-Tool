/*
 * SumCitationAggregationMeasure.java
 *
 * Created on 21-feb-2011, 12:30:16
 */
package scimat.api.analysis.performance.quantity;

import scimat.api.analysis.performance.DocumentAggregationMeasure;
import scimat.api.analysis.performance.docmapper.DocumentSet;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public class DocumentCountAggregationMeasure implements DocumentAggregationMeasure {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DocumentCountAggregationMeasure() {
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param docsList
   * @return
   *
   * @throws NotExistsItemException if a doc is not present in the dataset.
   */
  public double calculateMeasure(DocumentSet documentSet) {

    return documentSet.getDocumentsCount();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
