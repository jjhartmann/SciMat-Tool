/*
 * TargetItemSelectedObserver.java
 *
 * Created on 24-may-2011, 13:56:11
 */
package scimat.gui.components.observer;

/**
 *
 * @author mjcobo
 */
public interface TargetItemSelectedObserver {
 
  /**
   * The observer must implements this method.
   * 
   * @param selected true if there is a target item != null selected.
   */
  public void targetItemSelectionChanged(boolean selected);
}
