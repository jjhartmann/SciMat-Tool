/*
 * StatusTaskObserver.java
 *
 * Created on 30-ene-2008
 */

package scimat.observabletask;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public interface StatusTaskObserver {

  /**
   * 
   * @param status
   */
  public void statusTaskChanged(TaskStatus status);
}
