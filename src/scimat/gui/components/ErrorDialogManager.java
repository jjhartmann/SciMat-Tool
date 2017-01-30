/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimat.gui.components;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author mjcobo
 */
public class ErrorDialogManager {
  
  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Component component;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  private ErrorDialogManager() {
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  public static ErrorDialogManager getInstance() {
    return ErrorDialogManagerHolder.INSTANCE;
  }
  
  /**
   * 
   */
  private static class ErrorDialogManagerHolder {

    private static final ErrorDialogManager INSTANCE = new ErrorDialogManager();
  }
  
  /**
   * 
   * @param component 
   */
  public void  init(Component component) {
  
    this.component = component;
  }
  
  /**
   * 
   * @param message 
   */
  public void showError(String message) {
  
    JOptionPane.showMessageDialog(this.component, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
  
  /**
   * 
   * @param message 
   */
  public void showException(Exception e) {
  
    e.printStackTrace(System.err);
    
    JOptionPane.showMessageDialog(this.component, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
  }
}
