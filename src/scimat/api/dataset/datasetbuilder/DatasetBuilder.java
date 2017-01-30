/*
 * DatasetBuilder.java
 *
 * Created on 15-feb-2011, 17:46:26
 */
package scimat.api.dataset.datasetbuilder;

import java.util.ArrayList;
import scimat.api.dataset.Dataset;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface DatasetBuilder {

  public Dataset execute(ArrayList<PublishDate> publishDateList) throws KnowledgeBaseException;
}
