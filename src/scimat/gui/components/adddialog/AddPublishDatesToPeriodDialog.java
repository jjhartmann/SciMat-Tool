/*
 * AddPublishDatesToPeriodDialog.java
 *
 * Created on 26-may-2011, 19:12:26
 */
package scimat.gui.components.adddialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddPublishDatesToPeriodEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectManyItemsPanel;
import scimat.gui.components.tablemodel.PublishDatesTableModel;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddPublishDatesToPeriodDialog extends GenericAddItemsDialog<Period, PublishDate> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  public AddPublishDatesToPeriodDialog(JFrame frame) {
    super(frame, 
          new GenericSelectManyItemsPanel<PublishDate>(new GenericDynamicItemsListPanel<PublishDate>(new PublishDatesTableModel()), 
                                                           new GenericDynamicItemsListPanel<PublishDate>(new PublishDatesTableModel())));
  }
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param masterItem
   * @param itemsToAdd 
   */
  @Override
  public void addAction(Period masterItem, ArrayList<PublishDate> itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddPublishDatesToPeriodEdit(masterItem, itemsToAdd), rootPane)).execute();
    
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddPublishDateDialog();
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addPublishDateObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removePublishDateObserver(this);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
