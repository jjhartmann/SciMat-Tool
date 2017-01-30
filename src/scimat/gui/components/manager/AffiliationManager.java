/*
 * AffiliationManager.java
 *
 * Created on 13-mar-2011, 17:15:41
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteAffiliationEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.AffiliationGlobalSlavePanel;
import scimat.gui.components.itemslist.AffiliationsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.Affiliation;

/**
 *
 * @author mjcobo
 */
public class AffiliationManager extends GenericItemManagerPanel<Affiliation> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public AffiliationManager() {
    super(new AffiliationsListPanel(), 
          new AffiliationGlobalSlavePanel());

    setMasterPanelTitle("Affiliations list");
    setSlavePanelTitle("Affiliation detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddAffiliationDialog();
  }

  /**
   *
   * @param items
   */
  @Override
  public void moveToAction(ArrayList<Affiliation> items) {
    JoinEntitiesDialogManager.getInstance().showAffiliationsJoinDialog(items);
  }

  /**
   * 
   * @param items
   */
  @Override
  public void removeAction(ArrayList<Affiliation> items) {

    (new PerformKnowledgeBaseEditTask(new DeleteAffiliationEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
