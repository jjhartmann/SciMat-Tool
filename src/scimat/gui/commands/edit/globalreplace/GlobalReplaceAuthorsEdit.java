/*
 * GlobalReplaceAuthorsEdit.java
 *
 * Created on 22-ene-2012, 18:37:50
 */
package scimat.gui.commands.edit.globalreplace;

import java.util.ArrayList;
import scimat.gui.commands.edit.join.JoinAuthorEdit;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.commands.edit.update.UpdateAuthorEdit;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceAuthorsEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String findText;
  private String replaceText;
  private boolean findInAuthorName;
  private boolean findInFullAuthorName;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param findText
   * @param replaceText
   * @param findInFullAffiliation 
   */
  public GlobalReplaceAuthorsEdit(String findText, String replaceText, boolean findInAuthorName, boolean findInFullAuthorName) {
    this.findText = findText;
    this.replaceText = replaceText;
    this.findInAuthorName = findInAuthorName;
    this.findInFullAuthorName = findInFullAuthorName;
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
    ArrayList<Author> authors;
    Author author;
    String authorName, fullAuthorName;
    AuthorDAO authorDAO;
    
    successful = false;
    
    authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
    
    // For each entity:
    // 1. Update the unique fields.
    // 2. Check if there is an entity with the same unique fields.
    // 2.1 Yes: Join and finish
    // 2.2 No: Update the remainings fields and call to the corresponding update Edit.
    
    try {

      authors = authorDAO.getAuthors();
      
      for (i = 0; i < authors.size(); i++) {
        
        joined = false;
        updated = false;
      
        author = authors.get(i);
        
        if (findInAuthorName) {
        
          authorName = author.getAuthorName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! authorName.equals(author.getAuthorName()));
        
        } else {
        
          authorName = author.getAuthorName();
          
          updated = updated || false;
        }
        
        if (findInFullAuthorName) {
        
          fullAuthorName = author.getFullAuthorName().replaceAll(findText, replaceText);
          
          // If the field has been not modified we set false the flag. Otherwise, true.
          updated = updated || (! fullAuthorName.equals(author.getFullAuthorName()));
          
        } else {
        
          fullAuthorName = author.getFullAuthorName();
          
          updated = updated || false;
        }
        
        // If the unique fields have been modified, the entity could be 
        // joined with an entity with the same unique fields.
        if (updated) {
        
          if (authorDAO.checkAuthor(authorName, fullAuthorName)) {
          
            ArrayList<Author> tmpArray = new ArrayList<Author>();
            tmpArray.add(author);
            
            successful = new JoinAuthorEdit(tmpArray, authorDAO.getAuthor(authorName, fullAuthorName)).execute();
            joined = true;
            
          }
        } 
        
        // If the entity has been not joined and the fields have been modified, we perform the update.
        if (! joined && updated) {
        
          successful = new UpdateAuthorEdit(author.getAuthorID(), authorName, fullAuthorName).execute();
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
