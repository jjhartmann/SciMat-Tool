/*
 * GenericTableModel.java
 *
 * Created on 17-nov-2011, 17:10:22
 */
package scimat.gui.components.tablemodel;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import scimat.gui.components.observer.ElementsCountObserver;

/**
 *
 * @author mjcobo
 * @param <E>
 */
public abstract class GenericTableModel<E> extends AbstractTableModel {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Names of the table's columns.
   */
  private final String[] columnNames;

  /**
   * A copy of the TreeSet. It is used to access to a particular position of
   * the set. That is, to acces to the i-element of the set.
   */
  protected ArrayList<E> data;

  private final ArrayList<ElementsCountObserver> elementsCountObservers;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param columnNames
   */
  public GenericTableModel(String[] columnNames) {

    this.columnNames = columnNames;
    
    this.data = new ArrayList<E>();
    this.elementsCountObservers = new ArrayList<ElementsCountObserver>();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @return
   */
  @Override
  public int getColumnCount() {
    return this.columnNames.length;
  }

  /**
   *
   * @return
   */
  @Override
  public int getRowCount() {
    return this.data.size();
  }

  /**
   *
   * @param columnIndex
   * @return
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {

    if (getRowCount() > 0){
      return getValueAt(0, columnIndex).getClass();
    }else{
      return Void.class;
    }
  }

  /**
   *
   * @param column
   * @return
   */
  @Override
  public String getColumnName(int column) {

    if ((column >= 0) && (column < this.columnNames.length)) {
      return this.columnNames[column];
    } else {
      return "";
    }
  }

  /**
   *
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }
  
  /**
   *
   * @param rowIndex
   * @return
   */
  public E getItem(int rowIndex) {

    return this.data.get(rowIndex);
  }
  
  /**
   * 
   * @return
   */
  public ArrayList<E> getItems() {

    return new ArrayList<E>(data);
  }
  
  /**
   * 
   * @param items 
   */
  public void refreshItems(ArrayList<E> items) {

    this.data = items;

    fireTableDataChanged();

    fireElementsCountChange();
  }

  /**
   *
   * @param observer
   */
  public void addElementsCountObserver(ElementsCountObserver observer) {

    this.elementsCountObservers.add(observer);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * 
   */
  public void fireElementsCountChange() {

    for (ElementsCountObserver elementsCountObserver : this.elementsCountObservers) {
      elementsCountObserver.elementsCountChanged(this.data.size());
    }
  }
}
