/*
 * GenericExporter.java
 *
 * Created on 30-may-2011, 14:33:18
 */
package scimat.api.export;

import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public interface GenericExporter {

  public void execute() throws ExportException;
}
