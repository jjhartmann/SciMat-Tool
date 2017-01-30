/* ========================================================================
 * UndoStack.java
 * ========================================================================
 */
package scimat.gui.undostack;

import java.util.ArrayList;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Pila de �rdenes para deshacer.
 * Para conseguir que <TT>UndoManager</TT> sea un singleton se ha optado
 * por la opci�n de crear otra clase: <TT>UndoStack</TT> que la "recubre"
 * (<TT>UndoManager</TT> es un atributo de <TT>UndoStack</TT>). Y hacer
 * que la nueva clase sea un singleton.
 * PATR�N DE DISE�O: Singleton.
 * PAPEL EN EL PATR�N: No se aplica.
 * PATR�N DE DISE�O: Observer.
 * PAPEL EN EL PATR�N: Observable.
 *
 * @author mjcoo
 */
public class UndoStack {

  /*
   * = Atributos de clase ===============================================
   */
  /**
   * El singleton.
   */
  private static UndoStack __singleton = new UndoStack();
  /*
   * = Atributos de instancia ===========================================
   */
  /**
   * Pila de �rdenes para deshacer propiamente dicha.
   * Al ser <TT>UndoManager</TT> un agregado de esta clase, se puede
   * acceder a su funcionalidad como si fuese un singleton, a trav�s de
   * los m�todos de <TT>UndoStack</TT>.
   */
  private UndoManager undoManager = new UndoManager();
  /**
   * Lista de observadores interesados en ser notificados cuando la pila
   * cambie.
   */
  private ArrayList<UndoStackChangeObserver> undoStackListeners =
          new ArrayList<UndoStackChangeObserver>();

  /*
   * = Constructores ====================================================
   */
  /**
   * Constructor.
   * Crea una nueva instancia de <TT>UndoStack</TT>.
   * Es privado para que solo pueda instanciarse la �nica instancia que
   * queremos de la pila de �rdenes para deshacer.
   */
  private UndoStack() {
  }

  /*
   * = Otros ============================================================
   */
  /**
   * A�ade una nueva orden a la pila de �rdenes para deshacer.
   * @param edit Orden que se a�ade a la pila.
   */
  public static void addEdit(UndoableEdit edit) {
    __singleton.undoManager.addEdit(edit);
    
    __singleton.notifyUndoStackChanged();
  }

  /**
   * Deshacer la �ltima orden de la pila.
   */
  public static void undo() {
    __singleton.undoManager.undo();
    
    __singleton.notifyUndoStackChanged();
  }

  /**
   * Rehacer la �ltima orden de la pila.
   */
  public static void redo() {
    __singleton.undoManager.redo();
    
    __singleton.notifyUndoStackChanged();
  }

  /**
   * Notifica un cambio a los observadores.
   * Recorre la lista de observadores y a cada uno de ellos le indica
   * que la pila ha cambiado.
   * Este m�todo debe ser llamado por todos los m�todos de la clase
   * <TT>UndoStack</TT> que cambien el contenido de la pila.
   * Es privado, para que solo pueda ser llamado desde los m�todos que
   * cambien la pila.
   */
  private void notifyUndoStackChanged() {
    for (int i = 0; i < undoStackListeners.size(); i++) {
      undoStackListeners.get(i).undoStackChanged(__singleton.undoManager.canUndo(),
              __singleton.undoManager.canRedo());
    }
  }

  /**
   * A�ade un nuevo observador interesado en ser notificado cada vez que
   * la pila de �rdenes para deshacer cambie.
   * @param observer El nuevo observador que se a�ade.
   */
  public static void addUndoStackChangeObserver(UndoStackChangeObserver observer) {
    __singleton.undoStackListeners.add(observer);

    observer.undoStackChanged(__singleton.undoManager.canUndo(), __singleton.undoManager.canRedo());
  }
}

/*
 * ========================================================================
 */

