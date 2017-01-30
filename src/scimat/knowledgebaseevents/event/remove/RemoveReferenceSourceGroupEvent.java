/*
 * RemoveReferenceSourceGrouptEvent.java
 *
 * Created on 28-dic-2011, 20:10:51
 */
package scimat.knowledgebaseevents.event.remove;

import java.util.ArrayList;
import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class RemoveReferenceSourceGroupEvent implements KnowledgeBaseEvent {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private ArrayList<ReferenceSourceGroup> referenceSourceGroups;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param referenceSourceGroups 
   */
  public RemoveReferenceSourceGroupEvent(ArrayList<ReferenceSourceGroup> referenceSourceGroups) {
    this.referenceSourceGroups = referenceSourceGroups;
  }
  
  /**
   * 
   * @param referenceSourceGroup
   */
  public RemoveReferenceSourceGroupEvent(ReferenceSourceGroup referenceSourceGroup) {
    this.referenceSourceGroups = new ArrayList<ReferenceSourceGroup>();
    this.referenceSourceGroups.add(referenceSourceGroup);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvent() throws KnowledgeBaseException {
    
    CurrentProject.getInstance().getKbObserver().fireReferenceSourceGroupRemoved(referenceSourceGroups);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
