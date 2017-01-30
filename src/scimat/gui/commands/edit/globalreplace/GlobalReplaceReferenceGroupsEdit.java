/*
 * GlobalReplaceReferenceGroupsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinReferenceGroupEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateReferenceGroupEdit;
import scimat.model.knowledgebase.dao.ReferenceGroupDAO;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceReferenceGroupsEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInGroupName;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInGroupName 
   */
  public GlobalReplaceReferenceGroupsEdit(String findText, String replaceText, boolean findInGroupName) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInGroupName = findInGroupName;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * Exxecute the task
   */
  @Override
  public boolean  execute() throws KnowledgeBaseException {
    
    int i;
    boolean successful, joined, updated;
    ArrayList<ReferenceGroup> referenceGroups;
    ReferenceGroup referenceGroup;
    String groupName;
    ReferenceGroupDAO referenceGroupDAO;
    
    successful = false;
    
    referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      referenceGroups = referenceGroupDAO.getReferenceGroups();
      
      for (i = 0; i < referenceGroups.size(); i++) {
        
        joined = false;
        updated = false;
      
        referenceGroup = referenceGroups.get(i);
        
        if (findInGroupName) {
        
          groupName = referenceGroup.getGroupName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! groupName.equals(referenceGroup.getGroupName()));
          
        } else {
        
          groupName = referenceGroup.getGroupName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (referenceGroupDAO.checkReferenceGroup(groupName)) {
          
            ArrayList<ReferenceGroup> tmpArray = new ArrayList<ReferenceGroup>();
            tmpArray.add(referenceGroup);
            
            successful = new JoinReferenceGroupEdit(tmpArray, referenceGroupDAO.getReferenceGroup(groupName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateReferenceGroupEdit(referenceGroup.getReferenceGroupID(), groupName, referenceGroup.isStopGroup()).execute();
        }
        
      }

    } catch (KnowledgeBaseException e) {
      
      successful = false;
      this.errorMessage = "An exception happened.";

      throw e;
    }
    
    return successful;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
