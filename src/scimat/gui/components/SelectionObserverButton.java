/*
 * SelectionObserverButton.java
 *
 * Created on 26-nov-2008
 */

package scimat.gui.components;

import javax.swing.JButton;
import scimat.gui.components.observer.SelectionObserver;

/**
 * Extiende la funcionalidad de JButton para que observe las selecciones que
 * se realicen sobre las tablas. Dependiedo del modo en el que lo creemos
 * se activara cuando haya una seleccion multiple o simple.
 *
 * - Modo seleccion simple: unicamente se activara el boton cuando haya
 *                          seleccionado un unico elemento.
 * - Modo seleccion multiple: se seleccionara se hay seleccionado algun elemento.
 *
 * @author Manuel Jesus Cobo Martin.
 */
public class SelectionObserverButton extends JButton
        implements SelectionObserver {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Umbral minimo. Si se han seleccionado un numero de objetos mayor o igual
   * que este umbral el boton se activara. (Siempre que se cumpla la segunda
   * condicion). Un valor de -1 indicara que no existe umbral minimo.
   */
  private int _minThreshold;

  /**
   * Umbral maximo. Si se han seleccionado un numero de objetod menos o igual
   * que este umbral el boton se activara. (Siempre que se cumpla la primera
   * condicion). Un valor de -1 indicara que no exite umbral maximo.
   */
  private int _maxThreshold;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Crea una nueva instancia de SelectionObserverButton que observa
   * cualquier tipo de seleccion.
   */
  public SelectionObserverButton() {
    super("Button");

    _maxThreshold = _minThreshold = -1;
  }

  /**
   * Crea una nueva instancia de SelectionObserverButton. Dependiendo
   * de los umbrales elegidos observara un tipo diferente de seleccion.
   *
   * Si el umbral minimo esta por encima del umbral maximo o el umbral maximo
   * esta por debajo del umbral minimo, ambos conservaran el valor del umbral
   * minimo.
   *
   * @param minThreshold Umbral minimo. minThreshold <= maxThreshold. Si
   *                     minThreshold < 0 no se tendra encuenta el umbral minimo.
   * @param maxThreshold Umbral maximo. maxThreshold >= minThreshold. Si
   *                     maxThreshold < 0 no se tendra encuenta el umbral maximo.
   */
  public SelectionObserverButton(int minThreshold, int maxThreshold) {
    super();

    if ((minThreshold >= 0) && (maxThreshold >= 0)) {

      if (minThreshold <= maxThreshold) {

        _minThreshold = minThreshold;
        _maxThreshold = maxThreshold;

      } else {

        _minThreshold = _maxThreshold = minThreshold;
      }

    } else {

      _minThreshold = minThreshold;
      _maxThreshold = maxThreshold;
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Activa o desactiva el boton en funcion de la nueva seleccion. Si selection
   * es null se desactivara el boton, en caso contrario dependera del modo en el
   * que fue creado el boton.
   * 
   * @param selection nueva seleccion.
   */
  public void selectionChangeHappened(int[] selection) {

    boolean enabled = false;

    if (selection != null) {

      if (selection.length >= _minThreshold) {

        enabled = true;

      } else {

        enabled = false;

      }

      if ((_maxThreshold < 0) || (selection.length <= _maxThreshold)) {

        enabled = enabled && true;

      } else {

        enabled = false;
      }

    } else {

      enabled = false;
    }

    setEnabled(enabled);

  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

}
