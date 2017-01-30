/*
 * Normalizer.java
 *
 * Created on 14-feb-2011, 23:47:57
 */

package scimat.api.similaritymeasure;

import scimat.api.dataset.Dataset;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public interface Normalizer {

  /**
   *
   * @param dataset
   * @param network
   *
   * @throws NotExistsItemException
   */
  public void execute(Dataset dataset, UndirectNetworkMatrix network) throws NotExistsItemException;
}
