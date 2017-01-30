/*
 * UpgradeKnowledgeBaseTask.java
 *
 * Created on 26-ene-2012, 19:59:24
 */
package scimat.gui.commands.task;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.cursor.CursorManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.model.upgrade.UpgradeKnowledgeBaseVersion;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpgradeKnowledgeBaseTask implements NoUndoableTask {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String oldKnowledgeBasePath;
  private String newKnowledgeBaseFolderPath;
  private String newKnowledgeBaseFile;
  
  /**
   * 
   */
  private JComponent receiver;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param oldKnowledgeBasePath
   * @param newKnowledgeBaseFolderPath
   * @param newKnowledgeBaseFile
   * @param receiver 
   */
  public UpgradeKnowledgeBaseTask(String oldKnowledgeBasePath, 
          String newKnowledgeBaseFolderPath, String newKnowledgeBaseFile,
          JComponent receiver) {
    
    this.oldKnowledgeBasePath = oldKnowledgeBasePath;
    this.newKnowledgeBaseFolderPath = newKnowledgeBaseFolderPath;
    this.newKnowledgeBaseFile = newKnowledgeBaseFile;
    this.receiver = receiver;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   */
  public void execute() {
    
    File file;
    String fullPath;
    int returnVal;
    boolean flag = true;

    fullPath = this.newKnowledgeBaseFolderPath + File.separator + this.newKnowledgeBaseFile;
    
    file = new File(fullPath);

    if (file.exists()) {

      returnVal = JOptionPane.showConfirmDialog(this.receiver, "The file already exist. "
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
        
        (new UpgradeKnowledgeBaseVersion(fullPath, this.oldKnowledgeBasePath)).execute();
        CurrentProject.getInstance().loadProyect(fullPath);
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
