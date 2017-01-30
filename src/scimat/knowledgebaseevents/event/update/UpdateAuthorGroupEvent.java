/*
 * UpdateAuthorGroupEvent.java
 *
 * Created on 28-dic-2011, 20:10:51
 */
package scimat.knowledgebaseevents.event.update;

import java.util.ArrayList;
import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateAuthorGroupEvent implements KnowledgeBaseEvent {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private ArrayList<AuthorGroup> authorGroups;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param authorGroups 
   */
  public UpdateAuthorGroupEvent(ArrayList<AuthorGroup> authorGroups) {
    this.authorGroups = authorGroups;
  }
  
  /**
   * 
   * @param authorGroup
   */
  public UpdateAuthorGroupEvent(AuthorGroup authorGroup) {
    this.authorGroups = new ArrayList<AuthorGroup>();
    this.authorGroups.add(authorGroup);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvent() throws KnowledgeBaseException {
    
    CurrentProject.getInstance().getKbObserver().fireAuthorGroupUpdated(authorGroups);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
