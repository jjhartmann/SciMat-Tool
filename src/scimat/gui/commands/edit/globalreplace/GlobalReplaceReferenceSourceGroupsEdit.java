/*
 * GlobalReplaceReferenceSourceGroupsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinReferenceSourceGroupEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateReferenceSourceGroupEdit;
import scimat.model.knowledgebase.dao.ReferenceSourceGroupDAO;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceReferenceSourceGroupsEdit extends KnowledgeBaseEdit {

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
  public GlobalReplaceReferenceSourceGroupsEdit(String findText, String replaceText, boolean findInGroupName) {
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
    ArrayList<ReferenceSourceGroup> referenceSourceGroups;
    ReferenceSourceGroup referenceSourceGroup;
    String groupName;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    
    successful = false;
    
    referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      referenceSourceGroups = referenceSourceGroupDAO.getReferenceSourceGroups();
      
      for (i = 0; i < referenceSourceGroups.size(); i++) {
    
        joined = false;
        updated = false;
      
        referenceSourceGroup = referenceSourceGroups.get(i);
        
        if (findInGroupName) {
        
          groupName = referenceSourceGroup.getGroupName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! groupName.equals(referenceSourceGroup.getGroupName()));
          
        } else {
        
          groupName = referenceSourceGroup.getGroupName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (referenceSourceGroupDAO.checkReferenceSourceGroup(groupName)) {
          
            ArrayList<ReferenceSourceGroup> tmpArray = new ArrayList<ReferenceSourceGroup>();
            tmpArray.add(referenceSourceGroup);
            
            successful = new JoinReferenceSourceGroupEdit(tmpArray, referenceSourceGroupDAO.getReferenceSourceGroup(groupName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateReferenceSourceGroupEdit(referenceSourceGroup.getReferenceSourceGroupID(), groupName, referenceSourceGroup.isStopGroup()).execute();
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
