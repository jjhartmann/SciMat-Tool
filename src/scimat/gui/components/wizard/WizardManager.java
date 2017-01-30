/*
 * PerformKnowledgeBaseEditTask.java
 *
 * Created on 28-mar-2011, 20:11:26
 */
package scimat.gui.components.wizard;

import javax.swing.JFrame;
import scimat.gui.components.wizard.Analysis.MakeAnalysisDialog;

/**
 *
 * @author mjcobo
 */
public class WizardManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private NewProjectDialog newProjectDialog;
  private MakeAnalysisDialog makeAnalysisDialog;
  private UpgradeKnowledgeBaseDialog upgradeKnowledgeBaseDialog;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param frame
   */
  private WizardManager() {
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @return
   */
  public static WizardManager getInstance() {
    return WizardManagerHolder.INSTANCE;
  }

  /**
   *
   */
  private static class WizardManagerHolder {

    private static final WizardManager INSTANCE = new WizardManager();
  }

  /**
   * 
   * @param frame
   */
  public void init(JFrame frame) {

    this.newProjectDialog = new NewProjectDialog(frame, true);
    this.makeAnalysisDialog = new MakeAnalysisDialog(frame, true);
    this.upgradeKnowledgeBaseDialog = new UpgradeKnowledgeBaseDialog(frame, true);
  }

  /**
   * 
   */
  public void showNewProject() {

    this.newProjectDialog.refresh();
    this.newProjectDialog.setVisible(true);
  }

  public void showMakeAnalysis() {

    this.makeAnalysisDialog.refresh();
    this.makeAnalysisDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showUpdgradeKnowledgeBaseDialog(String oldKnowledgeBasePath) {
  
    this.upgradeKnowledgeBaseDialog.refresh(oldKnowledgeBasePath);
    this.upgradeKnowledgeBaseDialog.setVisible(true);
  }
}
