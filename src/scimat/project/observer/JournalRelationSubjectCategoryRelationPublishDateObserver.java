/*
 * JournalRelationSubjectCategoryRelationPublishDateObserver.java
 *
 * Created on 20-mar-2011, 21:13:20
 */
package scimat.project.observer;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface JournalRelationSubjectCategoryRelationPublishDateObserver {

  public void relationChanged() throws KnowledgeBaseException;
}
