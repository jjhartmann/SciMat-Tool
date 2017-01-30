/*
 * LoadProjectTask.java
 *
 * Created on 28-mar-2011
 */

package scimat.gui.commands.task;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.gui.components.wizard.WizardManager;
import scimat.model.knowledgebase.exception.IncorrectFormatKnowledgeBaseException;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class LoadProjectTask implements NoUndoableTask {

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
  public LoadProjectTask(JComponent component) {
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
    int val;

    JFileChooser fileChooser = new JFileChooser();

    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);

    int returnVal = fileChooser.showOpenDialog(this.receiver);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      path = fileChooser.getSelectedFile().getAbsolutePath();
      
      try {

        CursorManager.getInstance().setWaitCursor();
        CurrentProject.getInstance().loadProyect(path);
        CursorManager.getInstance().setNormalCursor();

      } catch (KnowledgeBaseException e) {

        CursorManager.getInstance().setNormalCursor();

        if (e instanceof IncorrectFormatKnowledgeBaseException) {

          val = JOptionPane.showConfirmDialog(receiver, "The file contains an old "
                  + "knowledge base format.\nDo you wan to upgrade the "
                  + "knowledge base to the current verion?", "Upgrade", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

          if (val == JOptionPane.YES_OPTION) {
          
            WizardManager.getInstance().showUpdgradeKnowledgeBaseDialog(path);
          }
          
        } else {

          ErrorDialogManager.getInstance().showError("The file's format is "
                  + "incorrect.\nAn error happened.\n"
                  + "Please choose other file.");
        }
      }
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
