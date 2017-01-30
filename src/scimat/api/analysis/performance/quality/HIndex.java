/*
 * HIndex.java
 *
 * Created on 21-feb-2011, 12:30:16
 */
package scimat.api.analysis.performance.quality;

import scimat.api.analysis.performance.DocumentAggregationMeasure;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import scimat.api.analysis.performance.docmapper.DocumentSet;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public class HIndex implements DocumentAggregationMeasure {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public HIndex(Dataset dataset) {
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

    int i, citations;
    double hIndex;
    boolean flag;
    ArrayList<Integer> docsList, citationsList;

    // Get the citations.
    citationsList = new ArrayList<Integer>();
    docsList = documentSet.getDocuments();

    for (i = 0; i < docsList.size(); i++) {
      
      citations = dataset.getDocumentCitations(docsList.get(i));
      
      if (citations > 0){
        
        citationsList.add(citations);
      }
    }

    Collections.sort(citationsList, new Comparator<Integer>() {

      public int compare(Integer o1, Integer o2) {

        return o2.compareTo(o1);
      }
    });

    i = 0;
    hIndex = 0.0;
    flag = true;

    while ((i < citationsList.size()) && flag) {
      
      if (citationsList.get(i) < (i + 1)) {

        flag = false;
        hIndex = i;
      }
      
      i ++;
    }
    
    if ((i == citationsList.size()) && (flag)) {
    
      hIndex = i;
    }
    
    return hIndex;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
