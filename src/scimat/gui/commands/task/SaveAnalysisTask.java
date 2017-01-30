/*
 * SaveExperimentTask.java
 *
 * Created on 06-apr-2011
 */

package scimat.gui.commands.task;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import scimat.analysis.CurrentAnalysis;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.cursor.CursorManager;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class SaveAnalysisTask implements NoUndoableTask{

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
  public SaveAnalysisTask(JComponent component) {
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
    fileChooser.setCurrentDirectory(new File(CurrentProject.getInstance().getCurrentProjectPath()));
    fileChooser.setMultiSelectionEnabled(false);

    int returnVal = fileChooser.showSaveDialog(this.receiver);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      path = fileChooser.getSelectedFile().getAbsolutePath();
      
      CursorManager.getInstance().setWaitCursor();
      
      try {
        
        CurrentAnalysis.getInstance().saveResults(path);

      } catch (Exception e) {

        
        e.printStackTrace(System.err);

        JOptionPane.showMessageDialog(receiver, "The file's format is "
                + "incorrect.\nAn error happened.\n"
                + "Please choose other file.",
                "Error", JOptionPane.ERROR_MESSAGE);
      }
      
      CursorManager.getInstance().setNormalCursor();
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
