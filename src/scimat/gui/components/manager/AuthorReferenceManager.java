/*
 * AuthorReferenceManager.java
 *
 * Created on 13-mar-2011, 17:17:08
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteAuthorReferenceEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.AuthorReferenceGlobalSlavePanel;
import scimat.gui.components.itemslist.AuthorReferencesListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.AuthorReference;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceManager extends GenericItemManagerPanel<AuthorReference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AuthorReferenceManager() {
    super(new AuthorReferencesListPanel(),
          new AuthorReferenceGlobalSlavePanel());

    setMasterPanelTitle("Authors-reference list");
    setSlavePanelTitle("Authors-reference detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddAuthorReferenceDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<AuthorReference> items) {
    JoinEntitiesDialogManager.getInstance().showAuthorReferencesJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<AuthorReference> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteAuthorReferenceEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
