/*
 * GlobalReplaceJournalsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinJournalEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateJournalEdit;
import scimat.model.knowledgebase.dao.JournalDAO;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceJournalsEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInSource;
  private boolean findInConferenceInformation;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInFullAffiliation 
   */
  public GlobalReplaceJournalsEdit(String findText, String replaceText, boolean findInSource, boolean findInConferenceInformation) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInSource = findInSource;
    this.findInConferenceInformation = findInConferenceInformation;
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
    ArrayList<Journal> journals;
    Journal journal;
    String source, conferenceInformation;
    JournalDAO journalDAO;
    
    successful = false;
    
    journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      journals = journalDAO.getJournals();
      
      for (i = 0; i < journals.size(); i++) {
        
        joined = false;
        updated = false;
      
        journal = journals.get(i);
        
        if (findInSource) {
        
          source = journal.getSource().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! source.equals(journal.getSource()));
        
        } else {
        
          source = journal.getSource();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (journalDAO.checkJournal(source)) {
          
            ArrayList<Journal> tmpArray = new ArrayList<Journal>();
            tmpArray.add(journal);
            
            successful = new JoinJournalEdit(tmpArray, journalDAO.getJournal(source)).execute();
            joined = true;
            
          }
        }
        
        if (! joined && findInConferenceInformation) {
        
          conferenceInformation = journal.getConferenceInformation().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! conferenceInformation.equals(journal.getConferenceInformation()));
          
        } else {
        
          conferenceInformation = journal.getConferenceInformation();
          
          updated = updated || false;
        }
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateJournalEdit(journal.getJournalID(), source, conferenceInformation).execute();
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
