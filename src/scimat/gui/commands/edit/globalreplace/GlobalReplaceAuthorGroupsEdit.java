/*
 * GlobalReplaceAuthorGroupsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinAuthorGroupEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateAuthorGroupEdit;
import scimat.model.knowledgebase.dao.AuthorGroupDAO;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceAuthorGroupsEdit extends KnowledgeBaseEdit {

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
  public GlobalReplaceAuthorGroupsEdit(String findText, String replaceText, boolean findInGroupName) {
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
    ArrayList<AuthorGroup> authorGroups;
    AuthorGroup authorGroup;
    String groupName;
    AuthorGroupDAO authorGroupDAO;
    
    successful = false;
    
    authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      authorGroups = authorGroupDAO.getAuthorGroups();
      
      for (i = 0; i < authorGroups.size(); i++) {
        
        joined = false;
        updated = false;
      
        authorGroup = authorGroups.get(i);
        
        if (findInGroupName) {
        
          groupName = authorGroup.getGroupName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! groupName.equals(authorGroup.getGroupName()));
          
        } else {
        
          groupName = authorGroup.getGroupName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (authorGroupDAO.checkAuthorGroup(groupName)) {
          
            ArrayList<AuthorGroup> tmpArray = new ArrayList<AuthorGroup>();
            tmpArray.add(authorGroup);
            
            successful = new JoinAuthorGroupEdit(tmpArray, authorGroupDAO.getAuthorGroup(groupName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateAuthorGroupEdit(authorGroup.getAuthorGroupID(), groupName, authorGroup.isStopGroup()).execute();
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
