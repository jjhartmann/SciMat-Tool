/*
 * ReferenceSourceGroupRelationReferenceSourceEvent.java
 *
 * Created on 20-mar-2011, 21:10:14
 */
package scimat.knowledgebaseevents.event.relation;

import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceGroupRelationReferenceSourceEvent implements KnowledgeBaseEvent {

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
    
    CurrentProject.getInstance().getKbObserver().fireReferenceSourceGroupRelationReferenceSourcesChanged();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
}
