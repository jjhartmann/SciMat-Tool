/*
 * NetworkCoOccurrenceBuilder.java
 *
 * Created on 11-feb-2011, 13:52:18
 */
package scimat.api.dataset.networkbuilder;

import java.util.ArrayList;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 * This class build a network from a dataset using the co-occurrences of the
 * items in each doc.
 * 
 * @author mjcobo
 */
public class NetworkCoOccurrenceBuilder implements NetworkBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset dataset;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public NetworkCoOccurrenceBuilder(Dataset dataset) {

    this.dataset = dataset;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Build he network.
   *
   * @param dataset The dataset used to extract the co-occurrence values.
   *
   * @return a network based on co-occurrence data.
   */
  public UndirectNetworkMatrix execute() {

    int i,j,k;
    ArrayList<Integer> docs, items;
    Double value;
    UndirectNetworkMatrix network;

    network = new UndirectNetworkMatrix(this.dataset.getItems());

    docs = this.dataset.getDocuments();

    for (i = 0; i < docs.size(); i++) {

      try {

        items = this.dataset.getItemsInDocument(docs.get(i));

        for (j = 0; j < items.size(); j++) {

          for (k = j + 1; k < items.size(); k++) {

            value = network.getEdge(items.get(j), items.get(k));

            network.addEdge(items.get(j), items.get(k), value + 1);
          }
        }
        
      } catch (NotExistsItemException e) {

        System.err.println("An internal error happens. The document " +
                docs.get(i) + " does not exist in the dataset.");
        e.printStackTrace(System.err);
      }
    }


    return network;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
