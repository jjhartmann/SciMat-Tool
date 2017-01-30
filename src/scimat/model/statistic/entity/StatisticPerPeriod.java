/*
 * StatisticPerPeriod.java
 *
 * Created on 25-ene-2012, 18:31:02
 */
package scimat.model.statistic.entity;

import scimat.model.knowledgebase.entity.Period;

/**
 *
 * @author mjcobo
 */
public class StatisticPerPeriod {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * The period used to calculate the statistics.
   */
  private Period period;
  
  /**
   * Number of unique groups associated with the documents of the period.
   */
  private int uniqueGroupsCount;
  
  /**
   * Max number of group per document.
   */
  private int max;
  
  /**
   * Min number of group per document.
   */
  private int min;
  
  /**
   * Average number of group per document.
   */
  private double mean;
  
  /**
   * Median of the number of group per document.
   */
  private double median;
  
  /**
   * Standard desviation of the number of group per document.
   */
  private double standardDesviation;
  
  /**
   * Variance of the number of group per document.
   */
  private double variance;
  
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param period The period used to calculate the statistics
   * @param uniqueGroupsCount
   * @param max Max number of group per document.
   * @param min Min number of group per document.
   * @param mean Average number of group per document.
   * @param median Median of the number of group per document.
   * @param standardDesviation Standard desviation of the number of group per document.
   * @param variance Variance of the number of group per document.
   */
  public StatisticPerPeriod(Period period, int uniqueGroupsCount, int max, 
          int min, double mean, double median, double standardDesviation, double variance) {
    
    this.period = period;
    this.uniqueGroupsCount = uniqueGroupsCount;
    this.max = max;
    this.min = min;
    this.mean = mean;
    this.median = median;
    this.standardDesviation = standardDesviation;
    this.variance = variance;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the period
   */
  public Period getPeriod() {
    return period;
  }

  /**
   * @return the uniqueGroupsCount
   */
  public int getUniqueGroupsCount() {
    return uniqueGroupsCount;
  }

  /**
   * @return the max
   */
  public int getMax() {
    return max;
  }

  /**
   * @return the min
   */
  public int getMin() {
    return min;
  }

  /**
   * @return the mean
   */
  public double getMean() {
    return mean;
  }

  /**
   * @return the median
   */
  public double getMedian() {
    return median;
  }

  /**
   * @return the standardDesviation
   */
  public double getStandardDesviation() {
    return standardDesviation;
  }

  /**
   * @return the variance
   */
  public double getVariance() {
    return variance;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
