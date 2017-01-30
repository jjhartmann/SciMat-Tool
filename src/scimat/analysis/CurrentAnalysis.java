/*
 * CurrentAnalysis.java
 *
 * Created on 13-mar-2011, 17:19:30
 */
package scimat.analysis;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author mjcobo
 */
public class CurrentAnalysis {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private GlobalAnalysisResult results = null;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  private CurrentAnalysis() {
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public static CurrentAnalysis getInstance() {
    return CurrentAnalysisHolder.INSTANCE;
  }

  /**
   *
   */
  private static class CurrentAnalysisHolder {

    private static final CurrentAnalysis INSTANCE = new CurrentAnalysis();
  }

  /**
   *
   * @return
   */
  public GlobalAnalysisResult getResults() {
    return results;
  }

  /**
   * 
   * @param results
   */
  public void setResults(GlobalAnalysisResult results) {
    this.results = results;
  }

  /**
   * 
   * @param path
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void loadResults(String path) throws IOException, ClassNotFoundException {

    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
    
    this.results = (GlobalAnalysisResult) ois.readObject();

    ois.close();

  }

  /**
   * 
   * @param path
   * @throws IOException
   */
  public void saveResults(String path) throws IOException{

    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
    oos.writeObject(this.results);
    
    oos.close();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
