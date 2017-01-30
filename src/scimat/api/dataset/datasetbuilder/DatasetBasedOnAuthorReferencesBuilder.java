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
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.dao.PublishDateDAO;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class DatasetBasedOnAuthorReferencesBuilder implements DatasetBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private KnowledgeBaseManager kbm;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DatasetBasedOnAuthorReferencesBuilder(KnowledgeBaseManager kbm) {

    this.kbm = kbm;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public Dataset execute(ArrayList<PublishDate> publishDateList) throws KnowledgeBaseException {

    int i, j, k, l;
    PublishDateDAO publishDateDAO = new PublishDateDAO(this.kbm);
    DocumentDAO documentDAO = new DocumentDAO(this.kbm);
    ReferenceDAO referenceDAO = new ReferenceDAO(this.kbm);
    AuthorReferenceDAO authorReferenceDAO = new AuthorReferenceDAO(this.kbm);
    Document document;
    ArrayList<Document> documentsList;
    Reference reference;
    ArrayList<Reference> referencesList;
    ArrayList<AuthorReferenceReference> authorReferenceReferenceList;
    AuthorReference authorReference;
    AuthorReferenceGroup authorReferenceGroup;

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

        // For each reference, if it has an AuthorReference associated, this
        // reference has a AuthorReferenceGroup associated and this group is
        // not a stopGroup, we add this item to the document in the dataset.
        for (k = 0; k < referencesList.size(); k++) {

          reference = referencesList.get(k);

          authorReferenceReferenceList = referenceDAO.getAuthorReferenceReferences(reference.getReferenceID());

          for (l = 0; l < authorReferenceReferenceList.size(); l++) {

            authorReference = authorReferenceReferenceList.get(l).getAuthorReference();

            authorReferenceGroup = authorReferenceDAO.getAuthorReferenceGroup(authorReference.getAuthorReferenceID());

            if ((authorReferenceGroup != null) && (!authorReferenceGroup.isStopGroup())) {

              try {

                dataset.addItemToDocument(document.getDocumentID(),
                        authorReferenceGroup.getAuthorReferenceGroupID(),
                        authorReferenceGroup.getGroupName());

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
