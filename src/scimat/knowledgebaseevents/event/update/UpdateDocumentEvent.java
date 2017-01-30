/*
 * UpdateDocumentEvent.java
 *
 * Created on 28-dic-2011, 20:10:51
 */
package scimat.knowledgebaseevents.event.update;

import java.util.ArrayList;
import scimat.knowledgebaseevents.event.KnowledgeBaseEvent;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateDocumentEvent implements KnowledgeBaseEvent {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private ArrayList<Document> documents;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param documents 
   */
  public UpdateDocumentEvent(ArrayList<Document> documents) {
    this.documents = documents;
  }
  
  /**
   * 
   * @param document 
   */
  public UpdateDocumentEvent(Document document) {
    this.documents = new ArrayList<Document>();
    this.documents.add(document);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  public void fireEvent() throws KnowledgeBaseException {
    
    CurrentProject.getInstance().getKbObserver().fireDocumentUpdated(documents);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
