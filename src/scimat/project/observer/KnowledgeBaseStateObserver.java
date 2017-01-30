/*
 * KnowledgeBaseStateObserver.java
 *
 * Created on 22-sep-2008
 */

package scimat.project.observer;

/**
 * Representa a un observador que observa el estado de la base de conocimiento
 * asociada al proyecto. El estado puede ser cargada o no cargada.
 * 
 * Los observadores que necesiten observar dicho estado, deberan implementar
 * esta interfaz
 * 
 * @author Manuel Jesus Cobo Martin.
 */
public interface KnowledgeBaseStateObserver {

  /**
   * Cuando ocurra un cambio en el estado de la base de conocimiento, se 
   * notificara a las clases que implementen esta interfaz a traves de este
   * metodo, por lo tanto bajo este metodo se tienen que realizar las acciones
   * oportunas al cambio de estado.
   * 
   * @param loaded true en caso de que la base de conocimiento este cargada.
   */
  public void knowledgeBaseStateChanged(boolean loaded);
}
