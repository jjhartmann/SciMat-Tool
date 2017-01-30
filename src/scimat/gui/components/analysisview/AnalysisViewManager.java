/*
 * AddDialogManager.java
 *
 * Created on 18-mar-2011, 13:56:42
 */
package scimat.gui.components.analysisview;

import java.awt.Window;
import javax.swing.JFrame;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;

/**
 *
 * @author mjcobo
 */
public class AnalysisViewManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private AnalysisViewDialog analysisViewDialog;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  private AnalysisViewManager() {
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public static AnalysisViewManager getInstance() {
    return ExperimentViewManagerHolder.INSTANCE;
  }

  private static class ExperimentViewManagerHolder {

    private static final AnalysisViewManager INSTANCE = new AnalysisViewManager();
  }

  /**
   *
   * @param frame
   */
  public void init(JFrame frame) {

    this.analysisViewDialog = new AnalysisViewDialog(frame, true);
  }

  /**
   * 
   */
  public void showAnalysisViewDialog() {

    Window oldWindow = CursorManager.getInstance().getWindow();
    
    CursorManager.getInstance().init(this.analysisViewDialog);
    ErrorDialogManager.getInstance().init(this.analysisViewDialog);
    
    this.analysisViewDialog.refresh();
    this.analysisViewDialog.setVisible(true);
    
    CursorManager.getInstance().init(oldWindow);
    ErrorDialogManager.getInstance().init(oldWindow);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
