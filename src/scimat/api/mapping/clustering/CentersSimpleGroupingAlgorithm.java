/*
 * CentersSimpleGroupingAlgorithm.java
 *
 * Created on 16-feb-2011, 17:17:33
 */
package scimat.api.mapping.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import scimat.api.dataset.NetworkPair;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public class CentersSimpleGroupingAlgorithm implements ClusteringAlgorithm {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private int minNetworkSize;

  /**
   * 
   */
  private int maxNetworkSize;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public CentersSimpleGroupingAlgorithm(int minNetworkSize, int maxNetworkSize) {
    
    this.minNetworkSize = minNetworkSize;
    this.maxNetworkSize = maxNetworkSize;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public ClusterSet execute(UndirectNetworkMatrix network) {
    
    int i;
    boolean themeFound;
    ClusterSet result;
    ArrayList<NetworkPair> pairList;
    TreeMap<Integer, Integer> vNodesCounter = new TreeMap<Integer, Integer>();
    NetworkPair pair;
    int currentMaxNetworkSize = this.maxNetworkSize - 1;

    result = new ClusterSet(network);

    // Obtebemos la lista de pares y la ordenamos en orden decreciente.
    pairList = network.getNetworkPairs();
    
    Collections.sort(pairList, new Comparator<NetworkPair>(){
      public int compare(NetworkPair o1, NetworkPair o2) {

        return Double.compare(o2.getValue(), o1.getValue());
      }
    });
    
    i = 0;

    while (i < pairList.size()) {

      themeFound = false;
      //System.out.println(i + " --> " + pairList.size());
      pair = pairList.get(i);

      incrementNodesCounter(pair.getID().getElementA(), vNodesCounter);
      incrementNodesCounter(pair.getID().getElementB(), vNodesCounter);

      if (vNodesCounter.get(pair.getID().getElementA()).intValue() == currentMaxNetworkSize) {
      
        result.addCluster(extractThemeNodes(pairList, i, pair.getID().getElementA(),
                currentMaxNetworkSize), pair.getID().getElementA());

        themeFound = true;

      } else if (vNodesCounter.get(pair.getID().getElementB()).intValue() == currentMaxNetworkSize) {
      
        result.addCluster(extractThemeNodes(pairList, i, pair.getID().getElementB(),
                currentMaxNetworkSize), pair.getID().getElementB());

        themeFound = true;
      }

      // Si hemos encontrado un tema, inicializamos el vector de contadores y
      // nos situamos al principio de la lista de pares. En caso contrario
      // avanzamos un posicion en la lista de pares.
      if (themeFound) {

        vNodesCounter.clear();
        i = 0;

      } else {

        i ++;

        // Si llegamos al final de la lista y todavia no hemos alcanzado el
        // minimo tamaño de la red, nos sitauamos al principio de la lista de
        // pares y decrementamos en una unidad el tamaño maximo de la red.
        if ((i == pairList.size()) && (currentMaxNetworkSize > (this.minNetworkSize - 1))) {

          vNodesCounter.clear();

          i = 0;
          currentMaxNetworkSize --;
        }
      }
    }

    return result;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * @param id
   * @param vNodesCounter
   */
  private void incrementNodesCounter(Integer id, TreeMap<Integer, Integer> vNodesCounter) {

    if (vNodesCounter.containsKey(id)) {

      vNodesCounter.put(id, vNodesCounter.get(id) + 1);

    } else {

      vNodesCounter.put(id, 1);
    }
  }

  /**
   * 
   * @param pairList
   * @param highestPairPosition
   * @param mainNodeIndex
   * @param currentMaxNetworkSize
   * @return
   */
  private ArrayList<Integer> extractThemeNodes(ArrayList<NetworkPair> pairList,
          int highestPairPosition, Integer mainNodeIndex, int currentMaxNetworkSize) {

    int i, nAddedPairs;
    ArrayList<Integer> themeNodes;

    // Creamos el tema.
    themeNodes = new ArrayList<Integer>();

    // Recorremos la lista en orden inverso a partir de la posicion
    // highestPairPosition hasta que el tema tenga un numero de pares igual
    // al tamaño maximo actual de la red.
    i = highestPairPosition;
    nAddedPairs = 0;

    themeNodes.add(mainNodeIndex);

    while (nAddedPairs < currentMaxNetworkSize) {

      // Si el par tiene contiene al nodo principal lo añadimos a la lista de nodos
      // del tema.

      if (pairList.get(i).getID().getElementA().intValue() == mainNodeIndex.intValue()) {

        themeNodes.add(pairList.get(i).getID().getElementB());

        nAddedPairs ++;

      } else if (pairList.get(i).getID().getElementB().intValue() == mainNodeIndex.intValue()) {

        themeNodes.add(pairList.get(i).getID().getElementA());

        nAddedPairs ++;
      }

      i--;

      //System.out.println("i: " + i + " nAddedPairs: " + nAddedPairs + " highestPairPosition: " + highestPairPosition + " currentMaxNetworkSize: " + currentMaxNetworkSize);

    }

    removeUsedPair(pairList, themeNodes);

    return themeNodes;
  }

  /**
   * 
   * @param pairList
   * @param themeNodes
   */
  private void removeUsedPair(ArrayList<NetworkPair> pairList, ArrayList<Integer> themeNodes) {

    int i, j;
    NetworkPair pair;

    for (i = 0; i < themeNodes.size(); i++) {

      for (j = 0; j < pairList.size(); j++) {

        pair = pairList.get(j);

        if ((pair.getID().getElementA().intValue() == themeNodes.get(i).intValue()) ||
            (pair.getID().getElementB().intValue() == themeNodes.get(i).intValue())) {

          pairList.remove(j);

          j--;
        } // END IF
      } // END FOR J
    } // END FOR I
  }

  

}
