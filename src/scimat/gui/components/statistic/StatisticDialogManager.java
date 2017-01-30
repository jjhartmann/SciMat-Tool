/*
 * StatisticDialogManager.java
 *
 * Created on 26-ene-2012, 13:11:56
 */
package scimat.gui.components.statistic;

import javax.swing.JFrame;

/**
 *
 * @author mjcobo
 */
public class StatisticDialogManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private AuthorGroupStatisticDialog authorGroupStatisticDialog;
  private ReferenceGroupStatisticDialog referenceGroupStatisticDialog;
  private WordGroupStatisticDialog wordGroupStatisticDialog;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  private StatisticDialogManager() {
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @return
   */
  public static StatisticDialogManager getInstance() {

    return StatisticDialogManagerHolder.INSTANCE;
  }

  /**
   *
   */
  private static class StatisticDialogManagerHolder {

    private static final StatisticDialogManager INSTANCE = new StatisticDialogManager();
  }
  
  /**
   * 
   * @param frame 
   */
  public void init(JFrame frame) {
  
    this.authorGroupStatisticDialog = new AuthorGroupStatisticDialog(frame);
    this.referenceGroupStatisticDialog = new ReferenceGroupStatisticDialog(frame);
    this.wordGroupStatisticDialog = new WordGroupStatisticDialog(frame);
  }
  
  /**
   * 
   */
  public void showAuthorGroupStatisticDialog() {
  
    this.authorGroupStatisticDialog.refresh();
    this.authorGroupStatisticDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showReferenceGroupStatisticDialog() {
  
    this.referenceGroupStatisticDialog.refresh();
    this.referenceGroupStatisticDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showWordGroupStatisticDialog() {
  
    this.wordGroupStatisticDialog.refresh();
    this.wordGroupStatisticDialog.setVisible(true);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
