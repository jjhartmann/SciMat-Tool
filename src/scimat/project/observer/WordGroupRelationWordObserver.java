/*
 * WordGroupRelationWordObserver.java
 *
 * Created on 20-mar-2011, 21:08:59
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface WordGroupRelationWordObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
