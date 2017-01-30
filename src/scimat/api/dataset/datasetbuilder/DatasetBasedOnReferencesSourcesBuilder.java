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
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class DatasetBasedOnReferencesSourcesBuilder implements DatasetBuilder {

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
  public DatasetBasedOnReferencesSourcesBuilder(KnowledgeBaseManager kbm) {

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
    ReferenceSourceDAO referenceSourceDAO = new ReferenceSourceDAO(this.kbm);
    Document document;
    ArrayList<Document> documentsList;
    Reference reference;
    ArrayList<Reference> referencesList;
    ReferenceSource referenceSource;
    ReferenceSourceGroup referenceSourceGroup;

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

        // For each reference, if it has a referenceSource associated, this
        // reference has a referenceSourceGroup associated and this group is
        // not a stopGroup, we add this item to the document in the dataset.
        for (k = 0; k < referencesList.size(); k++) {

          reference = referencesList.get(k);

          referenceSource = referenceDAO.getReferenceSource(reference.getReferenceID());

          if (referenceSource != null) {

            referenceSourceGroup = referenceSourceDAO.getReferenceSourceGroup(referenceSource.getReferenceSourceID());

            if ((referenceSourceGroup != null) && (!referenceSourceGroup.isStopGroup())) {

              try {

                dataset.addItemToDocument(document.getDocumentID(),
                        referenceSourceGroup.getReferenceSourceGroupID(),
                        referenceSourceGroup.getGroupName());

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

    }

    return dataset;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
