/*
 * UpdateReferenceSourceWithoutGroupEvent.java
 *
 * Created on 28-dic-2011, 20:10:51
 */
package scimat.knowledgebaseevents.event.update;

import java.util.ArrayList;
import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateReferenceSourceWithoutGroupEvent implements KnowledgeBaseEvent {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private ArrayList<ReferenceSource> referenceSources;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param referenceSources 
   */
  public UpdateReferenceSourceWithoutGroupEvent(ArrayList<ReferenceSource> referenceSources) {
    this.referenceSources = referenceSources;
  }
  
  /**
   * 
   * @param referenceSource 
   */
  public UpdateReferenceSourceWithoutGroupEvent(ReferenceSource referenceSource) {
    this.referenceSources = new ArrayList<ReferenceSource>();
    this.referenceSources.add(referenceSource);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvent() throws KnowledgeBaseException {
    
    CurrentProject.getInstance().getKbObserver().fireReferenceSourceWithoutGroupUpdated(referenceSources);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
