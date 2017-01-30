/*
 * AggregatedClusters.java
 *
 * Created on 24-feb-2011, 21:37:59
 */
package scimat.api.analysis.network.aggregation;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;

/**
 *
 * @author mjcobo
 */
public class AggregatedClusters {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * 
   */
  private SparseDoubleMatrix2D matrix;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param clusters
   */
  public AggregatedClusters(int clusters) {

    this.matrix = new SparseDoubleMatrix2D(clusters, clusters);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public int clustersCount() {

    // Columns == rows
    return this.matrix.rows();
  }

  /**
   * 
   * @param cluster1
   * @param cluster2
   * @return
   *
   * @throws IndexOutOfBoundsException if cluster1<0 || cluster1>=clustersCount() || cluster2<0 || cluster2>=clustersCount()
   */
  public double getEdge(int cluster1, int cluster2) {

    return this.matrix.get(cluster1, cluster2);
  }

  /**
   * 
   * @param cluster1
   * @param cluster2
   * @param value
   *
   * @throws IndexOutOfBoundsException if cluster1<0 || cluster1>=clustersCount() || cluster2<0 || cluster2>=clustersCount()
   */
  public void setEdge(int cluster1, int cluster2, double value) {

    this.matrix.set(cluster1, cluster2, value);
    this.matrix.set(cluster2, cluster1, value);
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
