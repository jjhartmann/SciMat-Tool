/*
 * ReferenceSourceRelationReferenceObserver.java
 *
 * Created on 20-mar-2011, 21:10:57
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface ReferenceSourceRelationReferenceObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
