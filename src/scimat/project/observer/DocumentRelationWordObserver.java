/*
 * DocumentRelationWordObserver.java
 *
 * Created on 20-mar-2011, 21:12:24
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface DocumentRelationWordObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
