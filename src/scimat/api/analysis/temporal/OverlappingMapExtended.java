/*
 * OverlappingMap.java
 *
 * Created on 08-may-2011, 17:31:56
 */
package scimat.api.analysis.temporal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author mjcobo
 */
public class OverlappingMapExtended implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<OverlappingMapItem> overlappingMapItems;
  private ArrayList<ArrayList<Integer>> sharedItems;
  private ArrayList<Double> overlappingWeights;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param overlappingMapItem
   */
  public OverlappingMapExtended() {

    this.overlappingMapItems = new ArrayList<OverlappingMapItem>();
    this.sharedItems = new ArrayList<ArrayList<Integer>>();
    this.overlappingWeights = new ArrayList<Double>();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param items
   * @param measure
   */
  public void addItem(ArrayList<Integer> items, OverlappingMeasure measure) {

    OverlappingMapItem itemToAdd, lastItem;
    TreeSet<Integer> shared, dissapeared, newItems;

    if (this.overlappingMapItems.isEmpty()) {

      this.overlappingMapItems.add(new OverlappingMapItem(items, null, null));
      
    } else {

      lastItem = this.overlappingMapItems.get(this.overlappingMapItems.size() - 1);
      
      itemToAdd = new OverlappingMapItem(items, null, null);
      this.overlappingMapItems.add(itemToAdd);

      // Shared elements
      shared = new TreeSet<Integer>(lastItem.getItems());
      shared.retainAll(itemToAdd.getItems());

      // New items
      newItems = new TreeSet<Integer>(itemToAdd.getItems());
      newItems.removeAll(lastItem.getItems());

      // Dissapeared items
      dissapeared = new TreeSet<Integer>(lastItem.getItems());
      dissapeared.removeAll(itemToAdd.getItems());

      lastItem.setDissapearedItems(new ArrayList<Integer>(dissapeared));
      itemToAdd.setNewItems(new ArrayList<Integer>(newItems));
      this.sharedItems.add(new ArrayList<Integer>(shared));
      this.overlappingWeights.add(measure.calculateOverlapping(lastItem.getItems(), itemToAdd.getItems()));
    }
  }
  
  public int getPeriodCount() {
   
    return this.overlappingMapItems.size();
  }

  /**
   * 
   * @param sourcePeriod
   * @return
   */
  public int getItemsCountInPeriod(int sourcePeriod) {

    return this.overlappingMapItems.get(sourcePeriod).getItemsCount();
  }
  
  public ArrayList<Integer> getItemsInPeriod(int sourcePeriod) {

    return this.overlappingMapItems.get(sourcePeriod).getItems();
  }

  /**
   *
   * @param sourcePeriod
   * @return
   */
  public int getNewItemsCountInPeriod(int sourcePeriod) {

    return this.overlappingMapItems.get(sourcePeriod).getNewItemsCount();
  }
  
  public ArrayList<Integer> getNewItemsInPeriod(int sourcePeriod) {

    return this.overlappingMapItems.get(sourcePeriod).getNewItems();
  }

  /**
   *
   * @param sourcePeriod
   * @return
   */
  public int getDissapearedItemsCountInPeriod(int sourcePeriod) {

    return this.overlappingMapItems.get(sourcePeriod).getDissapearedItemsCount();
  }
  
  public ArrayList<Integer> getDissapearedItemsInPeriod(int sourcePeriod) {

    return this.overlappingMapItems.get(sourcePeriod).getDissapearedItems();
  }

  /**
   * 
   * @param sourcePeriod
   * @return
   */
  public double getOverlappingWeight(int sourcePeriod) {

    return this.overlappingWeights.get(sourcePeriod);
  }

  /**
   * 
   * @param sourcePeriod
   * @return
   */
  public int getOverlappedItemsCountInPeriod(int sourcePeriod) {

    return this.sharedItems.get(sourcePeriod).size();
  }
  
  public ArrayList<Integer> getOverlappedItemsInPeriod(int sourcePeriod) {

    return this.sharedItems.get(sourcePeriod);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
