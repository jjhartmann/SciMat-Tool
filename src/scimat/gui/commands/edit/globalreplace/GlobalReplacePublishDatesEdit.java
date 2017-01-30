/*
 * GlobalReplacePublishDatesEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinPublishDateEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdatePublishDateEdit;
import scimat.model.knowledgebase.dao.PublishDateDAO;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplacePublishDatesEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInYear;
  private boolean findInDate;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInFullAffiliation 
   */
  public GlobalReplacePublishDatesEdit(String findText, String replaceText, boolean findInYear, boolean findInDate) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInYear = findInYear;
    this.findInDate = findInDate;
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
    ArrayList<PublishDate> publishDates;
    PublishDate publishDate;
    String year, date;
    PublishDateDAO publishDateDAO;
    
    successful = false;
    
    publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      publishDates = publishDateDAO.getPublishDates();
      
      for (i = 0; i < publishDates.size(); i++) {
    
        joined = false;
        updated = false;
      
        publishDate = publishDates.get(i);
        
        if (findInYear) {
        
          year = publishDate.getYear().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! year.equals(publishDate.getYear()));
        
        } else {
        
          year = publishDate.getYear();
          
          updated = updated || false;
        }
        
        if (findInDate) {
        
          date = publishDate.getDate().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! date.equals(publishDate.getDate()));
          
        } else {
        
          date = publishDate.getDate();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (publishDateDAO.checkPublishDate(year, date)) {
          
            ArrayList<PublishDate> tmpArray = new ArrayList<PublishDate>();
            tmpArray.add(publishDate);
            
            successful = new JoinPublishDateEdit(tmpArray, publishDateDAO.getPublishDate(year, date)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdatePublishDateEdit(publishDate.getPublishDateID(), year, date).execute();
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
