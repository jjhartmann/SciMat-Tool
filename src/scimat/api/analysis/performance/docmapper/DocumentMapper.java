/*
 * DocumentMapper.java
 *
 * Created on 22-feb-2011, 20:09:14
 */
package scimat.api.analysis.performance.docmapper;

import java.util.ArrayList;

/**
 *
 * @author mjcobo
 */
public interface DocumentMapper {

  /**
   * Execute the documents assigner, returning a list with the identifiers of
   * the documents that belong to the items according to a specific
   * implementation.
   * 
   * @param itemsList A list with the identifier of the items.
   *
   * @return A list with the identifiers of the documents which are associated with the items.
   */
  public DocumentSet executeMapper(ArrayList<Integer> itemsList);
}
