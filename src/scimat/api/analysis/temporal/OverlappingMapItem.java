/*
 * OverlappingMapItem.java
 *
 * Created on 08-may-2011, 20:23:52
 */
package scimat.api.analysis.temporal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mjcobo
 */
public class OverlappingMapItem implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Integer> items;
  private ArrayList<Integer> newItems;
  private ArrayList<Integer> dissapearedItems;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param items
   * @param newItems
   * @param dissapearedItems
   */
  public OverlappingMapItem(ArrayList<Integer> items,
          ArrayList<Integer> newItems, ArrayList<Integer> dissapearedItems) {
    
    this.items = items;
    this.newItems = newItems;
    this.dissapearedItems = dissapearedItems;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public int getItemsCount() {

    return this.items.size();
  }

  /**
   *
   * @return
   */
  public ArrayList<Integer> getItems() {
    return items;
  }

  public void setItems(ArrayList<Integer> items) {
    this.items = items;
  }

  /**
   *
   * @return
   */
  public int getNewItemsCount() {

    return this.newItems.size();
  }

  public void setNewItems(ArrayList<Integer> newItems) {
    this.newItems = newItems;
  }

  /**
   * 
   * @return
   */
  public ArrayList<Integer> getNewItems() {
    return newItems;
  }
  
  /**
   * 
   */
  public int getDissapearedItemsCount() {

    return this.dissapearedItems.size();
  }

  /**
   * 
   * @return
   */
  public ArrayList<Integer> getDissapearedItems() {
    return dissapearedItems;
  }

  public void setDissapearedItems(ArrayList<Integer> dissapearedItems) {
    this.dissapearedItems = dissapearedItems;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
