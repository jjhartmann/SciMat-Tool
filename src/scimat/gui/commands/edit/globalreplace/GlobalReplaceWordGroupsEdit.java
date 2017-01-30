/*
 * GlobalReplaceWordGroupsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinWordGroupEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateWordGroupEdit;
import scimat.model.knowledgebase.dao.WordGroupDAO;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceWordGroupsEdit extends KnowledgeBaseEdit {

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
  public GlobalReplaceWordGroupsEdit(String findText, String replaceText, boolean findInGroupName) {
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
    ArrayList<WordGroup> wordGroups;
    WordGroup wordGroup;
    String groupName;
    WordGroupDAO wordGroupDAO;
    
    successful = false;
    
    wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      wordGroups = wordGroupDAO.getWordGroups();
      
      for (i = 0; i < wordGroups.size(); i++) {
        
        joined = false;
        updated = false;
      
        wordGroup = wordGroups.get(i);
        
        if (findInGroupName) {
        
          groupName = wordGroup.getGroupName().replaceAll(findText, replaceText);
          
          System.out.println("group name: " + groupName + " ##### " + "wordGroup: " + wordGroup.getGroupName());
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! groupName.equals(wordGroup.getGroupName()));
          
        } else {
        
          groupName = wordGroup.getGroupName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (wordGroupDAO.checkWordGroup(groupName)) {
          
            ArrayList<WordGroup> tmpArray = new ArrayList<WordGroup>();
            tmpArray.add(wordGroup);
            
            successful = new JoinWordGroupEdit(tmpArray, wordGroupDAO.getWordGroup(groupName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateWordGroupEdit(wordGroup.getWordGroupID(), groupName, wordGroup.isStopGroup()).execute();
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
