/*
 * AuthorGroupRelationAuthorObserver.java
 *
 * Created on 20-mar-2011, 21:09:15
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface AuthorRelationAuthorReferenceObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
