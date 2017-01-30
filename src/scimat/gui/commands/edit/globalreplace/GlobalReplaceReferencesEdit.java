/*
 * GlobalReplaceReferencesEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinReferenceEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateReferenceEdit;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceReferencesEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInFullReference;
  private boolean findInVolume;
  private boolean findInIssue;
  private boolean findInPage;
  private boolean findInDoi;
  private boolean findInFormat;
  private boolean findInYear;

  
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInFullReference
   * @param findInVolume
   * @param findInIssue
   * @param findInPage
   * @param findInDoi
   * @param findInFormat
   * @param findInYear 
   */
  public GlobalReplaceReferencesEdit(String findText, String replaceText, boolean findInFullReference, boolean findInVolume, boolean findInIssue, boolean findInPage, boolean findInDoi, boolean findInFormat, boolean findInYear) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInFullReference = findInFullReference;
    this.findInVolume = findInVolume;
    this.findInIssue = findInIssue;
    this.findInPage = findInPage;
    this.findInDoi = findInDoi;
    this.findInFormat = findInFormat;
    this.findInYear = findInYear;
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
    ArrayList<Reference> references;
    Reference reference;
    String fullReference, volume, issue, page, doi, format, year;
    ReferenceDAO referenceDAO;
    
    successful = false;
    
    referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      references = referenceDAO.getReferences();
      
      for (i = 0; i < references.size(); i++) {
        
        joined = false;
        updated = false;
      
        reference = references.get(i);
        
        if (findInFullReference) {
        
          fullReference = reference.getFullReference().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! fullReference.equals(reference.getFullReference()));
        
        } else {
        
          fullReference = reference.getFullReference();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (referenceDAO.checkReference(fullReference)) {
          
            ArrayList<Reference> tmpArray = new ArrayList<Reference>();
            tmpArray.add(reference);
            
            successful = new JoinReferenceEdit(tmpArray, referenceDAO.getReference(fullReference)).execute();
            joined = true;
            
          }
        } 
        
        if (!joined && findInVolume) {
        
          volume = reference.getVolume().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! volume.equals(reference.getVolume()));
        
        } else {
        
          volume = reference.getVolume();
          
          updated = updated || false;
        }
        
        if (!joined && findInIssue) {
        
          issue = reference.getIssue().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! issue.equals(reference.getIssue()));
        
        } else {
        
          issue = reference.getIssue();
          
          updated = updated || false;
        }
        
        if (!joined && findInPage) {
        
          page = reference.getPage().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! page.equals(reference.getPage()));
        
        } else {
        
          page = reference.getPage();
          
          updated = updated || false;
        }
        
        if (!joined && findInDoi) {
        
          doi = reference.getDoi().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! doi.equals(reference.getDoi()));
        
        } else {
        
          doi = reference.getDoi();
          
          updated = updated || false;
        }
        
        if (!joined && findInFormat) {
        
          format = reference.getFormat().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! format.equals(reference.getFormat()));
        
        } else {
        
          format = reference.getFormat();
          
          updated = updated || false;
        }
        
        if (!joined && findInYear) {
        
          year = reference.getYear().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! year.equals(reference.getYear()));
        
        } else {
        
          year = reference.getYear();
          
          updated = updated || false;
        }
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateReferenceEdit(reference.getReferenceID(), 
                  fullReference, 
                  volume, 
                  issue, 
                  page, 
                  year, 
                  doi, 
                  format).execute();
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
