/*
 * BuildPerformanceMeasuresAvailable.java
 *
 * Created on 12-may-2011, 23:58:02
 */
package scimat.analysis;

import java.util.ArrayList;

/**
 *
 * @author mjcobo
 */
public class BuildPerformanceMeasuresAvailable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  public ArrayList<PerformanceMeasuresAvailable> build() {
  
    ArrayList<PerformanceMeasuresAvailable> properties = new ArrayList<PerformanceMeasuresAvailable>();
    AnalysisConfiguration conf = CurrentAnalysis.getInstance().getResults().getAnalysisConfiguration();

    if (conf.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence)
            || conf.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnAuthor)
            || conf.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnJournal)) {

      if (conf.isCoreMapper()) {

        addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_CORE_DOCUMENTS);
      }

      if (conf.isSecondaryMapper()) {

        addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_SECONDARY_DOCUMENTS);
      }

      if (conf.iskCoreMapper()) {

        addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_KCORE_DOCUMENTS);
      }

      if (conf.isIntersectionMapper()) {

        addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_INTERSECTION_DOCUMENTS);
      }

      if (conf.isUnionMapper()) {

        addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_UNION_DOCUMENTS);
      }

    } else {

      if (conf.isBasicCouplingMapper()) {

        addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_COUPLING_DOCUMENTS);
      }
    }
    
    return properties;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /**
   * 
   * @param conf
   * @param properties
   * @param mapper
   * @param propertySet
   */
  private void addDocumentMapperProperties(AnalysisConfiguration conf,
          ArrayList<PerformanceMeasuresAvailable> properties, String mapper) {

    properties.add(new PerformanceMeasuresAvailable(mapper,
            KeyProperties.__KEY_DOCUMENTS_COUNT));

    if (conf.isHIndex()) {

      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_HINDEX));
    }
    
    if (conf.isGIndex()) {

      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_GINDEX));
    }
    
    if (conf.isHgIndex()) {

      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_HGINDEX));
    }
    
    if (conf.isQ2Index()) {

      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_Q2INDEX));
    }

    if (conf.isAverageCitations()) {


      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_AVERAGE_CITATIONS));
    }

    if (conf.isSumCitations()) {


      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_SUM_CITATIONS));
    }

    if (conf.isMaxCitations()) {


      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_MAX_CITATIONS));
    }

    if (conf.isMinCitations()) {

      properties.add(new PerformanceMeasuresAvailable(mapper,
              KeyProperties.__KEY_MIN_CITATIONS));
    }
  }
}
