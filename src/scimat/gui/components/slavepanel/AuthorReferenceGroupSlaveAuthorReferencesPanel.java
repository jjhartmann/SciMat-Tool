/*
 * AuthorReferenceGroupSlaveAuthorReferencesPanel.java
 *
 * Created on 21-mar-2011, 19:19:37
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.tablemodel.AuthorReferencesTableModel;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.AuthorReferenceGroupRelationAuthorReferenceObserver;
import scimat.project.observer.EntityObserver;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupSlaveAuthorReferencesPanel 
        extends GenericSlaveListPanel<AuthorReferenceGroup, AuthorReference>
        implements AuthorReferenceGroupRelationAuthorReferenceObserver, EntityObserver<AuthorReference>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/


  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public AuthorReferenceGroupSlaveAuthorReferencesPanel() {
    super(new AuthorReferencesTableModel());

    CurrentProject.getInstance().getKbObserver().addAuthorReferenceGroupRelationAuthorReferenceObserver(this);
    CurrentProject.getInstance().getKbObserver().addAuthorReferenceObserver(this);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void setMasterItem(AuthorReferenceGroup authorReferenceGroup) {

    this.masterItem = authorReferenceGroup;

    try {

      if (this.masterItem != null) {

        relationChanged();

      } else {

        this.refreshData(new ArrayList<AuthorReference>());

      }

    } catch (KnowledgeBaseException e) {
    
      ErrorDialogManager.getInstance().showException(e);

    }
  }

  /**
   *
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityAdded(ArrayList<AuthorReference> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   *
   * @param entity
   * @throws KnowledgeBaseException
   */
  public void entityRefresh() throws KnowledgeBaseException {

    relationChanged();
  }

  /**
   *
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityRemoved(ArrayList<AuthorReference> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityUpdated(ArrayList<AuthorReference> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @throws KnowledgeBaseException
   */
  public void relationChanged() throws KnowledgeBaseException {

    if (this.masterItem != null) {
      
      refreshData(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().getAuthorReferences(this.masterItem.getAuthorReferenceGroupID()));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
