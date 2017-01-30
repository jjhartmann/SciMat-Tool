/*
 * PausableTask.java
 *
 * Created on 30-ene-2008
 */

package scimat.observabletask;

/**
 *
 * @author Manuel Jesus Cobo Martin
 * 
 * @param <T> the result type returned by this {@code PausableTask's}
 *        {@code doInBackground} and {@code get} methods
 * @param <V> the type used for carrying out intermediate results by this
 *        {@code PausableTask's} {@code publish} and {@code process} methods
 */
public abstract class PausableTask<T,V> extends ObservableTask<T,V>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  protected static final long __PAUSE_HALF_SECOND = 500;
  protected static final long __PAUSE_1_SECOND = 1000;
  protected static final long __PAUSE_2_SECOND = 2000;
  
  private boolean _pause = false;
    
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public PausableTask(){
    super();
  }
  
  /**
   * 
   * @param sendAllInterimResult
   */
  public PausableTask(boolean sendAllInterimResult){
    super(sendAllInterimResult);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   */
  public synchronized void pause(){
  
    _pause = true;
  }
  
  /**
   * 
   */
  public synchronized void resume(){
    
    _pause = false;
  }
  
  /**
   * 
   * @return
   */
  public synchronized boolean isPaused(){
  
    return _pause;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
