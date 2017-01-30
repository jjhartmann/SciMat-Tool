/*
 * GlobalReplaceDocumentsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateDocumentEdit;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;


/**
 *
 * @author mjcobo
 */
public class GlobalReplaceDocumentsEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInTitle;
  private boolean findInDocAbstract;
  private boolean findInType;
  private boolean findInDoi;
  private boolean findInSourceIdentifier;
  private boolean findInVolume;
  private boolean findInIssue;
  private boolean findInBeginPage;
  private boolean findInEndPage;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInTitle
   * @param findInDocAbstract
   * @param findInType
   * @param findInDoi
   * @param findInSourceIdentifier
   * @param findInVolume
   * @param findInIssue
   * @param findInBeginPage
   * @param findInEndPage 
   */
  public GlobalReplaceDocumentsEdit(String findText, String replaceText, boolean findInTitle, boolean findInDocAbstract, boolean findInType, boolean findInDoi, boolean findInSourceIdentifier, boolean findInVolume, boolean findInIssue, boolean findInBeginPage, boolean findInEndPage) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInTitle = findInTitle;
    this.findInDocAbstract = findInDocAbstract;
    this.findInType = findInType;
    this.findInDoi = findInDoi;
    this.findInSourceIdentifier = findInSourceIdentifier;
    this.findInVolume = findInVolume;
    this.findInIssue = findInIssue;
    this.findInBeginPage = findInBeginPage;
    this.findInEndPage = findInEndPage;
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
    boolean successful, updated;
    ArrayList<Document> documents;
    Document document;
    String title, docAbstract, type, doi, sourceIdentifier, volume, issue, beginPage, endPage;
    DocumentDAO documentDAO;
    
    successful = false;
    
    documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
    
    // For each entity:
    // 1. Update the remainings fields and call to the corresponding update Edit.
    
    try {

      documents = documentDAO.getDocuments();
      
      for (i = 0; i < documents.size(); i++) {
        updated = false;
      
        document = documents.get(i);
        
        if (findInTitle) {
        
          title = document.getTitle().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! title.equals(document.getTitle()));
        
        } else {
        
          title = document.getTitle();
          
          updated = updated || false;
        }
        
        if (findInDocAbstract) {
        
          docAbstract = document.getDocAbstract().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! docAbstract.equals(document.getDocAbstract()));
        
        } else {
        
          docAbstract = document.getDocAbstract();
          
          updated = updated || false;
        }
        
        if (findInType) {
        
          type = document.getType().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! type.equals(document.getType()));
        
        } else {
        
          type = document.getType();
          
          updated = updated || false;
        }
        
        if (findInDoi) {
        
          doi = document.getDoi().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! doi.equals(document.getDoi()));
        
        } else {
        
          doi = document.getDoi();
          
          updated = updated || false;
        }
        
        if (findInSourceIdentifier) {
        
          sourceIdentifier = document.getSourceIdentifier().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! sourceIdentifier.equals(document.getSourceIdentifier()));
        
        } else {
        
          sourceIdentifier = document.getSourceIdentifier();
          
          updated = updated || false;
        }
        
        if (findInVolume) {
        
          volume = document.getVolume().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! volume.equals(document.getVolume()));
        
        } else {
        
          volume = document.getVolume();
          
          updated = updated || false;
        }
        
        if (findInIssue) {
        
          issue = document.getIssue().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! issue.equals(document.getIssue()));
        
        } else {
        
          issue = document.getIssue();
          
          updated = updated || false;
        }
        
        if (findInBeginPage) {
        
          beginPage = document.getBeginPage().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! beginPage.equals(document.getBeginPage()));
        
        } else {
        
          beginPage = document.getBeginPage();
          
          updated = updated || false;
        }
        
        if (findInEndPage) {
        
          endPage = document.getEndPage().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! endPage.equals(document.getEndPage()));
        
        } else {
        
          endPage = document.getEndPage();
          
          updated = updated || false;
        }
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (updated) {
        
          successful = new UpdateDocumentEdit(document.getDocumentID(), 
                  title, 
                  docAbstract, 
                  type, 
                  document.getCitationsCount(), 
                  doi, 
                  sourceIdentifier, 
                  volume, 
                  issue, 
                  beginPage, 
                  endPage).execute();
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
