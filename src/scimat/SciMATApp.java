/*
 * SciMATApp.java
 *
 * Created on 06-mar-2011, 20:57:22
 */
package scimat;

import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import scimat.gui.MainFrame;

/**
 *
 * @author mjcobo
 */
public class SciMATApp {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    try {

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      Locale.setDefault(Locale.UK);

      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          new MainFrame().setVisible(true);
        }
      });

    } catch (ClassNotFoundException e) {

      System.out.println(e.getClass().getCanonicalName());
      System.out.println(e.getCause());
      System.out.println(e.getMessage());

    } catch (InstantiationException e) {
      
      System.out.println(e.getClass().getCanonicalName());
      System.out.println(e.getCause());
      System.out.println(e.getMessage());
      
    } catch (IllegalAccessException e) {
      
      System.out.println(e.getClass().getCanonicalName());
      System.out.println(e.getCause());
      System.out.println(e.getMessage());
      
    } catch (UnsupportedLookAndFeelException e) {
      
      System.out.println(e.getClass().getCanonicalName());
      System.out.println(e.getCause());
      System.out.println(e.getMessage());
      
    } catch (OutOfMemoryError e) {

      System.out.println("Out of mememory");
      System.out.println("Please execute the application with more Java heap space");
      System.out.println("Use the option: -Xmx");
      System.out.println("i.e. -Xmx1024m");
      e.printStackTrace(System.err);
    }

  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
