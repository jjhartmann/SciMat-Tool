/*
 * PeriodRelationPublishDateEvent.java
 *
 * Created on 20-mar-2011, 21:11:25
 */
package scimat.knowledgebaseevents.event.relation;

import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class PeriodRelationPublishDateEvent implements KnowledgeBaseEvent {

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
    
    CurrentProject.getInstance().getKbObserver().firePeriodsRelationPublishDatesChanged();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
}
