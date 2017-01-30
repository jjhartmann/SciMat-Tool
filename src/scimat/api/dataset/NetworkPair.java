/*
 * AssociationPairItem.java
 *
 * Created on 04-mar-2009, 13:18:51
 */
package scimat.api.dataset;

import java.io.Serializable;

/**
 *
 * @author Manuel Jesus Cobo Martin
 */
public class NetworkPair implements Serializable, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private NetworkPairID networkPairID;

  /**
   * 
   */
  private double value;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param associationPairID
   */
  public NetworkPair(Integer elementAid, Integer elementBid,
          double value) {

    this.networkPairID = new NetworkPairID(elementAid, elementBid);
    this.value = value;
  }

  public NetworkPair(NetworkPair networkPair) {

    this.networkPairID = new NetworkPairID(networkPair.networkPairID);
    this.value = networkPair.value;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public NetworkPairID getID() {

    return networkPairID;
  }

  /**
   * 
   * @return
   */
  public double getValue() {
    return this.value;
  }
  
   /**
    * 
    * @return
    */
  @Override
  @SuppressWarnings("unchecked")
  public NetworkPair clone() {

    NetworkPair pair = new NetworkPair(this.networkPairID.getElementA(),
            this.networkPairID.getElementB(), value);

    return pair;
  }

  /**
   * 
   * @return
   */
  @Override
  public String toString() {

    String s;

    s = "NetoworkPairList(" +
            this.networkPairID  + "," +
            this.value;

    return s;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
