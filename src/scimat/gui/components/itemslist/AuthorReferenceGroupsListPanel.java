/*
 * AuthorReferenceGroupsListPanel.java
 *
 * Created on 17-nov-2011, 19:38:33
 */
package scimat.gui.components.itemslist;

import java.util.ArrayList;
import scimat.gui.components.tablemodel.AuthorReferenceGroupsTableModel;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.EntityObserver;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupsListPanel extends GenericDynamicItemsListPanel<AuthorReferenceGroup> implements EntityObserver<AuthorReferenceGroup> {

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
  public AuthorReferenceGroupsListPanel() {
    super(new AuthorReferenceGroupsTableModel());
    
    CurrentProject.getInstance().getKbObserver().addAuthorReferenceGroupObserver(this);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityAdded(ArrayList<AuthorReferenceGroup> items) throws KnowledgeBaseException {
    addItems(items);
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityRemoved(ArrayList<AuthorReferenceGroup> items) throws KnowledgeBaseException {
    removeItems(items);
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException 
   */
  public void entityUpdated(ArrayList<AuthorReferenceGroup> items) throws KnowledgeBaseException {
    updateItems(items);
  }

  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void entityRefresh() throws KnowledgeBaseException {
    refreshItems(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().getAuthorReferenceGroups());
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
