/*
 * GIndex.java
 *
 * Created on 18-may-2011, 12:49:12
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
public class GIndex implements DocumentAggregationMeasure  {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Dataset dataset;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public GIndex(Dataset dataset) {
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

    int i, citationsCount;
    double sum, gIndex;
    boolean flag;
    ArrayList<Integer> docsList, citations;

    // Get the citations.
    citations = new ArrayList<Integer>();
    docsList = documentSet.getDocuments();

    for (i = 0; i < docsList.size(); i++) {
        
      citationsCount = dataset.getDocumentCitations(docsList.get(i));
      
      if (citationsCount > 0){
        
        citations.add(citationsCount);
      }
    }

    Collections.sort(citations, new Comparator<Integer>() {

      public int compare(Integer o1, Integer o2) {

        return o2.compareTo(o1);
      }
    });

    i = 0;
    sum = 0.0;
    flag = true;
    gIndex = 0;
    
    while ((i < citations.size()) && flag) {

      sum += citations.get(i);
      
      if (sum >= Math.pow(i + 1, 2)) {

        gIndex ++;

      } else {

        flag = false;
      }
      
      i ++;
    }
    
    return gIndex;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
