/*
 * AuthorGroupManualSetManager.java
 *
 * Created on 23-mar-2011, 21:49:34
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.add.AddAuthorsToAuthorGroupEdit;
import scimat.gui.commands.edit.delete.DeleteAuthorEdit;
import scimat.gui.commands.edit.delete.DeleteAuthorGroupEdit;
import scimat.gui.commands.edit.delete.DeleteAuthorsFromAuthorGroupEdit;
import scimat.gui.commands.edit.move.MoveAuthorToDifferentAuthorGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.editdialog.EditDialogManager;
import scimat.gui.components.itemslist.AuthorGroupsListPanel;
import scimat.gui.components.itemslist.AuthorWithoutGroupsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.gui.components.movetogroup.MoveToGroupDialogManager;
import scimat.gui.components.slavepanel.AuthorGroupSlaveAuthorsPanel;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorGroupManualSetManager extends GenericManualSetGroupPanel<AuthorGroup, Author>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public AuthorGroupManualSetManager() {
    super(new AuthorGroupsListPanel(),
          new AuthorWithoutGroupsListPanel(),
          new AuthorGroupSlaveAuthorsPanel());

    setDescription("Author groups", "Authors of the group", "Authors without group");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void addGroupAction() {

    AddDialogManager.getInstance().showAddAuthorGroupDialog();
  }

  /**
   * 
   * @param group 
   */
  public void editGroupAction(AuthorGroup group) {

    EditDialogManager.getInstance().showEditAuthorGroupDialog(group);
  }

  /**
   * 
   * @param groups 
   */
  public void moveGroupToAction(ArrayList<AuthorGroup> groups) {

    JoinEntitiesDialogManager.getInstance().showAuthorGroupsJoinDialog(groups);
  }

  /**
   * 
   * @param groups 
   */
  public void deleteGroupAction(ArrayList<AuthorGroup> groups) {

    (new PerformKnowledgeBaseEditTask(new DeleteAuthorGroupEdit(groups), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toNewGroupAction(ArrayList<Author> itemsWithoutGroup) {

    MoveToGroupDialogManager.getInstance().showMoveAuthorsToNewGroupDialog(itemsWithoutGroup);
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toDifferentGroupAction(ArrayList<Author> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new MoveAuthorToDifferentAuthorGroupEdit(itemsWithoutGroup), this)).execute();
  }

  /**
   * 
   * @param itemsFromGroup
   * @param group 
   */
  public void removeItemFromGroupAction(ArrayList<Author> itemsFromGroup, AuthorGroup group) {

    (new PerformKnowledgeBaseEditTask(new DeleteAuthorsFromAuthorGroupEdit(itemsFromGroup, group), this)).execute();
  }

  /**
   * 
   * @param group
   * @param itemsWithoutGroup 
   */
  public void addItemToGroupAction(AuthorGroup group, ArrayList<Author> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new AddAuthorsToAuthorGroupEdit(itemsWithoutGroup, group), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  @Override
  public void removeItemsWithoutGroup(ArrayList<Author> itemsWithoutGroup) {
    (new PerformKnowledgeBaseEditTask(new DeleteAuthorEdit(itemsWithoutGroup), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
