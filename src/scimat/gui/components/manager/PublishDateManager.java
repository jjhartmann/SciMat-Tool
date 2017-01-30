/*
 * PublishDateManager.java
 *
 * Created on 13-mar-2011, 17:16:08
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeletePublishDateEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.PublishDateGlobalSlavePanel;
import scimat.gui.components.itemslist.PublishDatesListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.PublishDate;

/**
 *
 * @author mjcobo
 */
public class PublishDateManager extends GenericItemManagerPanel<PublishDate> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public PublishDateManager() {
    super(new PublishDatesListPanel(),
          new PublishDateGlobalSlavePanel());

    setMasterPanelTitle("Publish dates list");
    setSlavePanelTitle("Publish date detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddPublishDateDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<PublishDate> items) {
    JoinEntitiesDialogManager.getInstance().showPublishDatesJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<PublishDate> items) {
    (new PerformKnowledgeBaseEditTask(new DeletePublishDateEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
