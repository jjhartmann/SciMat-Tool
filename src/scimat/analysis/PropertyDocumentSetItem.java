/*
 * PropertyDocumentSetItem.java
 *
 * Created on 05-abr-2011, 14:22:02
 */
package scimat.analysis;

/**
 *
 * @author mjcobo
 */
public class PropertyDocumentSetItem {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private String mapper;
  private String propertyKey;
  private double value;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param mapper
   * @param propertyKey
   * @param value
   */
  public PropertyDocumentSetItem(String mapper, String propertyKey, double value) {
    this.mapper = mapper;
    this.propertyKey = propertyKey;
    this.value = value;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public String getMapper() {
    return mapper;
  }

  public String getPropertyKey() {
    return propertyKey;
  }

  public double getValue() {
    return value;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
