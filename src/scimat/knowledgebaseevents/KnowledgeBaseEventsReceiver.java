/*
 * KnowledgeBaseEventsReceiver.java
 *
 * Created on 28-dic-2011, 19:56:27
 */
package scimat.knowledgebaseevents;

import java.util.ArrayList;
import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class KnowledgeBaseEventsReceiver {

  

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<KnowledgeBaseEvent> knowledgeBaseEvents;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  private KnowledgeBaseEventsReceiver() {
    
    this.knowledgeBaseEvents = new ArrayList<KnowledgeBaseEvent>();
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @return
   */
  public static KnowledgeBaseEventsReceiver getInstance() {
    return KnowledgeBaseEventsReceiverSingleton.INSTANCE;
  }

  /**
   * 
   */
  private static class KnowledgeBaseEventsReceiverSingleton {
    private static final KnowledgeBaseEventsReceiver INSTANCE = new KnowledgeBaseEventsReceiver();
  }
  
  /**
   * 
   * @param event 
   */
  public void addEvent(KnowledgeBaseEvent event) {
      
    this.knowledgeBaseEvents.add(event);
  }
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvents() throws KnowledgeBaseException {
  
    int i;
    
    for (i = 0; i < this.knowledgeBaseEvents.size(); i++) {
      
      this.knowledgeBaseEvents.get(i).fireEvent();
    }
    
    this.knowledgeBaseEvents.clear();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
