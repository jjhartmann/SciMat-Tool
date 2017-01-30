/*
 * JournalRelationDocumentObserver.java
 *
 * Created on 20-mar-2011, 21:10:29
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface JournalRelationDocumentObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
