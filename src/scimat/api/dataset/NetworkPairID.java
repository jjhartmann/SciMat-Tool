/*
 * AssociationPairID.java
 *
 * Created on 04-mar-2009, 13:19:02
 */
package scimat.api.dataset;

import java.io.Serializable;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class NetworkPairID implements Comparable<NetworkPairID>, Serializable, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Integer elementA;
  private Integer elementB;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param elementA
   * @param elementB
   */
  public NetworkPairID(Integer elementA, Integer elementB) {

    if (elementA < elementB) {

      this.elementA = elementA;
      this.elementB = elementB;

    } else {

      this.elementA = elementB;
      this.elementB = elementA;

    }
  }

  /**
   * 
   * @param networkPairID
   */
  public NetworkPairID(NetworkPairID networkPairID) {

    this(networkPairID.elementA, networkPairID.elementB);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public Integer getElementA() {

    return this.elementA;
  }

  /**
   *
   * @return
   */
  public Integer getElementB() {

    return elementB;
  }

  public int compareTo(NetworkPairID o) {

    // Comparamos el elemento A
    int comp1 = this.elementA.compareTo(o.getElementA());

    // Si no son iguales devolvemos el valor de su comparacion.
    if (comp1 != 0){

      return comp1;

    } else { // Si son iguales comparamos el elemento B.

      return this.elementB.compareTo(o.getElementB());
    }
  }

  /**
   * s
   * @return
   */
  @Override
  public String toString() {
    return "(" + this.elementA + "," + this.elementB + ")";
  }

  @Override
  protected NetworkPairID clone() throws CloneNotSupportedException {

    NetworkPairID networkPairID;

    networkPairID = new NetworkPairID(elementA, elementB);

    return networkPairID;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
