/*
 * ExportGroupTask.java
 *
 * Created on 28-mar-2011
 */

package scimat.gui.commands.task;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import scimat.api.export.ExportException;
import scimat.api.export.ExportGroupsXML;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class ExportGroupTask implements NoUndoableTask{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private JComponent receiver;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param component
   */
  public ExportGroupTask(JComponent component) {
    this.receiver = component;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Tarea a realizar por el objeto.
   */
  public void execute() {
  
    String path;

    JFileChooser fileChooser = new JFileChooser();

    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setCurrentDirectory(new File(CurrentProject.getInstance().getCurrentProjectPath()));

    int returnVal = fileChooser.showSaveDialog(this.receiver);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      path = fileChooser.getSelectedFile().getAbsolutePath();
      
      try {

        CursorManager.getInstance().setWaitCursor();
        (new ExportGroupsXML(path)).execute();
        CursorManager.getInstance().setNormalCursor();

      } catch (ExportException e) {

        CursorManager.getInstance().setNormalCursor();
        e.printStackTrace(System.err);
    
        ErrorDialogManager.getInstance().showError("An error happens.");
      }
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
