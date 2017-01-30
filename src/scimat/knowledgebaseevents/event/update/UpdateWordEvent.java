/*
 * UpdateWordEvent.java
 *
 * Created on 28-dic-2011, 20:10:51
 */
package scimat.knowledgebaseevents.event.update;

import java.util.ArrayList;
import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateWordEvent implements KnowledgeBaseEvent {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private ArrayList<Word> words;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param words 
   */
  public UpdateWordEvent(ArrayList<Word> words) {
    this.words = words;
  }
  
  /**
   * 
   * @param wordGroup 
   */
  public UpdateWordEvent(Word word) {
    this.words = new ArrayList<Word>();
    this.words.add(word);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvent() throws KnowledgeBaseException {
    
    CurrentProject.getInstance().getKbObserver().fireWordUpdated(words);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
