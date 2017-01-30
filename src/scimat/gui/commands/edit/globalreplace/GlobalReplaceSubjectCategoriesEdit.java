/*
 * GlobalReplaceSubjectCategoriesEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinSubjectCategoryEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateSubjectCategoryEdit;
import scimat.model.knowledgebase.dao.SubjectCategoryDAO;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceSubjectCategoriesEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInSubjectCategoryName;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInSubjectCategoryName 
   */
  public GlobalReplaceSubjectCategoriesEdit(String findText, String replaceText, boolean findInSubjectCategoryName) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInSubjectCategoryName = findInSubjectCategoryName;
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
    ArrayList<SubjectCategory> subjectCategoryies;
    SubjectCategory subjectCategory;
    String subjectCategoryName;
    SubjectCategoryDAO subjectCategoryDAO;
    
    successful = false;
    
    subjectCategoryDAO = CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      subjectCategoryies = subjectCategoryDAO.getSubjectCategories();
      
      for (i = 0; i < subjectCategoryies.size(); i++) {
        
        joined = false;
        updated = false;
      
        subjectCategory = subjectCategoryies.get(i);
        
        if (findInSubjectCategoryName) {
        
          subjectCategoryName = subjectCategory.getSubjectCategoryName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! subjectCategoryName.equals(subjectCategory.getSubjectCategoryName()));
          
        } else {
        
          subjectCategoryName = subjectCategory.getSubjectCategoryName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (subjectCategoryDAO.checkSubjectCategory(subjectCategoryName)) {
          
            ArrayList<SubjectCategory> tmpArray = new ArrayList<SubjectCategory>();
            tmpArray.add(subjectCategory);
            
            successful = new JoinSubjectCategoryEdit(tmpArray, subjectCategoryDAO.getSubjectCategory(subjectCategoryName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateSubjectCategoryEdit(subjectCategory.getSubjectCategoryID(), subjectCategoryName).execute();
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
