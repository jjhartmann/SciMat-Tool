/*
 * GlobalReplaceDialogManager.java
 *
 * Created on 23-ene-2012, 11:38:12
 */
package scimat.gui.components.globalreplace;

import javax.swing.JFrame;

/**
 *
 * @author mjcobo
 */
public class GlobalReplaceDialogManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private GlobalReplaceDialog affiliationGlobalReplaceDialog;
  private GlobalReplaceDialog authorGlobalReplaceDialog;
  private GlobalReplaceDialog authorGroupGlobalReplaceDialog;
  private GlobalReplaceDialog authorReferenceGlobalReplaceDialog;
  private GlobalReplaceDialog authorReferenceGroupGlobalReplaceDialog;
  private GlobalReplaceDialog journalGlobalReplaceDialog;
  private GlobalReplaceDialog documentGlobalReplaceDialog;
  private GlobalReplaceDialog periodGlobalReplaceDialog;
  private GlobalReplaceDialog publishDateGlobalReplaceDialog;
  private GlobalReplaceDialog referenceGlobalReplaceDialog;
  private GlobalReplaceDialog referenceGroupGlobalReplaceDialog;
  private GlobalReplaceDialog referenceSourceGlobalReplaceDialog;
  private GlobalReplaceDialog referenceSourceGroupGlobalReplaceDialog;
  private GlobalReplaceDialog subjectCategoryGlobalReplaceDialog;
  private GlobalReplaceDialog wordGlobalReplaceDialog;
  private GlobalReplaceDialog wordGroupGlobalReplaceDialog;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  private GlobalReplaceDialogManager() {
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @return
   */
  public static GlobalReplaceDialogManager getInstance() {

    return GlobalReplaceDialogManagerHolder.INSTANCE;
  }

  /**
   *
   */
  private static class GlobalReplaceDialogManagerHolder {

    private static final GlobalReplaceDialogManager INSTANCE = new GlobalReplaceDialogManager();
  }
  
  /**
   *
   * @param frame
   */
  public void init(JFrame frame) {

    this.affiliationGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceAffiliationPanel());
    this.authorGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceAuthorPanel());
    this.authorGroupGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceAuthorGroupPanel());
    this.authorReferenceGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceAuthorReferencePanel());
    this.authorReferenceGroupGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceAuthorReferenceGroupPanel());
    this.journalGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceJournalPanel());
    this.documentGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceDocumentPanel());
    this.periodGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplacePeriodPanel());
    this.publishDateGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplacePublishDatePanel());
    this.referenceGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceReferencePanel());
    this.referenceGroupGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceReferenceGroupPanel());
    this.referenceSourceGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceReferenceSourcePanel());
    this.referenceSourceGroupGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceReferenceSourceGroupPanel());
    this.wordGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceWordPanel());
    this.wordGroupGlobalReplaceDialog = new GlobalReplaceDialog(frame, new GlobalReplaceWordGroupPanel());
  }
  
  /**
   * 
   */
  public void showGlobalAffiliationGlobalReplaceDialog() {
  
    this.affiliationGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalAuthorGlobalReplaceDialog() {
  
    this.authorGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalAuthorGroupGlobalReplaceDialog() {
  
    this.authorGroupGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalAuthorReferenceGlobalReplaceDialog() {
  
    this.authorReferenceGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalAuthorReferenceGroupGlobalReplaceDialog() {
  
    this.authorReferenceGroupGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalJournalGlobalReplaceDialog() {
  
    this.journalGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalDocumentGlobalReplaceDialog() {
  
    this.documentGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalPeriodGlobalReplaceDialog() {
  
    this.periodGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalPublishDateGlobalReplaceDialog() {
  
    this.publishDateGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalReferenceGlobalReplaceDialog() {
  
    this.referenceGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalReferenceGroupGlobalReplaceDialog() {
  
    this.referenceGroupGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalReferenceSourceGlobalReplaceDialog() {
  
    this.referenceSourceGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalReferenceSourceGroupGlobalReplaceDialog() {
  
    this.referenceSourceGroupGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalSubjectCategoryGlobalReplaceDialog() {
  
    this.subjectCategoryGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalWordGlobalReplaceDialog() {
  
    this.wordGlobalReplaceDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showGlobalWordGroupGlobalReplaceDialog() {
  
    this.wordGroupGlobalReplaceDialog.setVisible(true);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
