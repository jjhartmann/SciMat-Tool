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
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class DatasetBasedOnWordsBuilder implements DatasetBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private KnowledgeBaseManager kbm;

  private boolean authorKeyword;
  private boolean sourceKeyword;
  private boolean addedKeyword;
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   * @param authorKeyword
   * @param sourceKeyword
   * @param addedKeyword
   */
  public DatasetBasedOnWordsBuilder(KnowledgeBaseManager kbm, boolean authorKeyword, boolean sourceKeyword,
          boolean addedKeyword) {

    this.kbm = kbm;
    this.authorKeyword = authorKeyword;
    this.sourceKeyword = sourceKeyword;
    this.addedKeyword = addedKeyword;
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
    WordDAO wordDAO = new WordDAO(this.kbm);
    Document document;
    ArrayList<Document> documentsList;
    Word word;
    ArrayList<Word> wordsList;
    WordGroup wordGroup;
    Dataset dataset;

    dataset = new Dataset();

    // For each year we retrieved all the documents associated with it.
    for (i = 0; i < publishDateList.size(); i++) {

      documentsList = publishDateDAO.getDocuments(publishDateList.get(i).getPublishDateID());

      // For each document we retrieved its associated words.
      for (j = 0; j < documentsList.size(); j++) {

        document = documentsList.get(j);

        // Add the document to the dataset
        dataset.addDocument(document.getDocumentID(), document.getCitationsCount());

        wordsList = documentDAO.getWords(document.getDocumentID(),this.authorKeyword, this.sourceKeyword,
                this.addedKeyword);

        // For each word, if it has a wordGroup associated and this group is not
        // a stopGroup, we add this item to the document in the dataset.
        for (k = 0; k < wordsList.size(); k++) {

          word = wordsList.get(k);

          wordGroup = wordDAO.getWordGroup(word.getWordID());

          if ((wordGroup != null) && (! wordGroup.isStopGroup())) {

            try {

              dataset.addItemToDocument(document.getDocumentID(),
                      wordGroup.getWordGroupID(),
                      wordGroup.getGroupName());

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
