/*
 * ObservableTask.java
 *
 * Created on 30-ene-2008
 */

package scimat.observabletask;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Manuel Jesus Cobo Martin
 * 
 * @param <T> the result type returned by this {@code ObservableTask's}
 *        {@code doInBackground} and {@code get} methods
 * @param <V> the type used for carrying out intermediate results by this
 *        {@code ObservableTask's} {@code publish} and {@code process} methods
 */
public abstract class ObservableTask<T,V> extends SwingWorker<T,V> implements PropertyChangeListener{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private LinkedList<InterimResultTaskObserver<V>> _lInterimResultTaskObserver = new LinkedList<InterimResultTaskObserver<V>>();
  private LinkedList<ProgressTaskObserver> _lProgressTaskObserver = new LinkedList<ProgressTaskObserver>();
  private LinkedList<StatusTaskObserver> _lStatusTaskObserver = new LinkedList<StatusTaskObserver>();
  
  private boolean _sendAllInterimResults = false;
  
  private boolean _determinateMode = true;
  
  private TaskStatus _taskStatus = TaskStatus.PENDING;
  
  private Exception _exception = null;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public ObservableTask() {
    super();
    
    addPropertyChangeListener(this);
  }

  /**
   * 
   * @param sendAllInterimResult
   */
  public ObservableTask(boolean sendAllInterimResult){
    super();
    
    _sendAllInterimResults = sendAllInterimResult;
    
    addPropertyChangeListener(this);
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param evt
   */
  public void propertyChange(PropertyChangeEvent evt) {
    
    if ("progress".equals(evt.getPropertyName())) {
      notifyProgressTaskObserver(isDeterminateMode(),(Integer)evt.getNewValue());
    }
  }
  
  /**
   * 
   * @return
   */
  public boolean getSendAllInterimResults(){
  
    return _sendAllInterimResults;
  }
  
  /**
   * 
   * @param sendAllInterimResults
   */
  public void setSendAllInterimResults(boolean sendAllInterimResults){
    _sendAllInterimResults = sendAllInterimResults;
  }
  
  /**
   * En caso de que ocurra una excepcion durante la ejecucion devuelve la 
   * excepcion ocurrida. Devuelve null si no ocuurio ninguna excepcion.
   * 
   * @return la excepcion ocurrida o null en caso de la ejecucion termine
   *         satisfactoriamente.
   */
  public Exception getException() {
    return _exception;
  }
  
  /***************************************************************************/
  /*                          Protected Methods                              */
  /***************************************************************************/
  
  /**
   * 
   * @param chunks
   */
  @Override
  protected void process(List<V> chunks) {
    
    if (_sendAllInterimResults){
    
      for (V interimResult : chunks) {
        notifyInterimResultTaskObserver(interimResult);
      }
      
    }else{
      
      notifyInterimResultTaskObserver(chunks.get(chunks.size() - 1));
      
    }
  }

  /**
   * 
   */
  @Override
  protected void done() {
    super.done();
    
    if (isCancelled()) {
      
      notifyStatusTaskObserver(TaskStatus.CANCELLED);
      
    } else if (_exception != null) {
      
      notifyStatusTaskObserver(TaskStatus.EXCEPTION);
      
    } else {
      
      notifyStatusTaskObserver(TaskStatus.DONE);
    }
  }
  
  /**
   * 
   * @return
   */
  protected boolean isDeterminateMode(){
    return _determinateMode;
  }
  
  /**
   * 
   * @param mode
   */
  protected void setDeterminateMode(boolean mode){
    _determinateMode = mode;
  }
  
  /**
   * 
   * @param status
   */
  protected void setTaskStatus(TaskStatus status){
    _taskStatus = status;
    notifyStatusTaskObserver(_taskStatus);
  }
  
  /**
   * 
   * @return
   */
  protected TaskStatus getTaskStatus(){
    return _taskStatus;
  }
  
  /**
   * 
   * @param min
   * @param max
   * @param actual
   * @return
   */
  protected int calculateProgres(long min, long max, long actual){
    
    return (int)((Math.abs(min - actual) / (double)Math.abs(min - max)) * 100);
  }
  
  /**
   * Actualiza el valor de la excepcion.
   * 
   * @param e excepcion a almacenar.
   */
  protected void setException(Exception e) {
    _exception = e;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                          Observer's Methods                             */
  /***************************************************************************/
  
  /////////////////////////////////////////////////////////////////////////////
  //
  // InterimResultTaskObserver
  //
  /////////////////////////////////////////////////////////////////////////////
  
  /**
   * 
   * @param observer
   */
  public void addInterimResultTaskObserver(InterimResultTaskObserver<V> observer){
  
    _lInterimResultTaskObserver.add(observer);
  }

  /**
   * 
   * @param observer
   * @return
   */
  public boolean removeInterimResultTaskObserver(InterimResultTaskObserver<V> observer){
  
    return _lInterimResultTaskObserver.remove(observer);
  }
  
  /**
   * 
   * @param interimResult
   */
  private void notifyInterimResultTaskObserver(V interimResult){
  
    for (int i = 0; i < _lInterimResultTaskObserver.size(); i++){
      _lInterimResultTaskObserver.get(i).interimResultChanged(interimResult);
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  //
  // ProgressTaskObserver
  //
  /////////////////////////////////////////////////////////////////////////////
  
  /**
   * 
   * @param observer
   */
  public void addProgressTaskObserver(ProgressTaskObserver observer){
  
    _lProgressTaskObserver.add(observer);
    
    notifyProgressTaskObserver(isDeterminateMode(), getProgress());
  }

  /**
   * 
   * @param observer
   * @return
   */
  public boolean removeProgressTaskObserver(ProgressTaskObserver observer){
  
    return _lProgressTaskObserver.remove(observer);
  }
  
  /**
   * 
   * @param mode
   * @param progress
   */
  private void notifyProgressTaskObserver(boolean mode, int progress){
  
    for (int i = 0; i < _lProgressTaskObserver.size(); i++){
      _lProgressTaskObserver.get(i).progressChanged(mode, progress);
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  //
  // StatusTaskObserver
  //
  /////////////////////////////////////////////////////////////////////////////
  
  /**
   * 
   * @param observer
   */
  public void addStatusTaskObserver(StatusTaskObserver observer){
  
    _lStatusTaskObserver.add(observer);
    
    notifyStatusTaskObserver(getTaskStatus());
  }

  /**
   * 
   * @param observer
   * @return
   */
  public boolean removeStatusTaskObserver(StatusTaskObserver observer){
  
    return _lStatusTaskObserver.remove(observer);
  }
  
  /**
   * 
   * @param status
   */
  private void notifyStatusTaskObserver(TaskStatus status){
  
    for (int i = 0; i < _lStatusTaskObserver.size(); i++){
      _lStatusTaskObserver.get(i).statusTaskChanged(status);
    }
  }
  
}
