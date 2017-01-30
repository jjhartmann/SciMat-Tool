/*
 * NewProjectTask.java
 *
 * Created on 28-mar-2011, 19:15:03
 */
package scimat.gui.commands.task;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class NewProjectTask implements NoUndoableTask {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private String folderPath;
  private String filePath;
  private JComponent component;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param component
   */
  public NewProjectTask(String folderPath, String filePath, JComponent component) {

    this.folderPath = folderPath;
    this.filePath = filePath;
    this.component = component;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void execute() {

    File file;
    int returnVal;
    boolean flag = true;

    file = new File(this.filePath);

    if (file.exists()) {

      returnVal = JOptionPane.showConfirmDialog(this.component, "The file already exist. "
              + "Would you like to override it?", "Existing file",
              JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

      if (returnVal == JOptionPane.OK_OPTION) {

        flag = true;

      } else {

        flag = false;
      }
    }

    if (flag) {

      try {

        CursorManager.getInstance().setWaitCursor();
        CurrentProject.getInstance().newProyect(this.folderPath, this.filePath);
        CursorManager.getInstance().setNormalCursor();

      } catch (KnowledgeBaseException e) {
    
        ErrorDialogManager.getInstance().showException(e);
      }
    }
    
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
