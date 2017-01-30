/*
 * SimilarityMeasure.java
 *
 * Created on 14-feb-2011, 19:59:22
 */
package scimat.api.similaritymeasure.direct;

/**
 * This interface represents a generic similarity measure.
 * @author mjcobo
 */
public interface DirectSimilarityMeasure {

  public double calculateMeasure(int itemsCountI, int itemsCountJ, double itemsCountIJ);
}
