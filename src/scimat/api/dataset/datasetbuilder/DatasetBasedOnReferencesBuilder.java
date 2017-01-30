/*
 * DatasetBasedOnWordsBuilder.java
 *
 * Created on 15-feb-2011, 17:48:32
 */
package scimat.api.dataset.datasetbuilder;

import java.util.ArrayList;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.exception.NotExistsItemException;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.dao.PublishDateDAO;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class DatasetBasedOnReferencesBuilder implements DatasetBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private KnowledgeBaseManager kbm;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   */
  public DatasetBasedOnReferencesBuilder(KnowledgeBaseManager kbm) {

    this.kbm = kbm;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param publishDateList
   * @return
   * @throws KnowledgeBaseException
   */
  public Dataset execute(ArrayList<PublishDate> publishDateList) throws KnowledgeBaseException {

    int i, j, k;
    PublishDateDAO publishDateDAO = new PublishDateDAO(this.kbm);
    DocumentDAO documentDAO = new DocumentDAO(this.kbm);
    ReferenceDAO referenceDAO = new ReferenceDAO(this.kbm);
    Document document;
    ArrayList<Document> documentsList;
    Reference reference;
    ArrayList<Reference> referencesList;
    ReferenceGroup referenceGroup;
    Dataset dataset;

    dataset = new Dataset();

    // For each year we retrieved all the documents associated with it.
    for (i = 0; i < publishDateList.size(); i++) {

      documentsList = publishDateDAO.getDocuments(publishDateList.get(i).getPublishDateID());

      // For each document we retrieved its associated references.
      for (j = 0; j < documentsList.size(); j++) {

        document = documentsList.get(j);

        // Add the document to the dataset
        dataset.addDocument(document.getDocumentID(), document.getCitationsCount());

        referencesList = documentDAO.getReferences(document.getDocumentID());

        // For each reference, if it has a referenceGroup associated and this
        // group is not a stopGroup, we add this item to the document in the
        // dataset.
        for (k = 0; k < referencesList.size(); k++) {

          reference = referencesList.get(k);

          referenceGroup = referenceDAO.getReferenceGroup(reference.getReferenceID());

          if ((referenceGroup != null) && (! referenceGroup.isStopGroup())) {

            try {

              dataset.addItemToDocument(document.getDocumentID(),
                      referenceGroup.getReferenceGroupID(),
                      referenceGroup.getGroupName());

            } catch (NotExistsItemException e) {

              System.err.println("An internal error occurs within the dataset "
                      + "construction. The document "
                      + document.getDocumentID() + " does not exist.");

              e.printStackTrace(System.err);
            }
          }
        }
      }

    }

    return dataset;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
