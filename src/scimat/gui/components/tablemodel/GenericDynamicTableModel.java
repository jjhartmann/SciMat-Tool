/*
 * GenericListTableModel.java
 *
 * Created on 10-mar-2011, 12:40:44
 */
package scimat.gui.components.tablemodel;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;
import scimat.gui.components.observer.ElementsCountObserver;

/**
 * <p>This class implements a TableModel of an specific Entity type. The class
 * implements all the abstract method of {@link AbstractTableModel} except
 * the {@code getValueAt} method.</p>
 *
 * <p>The class incorporate methods to add, remove or update items. Also it has a
 * method to refresh all the data. Usually this methods are called through the
 * observer's method of the particulary Entity.</p>
 *
 * <p>The number of elements contained in the table is observer through
 * {@link ElementsCountObserver}.</p>
 *
 * @author mjcobo
 * @param <E> The principal element of the table, specifically the element
 * must be an Entity of the model. It must be comparable.
 */
public abstract class GenericDynamicTableModel<E extends Comparable<E>> extends GenericTableModel<E> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param columnNames
   */
  public GenericDynamicTableModel(String[] columnNames) {
    super(columnNames);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param items 
   */
  public void addItems(ArrayList<E> items) {

    int i;
    int position;
    E item;

    for (i = 0; i < items.size(); i++) {

      item = items.get(i);
      
      position = Collections.binarySearch(this.data, item);

      if (position < 0) {

        position = -position - 1;

        this.data.add(position, item);

        fireTableRowsInserted(position, position);

        fireElementsCountChange();
      }

    }
  }

  /**
   *
   * @param items
   */
  public void removeItems(ArrayList<E> items) {

    int i;
    int position;
    E item;

    for (i = 0; i < items.size(); i++) {

      item = items.get(i);

      position = Collections.binarySearch(this.data, item);

      if (position >= 0) {

        this.data.remove(position);

        fireTableRowsDeleted(position, position);

        fireElementsCountChange();

      }

    }
  }

  /**
   *
   * @param items
   */
  public void updateItems(ArrayList<E> items) {

    int i;
    int position;
    E item;

    for (i = 0; i < items.size(); i++) {

      item = items.get(i);

      position = Collections.binarySearch(this.data, item);
      
      if (position >= 0) {

        this.data.set(position, item);
        fireTableRowsUpdated(position, position);
      }

    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
