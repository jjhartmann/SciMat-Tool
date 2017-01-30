/*
 * CursorManager.java
 *
 * Created on 02-dic-2008
 */

package scimat.gui.components.cursor;

import java.awt.Cursor;
import java.awt.Window;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class CursorManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private Window window;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  private CursorManager() {
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @return
   */
  public static CursorManager getInstance() {
    return CursorManagerSingleton.INSTANCE;
  }

  /**
   *
   */
  private static class CursorManagerSingleton {
    private static final CursorManager INSTANCE = new CursorManager();
  }

  /**
   * 
   * @param window
   */
  public void init(Window window) {

    this.window = window;
    //this.window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    this.window.setCursor(null);
  }

  public Window getWindow() {
    return window;
  }

  /**
   * 
   */
  public void setWaitCursor() {

    this.window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
  }

  /**
   *
   */
  public void setNormalCursor() {

    this.window.setCursor(null);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
