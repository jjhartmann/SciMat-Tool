/*
 * CoOccurrenceNormalizer.java
 *
 * Created on 14-feb-2011, 23:47:57
 */
package scimat.api.similaritymeasure;

import java.util.ArrayList;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.NetworkPair;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.exception.NotExistsItemException;
import scimat.api.similaritymeasure.direct.DirectSimilarityMeasure;

/**
 *
 * @author mjcobo
 */
public class CoOccurrenceNormalizer implements Normalizer {

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
  public CoOccurrenceNormalizer(DirectSimilarityMeasure measure) {
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

    pairs = network.getNetworkPairs();

    for (i = 0; i < pairs.size(); i++) {

      pair = pairs.get(i);

      normalizedValue = this.measure.calculateMeasure(dataset.getItemFrequency(pair.getID().getElementA()),
              dataset.getItemFrequency(pair.getID().getElementB()),
              pair.getValue());

      network.addEdge(pair.getID().getElementA(), pair.getID().getElementB(), normalizedValue);
    }

  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
