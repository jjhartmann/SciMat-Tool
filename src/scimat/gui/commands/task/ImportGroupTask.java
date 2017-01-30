/*
 * ImportGroupTask.java
 *
 * Created on 28-mar-2011
 */

package scimat.gui.commands.task;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import scimat.api.imports.ImportGroupsXML;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class ImportGroupTask implements NoUndoableTask{

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
  public ImportGroupTask(JComponent component) {
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

    JOptionPane.showMessageDialog(receiver, "The groups will be modified and this action can not be undone. "
            + "\nDo you want to continue?", "Continue?", JOptionPane.QUESTION_MESSAGE);
    
    JFileChooser fileChooser = new JFileChooser();

    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setCurrentDirectory(new File(CurrentProject.getInstance().getCurrentProjectPath()));

    int returnVal = fileChooser.showOpenDialog(this.receiver);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      path = fileChooser.getSelectedFile().getAbsolutePath();
      
      try {

        CursorManager.getInstance().setWaitCursor();
        (new ImportGroupsXML(CurrentProject.getInstance().getKnowledgeBase(), path)).execute();
        CurrentProject.getInstance().getKbObserver().fireKnowledgeBaseRefresh();
        CursorManager.getInstance().setNormalCursor();

      } catch (Exception e) {

        CursorManager.getInstance().setNormalCursor();
        e.printStackTrace(System.err);
    
        ErrorDialogManager.getInstance().showException(e);
      }
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
