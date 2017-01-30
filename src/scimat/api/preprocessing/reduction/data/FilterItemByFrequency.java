/*
 * FilterItemByFrequency.java
 *
 * Created on 08-feb-2011, 18:12:50
 */
package scimat.api.preprocessing.reduction.data;

import java.util.ArrayList;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 * This class implements a preprocessing method which removes all of the
 * items in a dataset with a frequency below a specific value.
 * 
 * @author mjcobo
 */
public class FilterItemByFrequency implements DataFilter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private int minFrequency;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Constructs a new filter which reduce the items of a given dataset. Only
   * the items with a frequency higher than {@code minFrequency} will stand
   * in the dataset after the filter will be applied. That is, a item will
   * stand in the dataset iff: {@code item.frequency >= minFrequency}.
   * 
   * @param minFrequency Minimun item's frequency allowed by the filer.
   */
  public FilterItemByFrequency(int minFrequency) {

   this.minFrequency = minFrequency;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Perform the filter.
   *
   * NOTE: the dataset will be modified.
   *
   * @param dataset Dataset on which the filter will be applied.
   */
  public void execute(Dataset dataset) {

    int i;
    Integer itemID;
    ArrayList<Integer> itemsList;

    itemsList = dataset.getItems();

    for (i = 0; i < itemsList.size(); i++) {

      itemID = itemsList.get(i);

      try {

        if (dataset.getItemFrequency(itemID) < this.minFrequency) {

          dataset.removeItem(itemID);
        }

      } catch (NotExistsItemException e) {

        System.err.println("An internal error occur within the dataset. "
                + "The item " + itemID + " does not exist.");
        e.printStackTrace(System.err);
      }
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
