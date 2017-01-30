/*
 * DocumentRelationAffiliationObserver.java
 *
 * Created on 20-mar-2011, 21:11:52
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface DocumentRelationAffiliationObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
