/*
 * SumCitationAggregationMeasure.java
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
public class SumCitationAggregationMeasure implements DocumentAggregationMeasure {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public SumCitationAggregationMeasure(Dataset dataset) {
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
    double result;
    ArrayList<Integer> docsList;
    
    docsList = documentSet.getDocuments();
    result = 0;

    for (i = 0; i < docsList.size(); i++) {
        
      result += dataset.getDocumentCitations(docsList.get(i));
    }

    return result;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
