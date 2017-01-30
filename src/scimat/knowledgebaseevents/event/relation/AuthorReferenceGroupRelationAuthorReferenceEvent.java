/*
 * AuthorReferenceGroupRelationAuthorReferenceEvent.java
 *
 * Created on 20-mar-2011, 21:09:38
 */
package scimat.knowledgebaseevents.event.relation;

import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupRelationAuthorReferenceEvent implements KnowledgeBaseEvent {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvent() throws KnowledgeBaseException {
    
    CurrentProject.getInstance().getKbObserver().fireAuthorReferenceGroupRelationAuthorReferencesChanged();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
}
