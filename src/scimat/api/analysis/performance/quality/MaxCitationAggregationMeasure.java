/*
 * MaxCitationAggregationMeasure.java
 *
 * Created on 21-feb-2011, 12:30:16
 */
package scimat.api.analysis.performance.quality;

import scimat.api.analysis.performance.DocumentAggregationMeasure;
import java.util.ArrayList;
import scimat.api.analysis.performance.docmapper.DocumentSet;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public class MaxCitationAggregationMeasure implements DocumentAggregationMeasure {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public MaxCitationAggregationMeasure(Dataset dataset) {
    this.dataset = dataset;
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

    int i;
    double max, tmp;
    ArrayList<Integer> docsList;

    max = 0.0;
    docsList = documentSet.getDocuments();

    if (docsList.size() > 0) {

      max = dataset.getDocumentCitations(docsList.get(0));

      for (i = 1; i < docsList.size(); i++) {

        tmp = dataset.getDocumentCitations(docsList.get(i));

        if (max < tmp) {

          max = tmp;
        }
      }
    }

    return max;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
