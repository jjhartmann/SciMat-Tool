/*
 * KnowledgeBaseEvent.java
 *
 * Created on 28-dic-2011, 19:58:38
 */
package scimat.knowledgebaseevents.event;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface KnowledgeBaseEvent {
  
  public void fireEvent() throws KnowledgeBaseException; 
}
