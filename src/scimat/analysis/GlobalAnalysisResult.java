/*
 * GlobalAnalysisResult.java
 *
 * Created on 04-abr-2011, 16:43:50
 */
package scimat.analysis;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mjcobo
 */
public class GlobalAnalysisResult implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private AnalysisConfiguration analysisConfiguration;

  /**
   *
   */
  private ArrayList<AnalysisPeriodResult> analysisPeriodResults;

  /**
   * 
   */
  private LongitudinalResult longitudinalResult;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param analysisConfiguration
   */
  public GlobalAnalysisResult(AnalysisConfiguration analysisConfiguration) {
    
    this.analysisPeriodResults = new ArrayList<AnalysisPeriodResult>();
    this.analysisConfiguration = analysisConfiguration;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param analysisPeriodResult
   */
  public void addAnalysisPeriodResult(AnalysisPeriodResult analysisPeriodResult) {

    this.analysisPeriodResults.add(analysisPeriodResult);
  }

  /**
   * 
   * @param index
   * @return
   */
  public AnalysisPeriodResult getAnalysisPeriodResult(int index) {

    return this.analysisPeriodResults.get(index);
  }

  /**
   * 
   * @return
   */
  public int getAnalysisPeriodResultsCount() {

    return this.analysisPeriodResults.size();
  }

  /**
   * 
   * @return
   */
  public AnalysisConfiguration getAnalysisConfiguration() {
    return analysisConfiguration;
  }

  /**
   * 
   * @return
   */
  public LongitudinalResult getLongitudinalResult() {
    return longitudinalResult;
  }

  /**
   * 
   * @param longitudinalResult
   */
  public void setLongitudinalResult(LongitudinalResult longitudinalResult) {
    this.longitudinalResult = longitudinalResult;
  }


  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
