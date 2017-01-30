/*
 * GlobalReplaceAuthorReferenceGroupsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinAuthorReferenceGroupEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateAuthorReferenceGroupEdit;
import scimat.model.knowledgebase.dao.AuthorReferenceGroupDAO;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceAuthorReferenceGroupsEdit extends KnowledgeBaseEdit {

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
  public GlobalReplaceAuthorReferenceGroupsEdit(String findText, String replaceText, boolean findInGroupName) {
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
    ArrayList<AuthorReferenceGroup> authorReferenceGroups;
    AuthorReferenceGroup authorReferenceGroup;
    String groupName;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    
    successful = false;
    
    authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      authorReferenceGroups = authorReferenceGroupDAO.getAuthorReferenceGroups();
      
      for (i = 0; i < authorReferenceGroups.size(); i++) {
        
        joined = false;
        updated = false;
      
        authorReferenceGroup = authorReferenceGroups.get(i);
        
        if (findInGroupName) {
        
          groupName = authorReferenceGroup.getGroupName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! groupName.equals(authorReferenceGroup.getGroupName()));
          
        } else {
        
          groupName = authorReferenceGroup.getGroupName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (authorReferenceGroupDAO.checkAuthorReferenceGroup(groupName)) {
          
            ArrayList<AuthorReferenceGroup> tmpArray = new ArrayList<AuthorReferenceGroup>();
            tmpArray.add(authorReferenceGroup);
            
            successful = new JoinAuthorReferenceGroupEdit(tmpArray, authorReferenceGroupDAO.getAuthorReferenceGroup(groupName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateAuthorReferenceGroupEdit(authorReferenceGroup.getAuthorReferenceGroupID(), groupName, authorReferenceGroup.isStopGroup()).execute();
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
