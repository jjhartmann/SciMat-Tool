/*
 * NodeDocumentMapper.java
 *
 * Created on 02-abr-2011, 12:32:55
 */
package scimat.api.analysis.performance.docmapper;

/**
 *
 * @author mjcobo
 */
public interface NodeDocumentMapper {

  /**
   * Execute the documents assigner, returning a list with the identifiers of
   * the documents that belong to the item according to a specific
   * implementation.
   * 
   * @param item The item ID.
   *
   * @return A list with the identifiers of the documents which are associated
   * with the item.
   */
  public DocumentSet executeMapper(Integer item);
}
