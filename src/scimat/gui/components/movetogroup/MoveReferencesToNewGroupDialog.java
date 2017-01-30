/*
 * MoveReferencesToNewGroupDialog.java
 *
 * Created on 25-may-2011, 17:12:02
 */
package scimat.gui.components.movetogroup;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.move.MoveReferencesToNewReferenceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.ReferencesTableModel;
import scimat.model.knowledgebase.entity.Reference;

/**
 *
 * @author mjcobo
 */
public class MoveReferencesToNewGroupDialog extends GenericMoveToGroupDialog<Reference> {

  

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param frame 
   */
  public MoveReferencesToNewGroupDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Reference>(new ReferencesTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param item 
   */
  @Override
  public void setGroupNameFromItem(Reference item) {
    
    if (item != null) {
    
      super.setGroupNameText(item.getFullReference());
      
    } else {
    
      super.setGroupNameText("");
    }
  }

  /**
   * 
   * @param items
   * @param groupName 
   */
  @Override
  public void moveAction(ArrayList<Reference> items, String groupName) {
    
    (new PerformKnowledgeBaseEditTask(new MoveReferencesToNewReferenceGroupEdit(items, groupName), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
