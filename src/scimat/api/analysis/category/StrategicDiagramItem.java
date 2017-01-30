/*
 * StrategicDiagramItem.java
 *
 * Created on 23-feb-2011, 21:50:34
 */
package scimat.api.analysis.category;

/**
 *
 * @author mjcobo
 */
public class StrategicDiagramItem {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private String label;
  private double valueAxisX;
  private double valueAxisY;
  private double volume;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param label
   * @param valueAxisX
   * @param valueAxisY
   * @param volume
   */
  public StrategicDiagramItem(String label, double valueAxisX, double valueAxisY, double volume) {
    this.label = label;
    this.valueAxisX = valueAxisX;
    this.valueAxisY = valueAxisY;
    this.volume = volume;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @return the xValue
   */
  public double getValueAxisX() {
    return valueAxisX;
  }

  /**
   * @return the yValue
   */
  public double getValueAxisY() {
    return valueAxisY;
  }

  /**
   * @return the volume
   */
  public double getVolume() {
    return volume;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
