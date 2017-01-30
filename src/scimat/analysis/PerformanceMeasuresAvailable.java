/*
 * PerformanceMeasuresAvailable.java
 *
 * Created on 12-may-2011, 23:26:58
 */
package scimat.analysis;

/**
 *
 * @author mjcobo
 */
public class PerformanceMeasuresAvailable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String mapper;
  private String propertyKey;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public PerformanceMeasuresAvailable(String mapper, String propertyKey) {
    this.mapper = mapper;
    this.propertyKey = propertyKey;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @return 
   */
  public String getMapper() {
    return mapper;
  }

  /**
   * 
   * @return 
   */
  public String getPropertyKey() {
    return propertyKey;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
