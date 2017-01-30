/*
 * DocumentSlaveAffiliationsPanel.java
 *
 * Created on 21-mar-2011, 19:19:37
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.tablemodel.AffiliationsTableModel;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.DocumentRelationAffiliationObserver;
import scimat.project.observer.EntityObserver;

/**
 *
 * @author mjcobo
 */
public class DocumentSlaveAffiliationsPanel 
        extends GenericSlaveListPanel<Document, Affiliation>
        implements DocumentRelationAffiliationObserver, EntityObserver<Affiliation>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public DocumentSlaveAffiliationsPanel() {
    super(new AffiliationsTableModel());

    CurrentProject.getInstance().getKbObserver().addDocumentsRelationAffiliationsObservers(this);
    CurrentProject.getInstance().getKbObserver().addAffiliationObserver(this);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void setMasterItem(Document document) {

    this.masterItem = document;

    try {

      if (this.masterItem != null) {

        relationChanged();

      } else {

        this.refreshData(new ArrayList<Affiliation>());

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
  public void entityAdded(ArrayList<Affiliation> items) throws KnowledgeBaseException {
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
  public void entityRemoved(ArrayList<Affiliation> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityUpdated(ArrayList<Affiliation> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   * 
   * @throws KnowledgeBaseException
   */
  public void relationChanged() throws KnowledgeBaseException {

    if (this.masterItem != null) {
      
      refreshData(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getAffiliations(this.masterItem.getDocumentID()));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
