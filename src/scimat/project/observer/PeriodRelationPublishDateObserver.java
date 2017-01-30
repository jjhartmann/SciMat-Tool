/*
 * PeriodRelationPublishDateObserver.java
 *
 * Created on 20-mar-2011, 21:11:25
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface PeriodRelationPublishDateObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
