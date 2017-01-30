/*
 * DocumentAggregationMeasure.java
 *
 * Created on 21-feb-2011, 12:27:49
 */
package scimat.api.analysis.performance;

import scimat.api.analysis.performance.docmapper.DocumentSet;

/**
 *
 * @author mjcobo
 */
public interface DocumentAggregationMeasure {

  /**
   * 
   * @param documentSet
   * @return
   */
  public double calculateMeasure(DocumentSet documentSet);
}
