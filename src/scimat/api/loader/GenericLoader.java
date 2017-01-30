/*
 * GenericLoader.java
 *
 * Created on 11-ene-2011, 13:43:07
 */

package scimat.api.loader;

import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface GenericLoader {

  public void execute(KnowledgeBaseManager kbm) throws LoaderException, KnowledgeBaseException;
}
