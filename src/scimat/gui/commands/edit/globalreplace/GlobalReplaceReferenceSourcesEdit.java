/*
 * GlobalReplaceReferenceSourcesEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinReferenceSourceEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateReferenceSourceEdit;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceReferenceSourcesEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInSource;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInSource 
   */
  public GlobalReplaceReferenceSourcesEdit(String findText, String replaceText, boolean findInSource) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInSource = findInSource;
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
    ArrayList<ReferenceSource> referenceSources;
    ReferenceSource referenceSource;
    String source;
    ReferenceSourceDAO referenceSourceDAO;
    
    successful = false;
    
    referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      referenceSources = referenceSourceDAO.getReferenceSources();
      
      for (i = 0; i < referenceSources.size(); i++) {
        
        joined = false;
        updated = false;
      
        referenceSource = referenceSources.get(i);
        
        if (findInSource) {
        
          source = referenceSource.getSource().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! source.equals(referenceSource.getSource()));
          
        } else {
        
          source = referenceSource.getSource();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (referenceSourceDAO.checkReferenceSource(source)) {
          
            ArrayList<ReferenceSource> tmpArray = new ArrayList<ReferenceSource>();
            tmpArray.add(referenceSource);
            
            successful = new JoinReferenceSourceEdit(tmpArray, referenceSourceDAO.getReferenceSource(source)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateReferenceSourceEdit(referenceSource.getReferenceSourceID(), source).execute();
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
