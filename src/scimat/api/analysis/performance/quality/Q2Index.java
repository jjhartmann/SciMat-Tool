/*
 * Q2Index.java
 *
 * Created on 18-may-2011, 12:49:39
 */
package scimat.api.analysis.performance.quality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import scimat.api.analysis.performance.DocumentAggregationMeasure;
import scimat.api.analysis.performance.docmapper.DocumentSet;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public class Q2Index implements DocumentAggregationMeasure {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Dataset dataset;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public Q2Index(Dataset dataset) {
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
    double hIndex, median;
    ArrayList<Integer> docsList, citationsList;
    HIndex hIndexFunction = new HIndex(this.dataset);

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
    hIndex = hIndexFunction.calculateMeasure(documentSet);
    
    if (citationsList.isEmpty()) {

      return 0;

    } else {

      if (hIndex % 2 == 1) {

        median = citationsList.get((((int) hIndex + 1) / 2) - 1);

      } else {

        median = citationsList.get(((int) hIndex / 2) - 1);
      }
      
      return Math.sqrt(hIndex * median);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
