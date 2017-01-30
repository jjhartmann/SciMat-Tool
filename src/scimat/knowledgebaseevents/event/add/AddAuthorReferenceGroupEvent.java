/*
 * AddAuthorReferenceGroupEvent.java
 *
 * Created on 28-dic-2011, 20:10:51
 */
package scimat.knowledgebaseevents.event.add;

import java.util.ArrayList;
import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddAuthorReferenceGroupEvent implements KnowledgeBaseEvent {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private ArrayList<AuthorReferenceGroup> authorReferenceGroups;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param authorReferenceGroups 
   */
  public AddAuthorReferenceGroupEvent(ArrayList<AuthorReferenceGroup> authorReferenceGroups) {
    this.authorReferenceGroups = authorReferenceGroups;
  }
  
  /**
   * 
   * @param authorReferenceGroup 
   */
  public AddAuthorReferenceGroupEvent(AuthorReferenceGroup authorReferenceGroup) {
    this.authorReferenceGroups = new ArrayList<AuthorReferenceGroup>();
    this.authorReferenceGroups.add(authorReferenceGroup);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvent() throws KnowledgeBaseException {
    
    CurrentProject.getInstance().getKbObserver().fireAuthorReferenceGroupAdded(authorReferenceGroups);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
