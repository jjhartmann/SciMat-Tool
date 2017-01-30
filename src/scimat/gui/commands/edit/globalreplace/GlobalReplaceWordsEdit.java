/*
 * GlobalReplaceWordsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinWordEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateWordEdit;
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceWordsEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInWordName;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInWordName 
   */
  public GlobalReplaceWordsEdit(String findText, String replaceText, boolean findInWordName) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInWordName = findInWordName;
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
    ArrayList<Word> words;
    Word word;
    String wordName;
    WordDAO wordDAO;
    
    successful = false;
    
    wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      words = wordDAO.getWords();
      
      for (i = 0; i < words.size(); i++) {
    
        joined = false;
        updated = false;
      
        word = words.get(i);
        
        if (findInWordName) {
        
          wordName = word.getWordName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! wordName.equals(word.getWordName()));
          
        } else {
        
          wordName = word.getWordName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (wordDAO.checkWord(wordName)) {
          
            ArrayList<Word> tmpArray = new ArrayList<Word>();
            tmpArray.add(word);
            
            successful = new JoinWordEdit(tmpArray, wordDAO.getWord(wordName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateWordEdit(word.getWordID(), wordName).execute();
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
