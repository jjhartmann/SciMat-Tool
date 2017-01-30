/*
 * NoUndoableTask.java
 *
 * Created on 10-mar-2011
 */

package scimat.gui.commands;

/**
 * Interfaz para las tareas del sistema que no se deshacen.
 * No es estrictamente necesaria pero si muy recomendable.
 *
 * @author mjcobo
 */
public interface NoUndoableTask {
  
  public void execute();
}
