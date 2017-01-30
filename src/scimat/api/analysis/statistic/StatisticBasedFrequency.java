/*
 * StatisticBasedFrequency.java
 *
 * Created on 25-ene-2012, 13:54:59
 */
package scimat.api.analysis.statistic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author mjcobo
 */
public class StatisticBasedFrequency {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private DescriptiveStatistics statistics;
  private Frequency frequency;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public StatisticBasedFrequency() {
    init();
  }
  
  /**
   * 
   * @param frequencies 
   */
  public StatisticBasedFrequency(ArrayList<Integer> frequencies) {
    init();
    
    addFrequency(frequencies);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param frequency 
   */
  public final void addFrequency(int frequency) {
  
    this.statistics.addValue(frequency);
    this.frequency.addValue(frequency);
  }
  
  /**
   * 
   * @param frequencies 
   */
  public final void addFrequency(ArrayList<Integer> frequencies) {
  
    int i;
    
    for (i = 0; i < frequencies.size(); i++) {
      
      addFrequency(frequencies.get(i));
    }
  }
  
  /**
   * 
   */
  public int getMax() {
  
    return (int)this.statistics.getMax();
  }
  
  /**
   * 
   */
  public int getMin() {
  
    return (int)this.statistics.getMin();
  }
  
  /**
   * 
   */
  public double getMean() {
  
    return this.statistics.getMean();
  }
  
  /**
   * 
   */
  public double getMedian() {
  
    return this.statistics.getPercentile(50);
  }
  
  /**
   * 
   */
  public double getStandardDesviation() {
  
    return this.statistics.getStandardDeviation();
  }
  
  /**
   * 
   */
  public double getVariance() {
  
    return this.statistics.getVariance();
  }
  
  /**
   * 
   * @return 
   */
  public long getMaxFrequency() {
  
    Iterator it;
    
    it = this.frequency.valuesIterator();
    
    if (it.hasNext()) {
    
      return (Long)it.next();
      
    } else {
    
      return 0;
    }
  }
  
  /**
   * Returns the cumulative frequency of values great than or equal to v.
   * <p>
   * Returns 0 if v is not comparable to the values set.</p>
   *
   * @param v the value to lookup
   * @return the proportion of values great than or equal to v
   */
  public long getCumFrequency(int v) {
  
    return this.frequency.getCumFreq(v);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private void init() {
  
    this.statistics = new DescriptiveStatistics();
    
    this.frequency = new Frequency(new Comparator<Long>(){

      public int compare(Long o1, Long o2) {
        
        return o2.compareTo(o1);
      }
    });
  }
}
