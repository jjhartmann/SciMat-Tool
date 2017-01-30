/*
 * ImportFileTask.java
 *
 * Created on 07-jun-2011, 13:35:49
 */
package scimat.gui.commands.task;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import scimat.api.loader.CsvLoader;
import scimat.api.loader.GenericLoader;
import scimat.api.loader.ISIWoSLoader;
import scimat.api.loader.RISLoader;
import scimat.api.loader.RISLoaderMay2014;
import scimat.gui.commands.NoUndoableTask;
import scimat.gui.components.ChooseCharDelimiterDialog;
import scimat.gui.components.cursor.CursorManager;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class ImportFileTask implements NoUndoableTask {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  public enum FormatAvailable {ISIWoS, RIS, RISMay2014, CSV};

  /**
   * 
   */
  private final FormatAvailable format;
  
  
  private final JFrame receiver;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param format
   * @param receiver 
   */
  public ImportFileTask(FormatAvailable format, JFrame receiver) {
    
    this.format = format;
    this.receiver = receiver;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   */
  @Override
  public void execute() {

    int i;
    String path;
    File[] files;
    GenericLoader loader = null;
    boolean importReferences;

    JFileChooser fileChooser = new JFileChooser();

    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(true);
    fileChooser.setCurrentDirectory(new File(CurrentProject.getInstance().getCurrentProjectPath()));

    int returnVal = fileChooser.showOpenDialog(this.receiver);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      files = fileChooser.getSelectedFiles();

      returnVal = JOptionPane.showConfirmDialog(this.receiver,
              "The references may delay the import process. Do you want to import them?",
              "Import references?",
              JOptionPane.YES_NO_CANCEL_OPTION,
              JOptionPane.QUESTION_MESSAGE);

      if (returnVal != JOptionPane.CANCEL_OPTION) {

        importReferences = returnVal == JOptionPane.YES_OPTION;

        try {

          CursorManager.getInstance().setWaitCursor();

          for (i = 0; i < files.length; i++) {

            path = files[i].getAbsolutePath();

            System.out.println("Loading file " + path);

            switch (this.format) {

              case ISIWoS:
                loader = new ISIWoSLoader(path, importReferences);
                break;

              case RIS:
                loader = new RISLoader(path, importReferences);
                break;
                
              case RISMay2014:
                loader = new RISLoaderMay2014(path, importReferences);
                break;

              case CSV:

                ChooseCharDelimiterDialog dialog = new ChooseCharDelimiterDialog(this.receiver, true);

                dialog.setVisible(true);

                loader = new CsvLoader(path, dialog.getCharDelimiter(), importReferences);
                break;
            }

            loader.execute(CurrentProject.getInstance().getKnowledgeBase());

          }

          CursorManager.getInstance().setNormalCursor();

        } catch (Exception e) {

          CursorManager.getInstance().setNormalCursor();
          e.printStackTrace(System.err);

          JOptionPane.showMessageDialog(this.receiver, "The file's format is "
                  + "incorrect.\nAn error happened.\n"
                  + "Please choose other file.",
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
