/*
 * CoOccurrenceNormalizer.java
 *
 * Created on 14-feb-2011, 23:47:57
 */
package scimat.api.similaritymeasure;

import java.util.ArrayList;
import scimat.api.dataset.AggregatedDataset;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.NetworkPair;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.exception.NotExistsItemException;
import scimat.api.similaritymeasure.direct.DirectSimilarityMeasure;

/**
 *
 * @author mjcobo
 */
public class CouplingNormalizer implements Normalizer {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private DirectSimilarityMeasure measure;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param measure
   */
  public CouplingNormalizer(DirectSimilarityMeasure measure) {
    this.measure = measure;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param dataset
   * @param network
   * @throws NotExistsItemException
   */
  public void execute(Dataset dataset, UndirectNetworkMatrix network)
          throws NotExistsItemException {

    int i;
    double normalizedValue;
    ArrayList<NetworkPair> pairs;
    NetworkPair pair;
    AggregatedDataset aggregatedDataset;

    pairs = network.getNetworkPairs();

    for (i = 0; i < pairs.size(); i++) {

      pair = pairs.get(i);

      if (dataset instanceof AggregatedDataset) {

        aggregatedDataset = (AggregatedDataset)dataset;

        normalizedValue = this.measure.calculateMeasure(aggregatedDataset.getDocumentsInHighLevelItemCount(pair.getID().getElementA()),
                                                 aggregatedDataset.getDocumentsInHighLevelItemCount(pair.getID().getElementB()),
                                                 pair.getValue());

      } else {

        normalizedValue = this.measure.calculateMeasure(dataset.getDocumentFrequency(pair.getID().getElementA()),
              dataset.getDocumentFrequency(pair.getID().getElementB()),
              pair.getValue());
      }

      network.addEdge(pair.getID().getElementA(), pair.getID().getElementB(), normalizedValue);
    }

  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
