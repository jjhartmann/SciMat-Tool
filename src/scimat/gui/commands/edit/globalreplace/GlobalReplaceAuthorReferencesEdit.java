/*
 * GlobalReplaceAuthorReferencesEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinAuthorReferenceEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateAuthorReferenceEdit;
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceAuthorReferencesEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInAuthorName;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInAuthorName 
   */
  public GlobalReplaceAuthorReferencesEdit(String findText, String replaceText, boolean findInAuthorName) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInAuthorName = findInAuthorName;
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
    ArrayList<AuthorReference> authorReferences;
    AuthorReference authorReference;
    String authorName;
    AuthorReferenceDAO authorReferenceDAO;
    
    successful = false;
    
    authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      authorReferences = authorReferenceDAO.getAuthorReferences();
      
      for (i = 0; i < authorReferences.size(); i++) {
                
        joined = false;
        updated = false;
      
        authorReference = authorReferences.get(i);
        
        if (findInAuthorName) {
        
          authorName = authorReference.getAuthorName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! authorName.equals(authorReference.getAuthorName()));
          
        } else {
        
          authorName = authorReference.getAuthorName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (authorReferenceDAO.checkAuthorReference(authorName)) {
          
            ArrayList<AuthorReference> tmpArray = new ArrayList<AuthorReference>();
            tmpArray.add(authorReference);
            
            successful = new JoinAuthorReferenceEdit(tmpArray, authorReferenceDAO.getAuthorReference(authorName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateAuthorReferenceEdit(authorReference.getAuthorReferenceID(), authorName).execute();
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
