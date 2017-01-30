/*
 * NetworkCouplingBuilder.java
 *
 * Created on 11-feb-2011, 13:52:18
 */
package scimat.api.dataset.networkbuilder;

import java.util.ArrayList;
import java.util.TreeSet;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 * This class build a network from a dataset using the coupling of the
 * items in each doc.
 * 
 * @author mjcobo
 */
public class NetworkCouplingBuilder implements NetworkBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public NetworkCouplingBuilder(Dataset dataset) {

    this.dataset = dataset;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Build he network.
   *
   * @param dataset The dataset used to extract the coupling values.
   *
   * @return a network based on coupling data.
   */
  public UndirectNetworkMatrix execute() throws NotExistsItemException {

    int i,j;
    ArrayList<Integer> docs;
    TreeSet<Integer> items, tmp;

    UndirectNetworkMatrix network;

    docs = this.dataset.getDocuments();

    network = new UndirectNetworkMatrix(docs);

    for (i = 0; i < docs.size(); i++) {

      items = new TreeSet<Integer>(this.dataset.getItemsInDocument(docs.get(i)));

      for (j = i + 1; j < docs.size(); j++) {

        tmp = new TreeSet<Integer>(this.dataset.getItemsInDocument(docs.get(j)));

        tmp.retainAll(items);

        network.addEdge(docs.get(i), docs.get(j), tmp.size());

      }
    }

    return network;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
