/*
 * AnalysisPeriodResult.java
 *
 * Created on 04-abr-2011, 11:54:51
 */
package scimat.analysis;

import java.io.Serializable;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public class AnalysisPeriodResult implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Dataset originalDataset;
  private Dataset preprocessedDataset;
  private UndirectNetworkMatrix networkMatrix;
  private ClusterSet clusterSet;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param originalDataset
   * @param preprocessedDataset
   * @param networkMatrix
   * @param clusterSet
   */
  public AnalysisPeriodResult(Dataset originalDataset,
          Dataset preprocessedDataset,
          UndirectNetworkMatrix networkMatrix,
          ClusterSet clusterSet) {
    
    this.originalDataset = originalDataset;
    this.preprocessedDataset = preprocessedDataset;
    this.networkMatrix = networkMatrix;
    this.clusterSet = clusterSet;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public Dataset getOriginalDataset() {
    return originalDataset;
  }

  /**
   *
   * @return
   */
  public Dataset getPreprocessedDataset() {
    return preprocessedDataset;
  }

  /**
   *
   * @return
   */
  public UndirectNetworkMatrix getNetworkMatrix() {
    return networkMatrix;
  }

  /**
   * 
   * @return
   */
  public ClusterSet getClusterSet() {
    return clusterSet;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
