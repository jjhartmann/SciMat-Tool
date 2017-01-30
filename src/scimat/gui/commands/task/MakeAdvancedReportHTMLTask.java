/*
 * MakeAdvancedReportHTMLTask.java
 *
 * Created on 06-jun-2011, 18:51:31
 */
package scimat.gui.commands.task;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import scimat.analysis.GlobalAnalysisResult;
import scimat.api.report.MakeExtendedReportHTML;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class MakeAdvancedReportHTMLTask implements NoUndoableTask {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private JComponent component;
  
  private GlobalAnalysisResult globalExperimentResult;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public MakeAdvancedReportHTMLTask(JComponent component, GlobalAnalysisResult globalExperimentResult) {
    this.component = component;
    this.globalExperimentResult = globalExperimentResult;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   */
  public void execute() {
    
    String path;
    int returnVal;

    if (CurrentProject.getInstance().isKnowledbaseLoaded()) {



      JFileChooser fileChooser = new JFileChooser();

      fileChooser.setDialogTitle("Select a directory");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.setMultiSelectionEnabled(false);

      if (CurrentProject.getInstance().getCurrentProjectPath() != null) {

        fileChooser.setCurrentDirectory(new File(CurrentProject.getInstance().getCurrentProjectPath()));
      }

      returnVal = fileChooser.showSaveDialog(this.component);

      if (returnVal == JFileChooser.APPROVE_OPTION) {

        path = fileChooser.getSelectedFile().getAbsolutePath();

        try {

          CursorManager.getInstance().setWaitCursor();
          (new MakeExtendedReportHTML(path, this.globalExperimentResult, CurrentProject.getInstance().getKnowledgeBase())).execute();
          CursorManager.getInstance().setNormalCursor();

        } catch (Exception e) {

          ErrorDialogManager.getInstance().showException(e);
        }
      }
      
    } else {
    
      ErrorDialogManager.getInstance().showError("A knowledge base must be loaded.");
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
