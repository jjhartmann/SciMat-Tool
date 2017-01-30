/*
 * FilterDocumentsByCitations.java
 *
 * Created on 08-feb-2011, 18:12:21
 */
package scimat.api.preprocessing.reduction.data;

import java.util.ArrayList;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.exception.NotExistsItemException;

/**
 * This class implements a preprocessing method which removes all of the
 * documents in a dataset with a citations score below a specific value.
 * 
 * @author mjcobo
 */
public class FilterDocumentsByCitations implements DataFilter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private int minCitations;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Constructs a new filter which reduce the documents of a given dataset. Only
   * the documents with a citations higher than {@code minCitations} will stand
   * in the dataset after the filter will be applied. That is, a document will
   * stand in the dataset iff: {@code document.citations >= minCitations}.
   *
   * @param minCitations Minimum number of citations allowed by the filter.
   */
  public FilterDocumentsByCitations(int minCitations) {
    
    this.minCitations = minCitations;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * Perform the filter.
   *
   * NOTE: the dataset will be modified.
   * 
   * @param dataset Dataset on which the filter will be applied.
   */
  public void execute(Dataset dataset) {

    int i;
    Integer docID;
    ArrayList<Integer> docList;

    docList = dataset.getDocuments();

    for (i = 0; i < docList.size(); i++) {

      docID = docList.get(i);

      try {

        if (dataset.getDocumentCitations(docID) < this.minCitations) {

          dataset.removeDocument(docID);
        }

      } catch (NotExistsItemException e) {

        System.err.println("An internal error occur within the dataset. "
                + "The document " + docID + " does not exist.");
        e.printStackTrace(System.err);
      }
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
