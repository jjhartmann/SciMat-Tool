/*
 * DocumentManager.java
 *
 * Created on 13-mar-2011, 16:04:49
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteDocumentEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.DocumentGlobalSlavePanel;
import scimat.gui.components.itemslist.DocumentsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.Document;

/**
 *
 * @author mjcobo
 */
public class DocumentManager extends GenericItemManagerPanel<Document> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public DocumentManager() {
    super(new DocumentsListPanel(),
          new DocumentGlobalSlavePanel());

    setMasterPanelTitle("Documents list");
    setSlavePanelTitle("Document detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddDocumentDialog();
  }

  @Override
  public void moveToAction(ArrayList<Document> items) {
    JoinEntitiesDialogManager.getInstance().showDocumentsJoinDialog(items);
  }

  @Override
  public void removeAction(ArrayList<Document> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteDocumentEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
