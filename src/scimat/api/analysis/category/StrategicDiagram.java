/*
 * StrategicDiagram.java
 *
 * Created on 23-feb-2011, 21:49:18
 */
package scimat.api.analysis.category;

import java.util.ArrayList;

/**
 *
 * @author mjcobo
 */
public class StrategicDiagram {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private ArrayList<StrategicDiagramItem> items =  new ArrayList<StrategicDiagramItem>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param items
   */
  public StrategicDiagram() {
    
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param label
   * @param valueAxisX
   * @param valueAxisY
   * @param volume
   */
  public void addItem(String label, double valueAxisX, double valueAxisY, double volume) {

    this.items.add(new StrategicDiagramItem(label, valueAxisX, valueAxisY, volume));
  }

  /**
   * 
   * @param item
   */
  public void addItem(StrategicDiagramItem item) {

    this.items.add(item);
  }

  /**
   * Returns the list of items. The list backed by this strategic diagram, so
   * changes to the strategic diagram are reflected in the returned list, and
   * vice-versa.
   *
   * @return the list of items.
   */
  public ArrayList<StrategicDiagramItem> getItems() {

    return this.items;
  }

  /**
   * The numner of elements in the strategic diagram.
   *
   * @return the numner of elements in the strategic diagram.
   */
  public int getItemsCount() {

    return this.items.size();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
