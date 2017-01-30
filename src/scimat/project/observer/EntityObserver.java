/*
 * EntityObserver.java
 *
 * Created on 13-mar-2011, 17:56:13
 */
package scimat.project.observer;

import java.util.ArrayList;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface EntityObserver<T extends Comparable<T>> {

  public void entityUpdated(ArrayList<T> items) throws KnowledgeBaseException;
  public void entityAdded(ArrayList<T> items) throws KnowledgeBaseException;
  public void entityRemoved(ArrayList<T> items) throws KnowledgeBaseException;
  public abstract void entityRefresh() throws KnowledgeBaseException;
}
