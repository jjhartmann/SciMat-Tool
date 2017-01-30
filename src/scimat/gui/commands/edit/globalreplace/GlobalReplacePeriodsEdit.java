/*
 * GlobalReplacePeriodsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinPeriodEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdatePeriodEdit;
import scimat.model.knowledgebase.dao.PeriodDAO;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplacePeriodsEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInName;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInName 
   */
  public GlobalReplacePeriodsEdit(String findText, String replaceText, boolean findInName) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInName = findInName;
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
    ArrayList<Period> periods;
    Period period;
    String name;
    PeriodDAO periodDAO;
    
    successful = false;
    
    periodDAO = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      periods = periodDAO.getPeriods();
      
      for (i = 0; i < periods.size(); i++) {
        
        joined = false;
        updated = false;
      
        period = periods.get(i);
        
        if (findInName) {
        
          name = period.getName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! name.equals(period.getName()));
          
        } else {
        
          name = period.getName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (periodDAO.checkPeriod(name)) {
          
            ArrayList<Period> tmpArray = new ArrayList<Period>();
            tmpArray.add(period);
            
            successful = new JoinPeriodEdit(tmpArray, periodDAO.getPeriod(name)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdatePeriodEdit(period.getPeriodID(), name, period.getPosition()).execute();
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
