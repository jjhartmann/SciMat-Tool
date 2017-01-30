/*
 * AuthorManager.java
 *
 * Created on 13-mar-2011, 17:15:25
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteAuthorEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.AuthorGlobalSlavePanel;
import scimat.gui.components.itemslist.AuthorsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.Author;

/**
 *
 * @author mjcobo
 */
public class AuthorManager extends GenericItemManagerPanel<Author> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AuthorManager() {
    super(new AuthorsListPanel(), 
          new AuthorGlobalSlavePanel());

    setMasterPanelTitle("Authors list");
    setSlavePanelTitle("Author detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddAuthorDialog();
  }

  /**
   * 
   * @param items 
   */
  @Override
  public void moveToAction(ArrayList<Author> items) {
    JoinEntitiesDialogManager.getInstance().showAuthorsJoinDialog(items);
  }

  /**
   * 
   * @param items 
   */
  @Override
  public void removeAction(ArrayList<Author> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteAuthorEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
