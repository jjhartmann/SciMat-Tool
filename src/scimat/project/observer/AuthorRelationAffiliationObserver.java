/*
 * AuthorRelationAffiliationObserver.java
 *
 * Created on 20-mar-2011, 21:12:09
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface AuthorRelationAffiliationObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
