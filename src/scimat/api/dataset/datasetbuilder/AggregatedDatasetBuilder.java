/*
 * AggregatedDatasetBuilder.java
 *
 * Created on 15-feb-2011, 17:46:26
 */
package scimat.api.dataset.datasetbuilder;

import scimat.api.dataset.AggregatedDataset;
import scimat.api.dataset.Dataset;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface AggregatedDatasetBuilder {

  public AggregatedDataset execute(Dataset dataset) throws KnowledgeBaseException;
}
