/*
 * ReferenceSourceSlaveReferencesPanel.java
 *
 * Created on 21-mar-2011, 19:19:37
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.tablemodel.ReferencesTableModel;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.EntityObserver;
import scimat.project.observer.ReferenceSourceRelationReferenceObserver;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceSlaveReferencesPanel 
        extends GenericSlaveListPanel<ReferenceSource, Reference>
        implements ReferenceSourceRelationReferenceObserver, EntityObserver<Reference>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public ReferenceSourceSlaveReferencesPanel() {
    super(new ReferencesTableModel());

    CurrentProject.getInstance().getKbObserver().addReferenceSourceRelationReferenceObserver(this);
    CurrentProject.getInstance().getKbObserver().addReferenceObserver(this);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void setMasterItem(ReferenceSource referenceSource) {

    this.masterItem = referenceSource;

    try {

      if (this.masterItem != null) {

        relationChanged();

      } else {

        this.refreshData(new ArrayList<Reference>());

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
  public void entityAdded(ArrayList<Reference> items) throws KnowledgeBaseException {
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
  public void entityRemoved(ArrayList<Reference> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityUpdated(ArrayList<Reference> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @throws KnowledgeBaseException
   */
  public void relationChanged() throws KnowledgeBaseException {

    if (this.masterItem != null) {
      
      refreshData(CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().getReferences(this.masterItem.getReferenceSourceID()));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
