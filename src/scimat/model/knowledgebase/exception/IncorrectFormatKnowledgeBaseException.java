/*
 * IncorrectFormatKnowledgeBaseException.java
 *
 * Created on 21-oct-2010, 17:47:44
 */
package scimat.model.knowledgebase.exception;

/**
 *
 * @author mjcobo
 */
public class IncorrectFormatKnowledgeBaseException extends KnowledgeBaseException {

  /**
   * Creates a new instance of <code>IncorrectFormatKnowledgeBaseException</code> without
   * detail message.
   */
  public IncorrectFormatKnowledgeBaseException() {

  }

  /**
   * Constructs an instance of <code>IncorrectFormatKnowledgeBaseException</code> with the
   * specified detail message.
   * 
   * @param message the detail message.
   */
  public IncorrectFormatKnowledgeBaseException(String message) {
    super(message);
  }

  /**
   * Constructs an instance of <code>IncorrectFormatKnowledgeBaseException</code> with the
   * specified detail cause.
   *
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>IncorrectFormatKnowledgeBaseException.getCause()</code> method).
   *              (A null value is permitted, and indicates that the cause is
   *              nonexistent or unknown.)
   */
  public IncorrectFormatKnowledgeBaseException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs an instance of <code>IncorrectFormatKnowledgeBaseException</code> with the
   * specified detail cause.
   *
   * @param message the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>IncorrectFormatKnowledgeBaseException.getCause()</code> method). (A
   *              null value is permitted, and indicates that the cause is
   *              nonexistent or unknown.)
   */
  public IncorrectFormatKnowledgeBaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
