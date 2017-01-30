/*
 * JournalsListPanel.java
 *
 * Created on 17-nov-2011, 19:38:33
 */
package scimat.gui.components.itemslist;

import java.util.ArrayList;
import scimat.gui.components.tablemodel.JournalsTableModel;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.EntityObserver;

/**
 *
 * @author mjcobo
 */
public class JournalsListPanel 
extends GenericDynamicItemsListPanel<Journal> 
implements EntityObserver<Journal> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/  

  /**
   * 
   * @param tableModel 
   */
  public JournalsListPanel() {
    super(new JournalsTableModel());
    
    CurrentProject.getInstance().getKbObserver().addJournalObserver(this);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityAdded(ArrayList<Journal> items) throws KnowledgeBaseException {
    addItems(items);
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityRemoved(ArrayList<Journal> items) throws KnowledgeBaseException {
    removeItems(items);
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityUpdated(ArrayList<Journal> items) throws KnowledgeBaseException {
    updateItems(items);
  }

  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void entityRefresh() throws KnowledgeBaseException {
    refreshItems(CurrentProject.getInstance().getFactoryDAO().getJournalDAO().getJournals());
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
