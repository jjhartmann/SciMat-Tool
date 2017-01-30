/*
 * ReferenceRelationAuthorReferenceObserver.java
 *
 * Created on 20-mar-2011, 21:12:50
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface ReferenceRelationAuthorReferenceObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
