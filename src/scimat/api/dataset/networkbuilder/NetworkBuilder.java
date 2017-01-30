/*
 * NetworkBuilder.java
 *
 * Created on 11-feb-2011, 13:48:56
 */

package scimat.api.dataset.networkbuilder;

import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 *
 * @author mjcobo
 */
public interface NetworkBuilder {

  public UndirectNetworkMatrix execute() throws NotExistsItemException;
}
