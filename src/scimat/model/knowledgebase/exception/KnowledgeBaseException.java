/*
 * KnowledgeBaseException.java
 *
 * Created on 21-oct-2010, 17:47:44
 */
package scimat.model.knowledgebase.exception;

/**
 *
 * @author mjcobo
 */
public class KnowledgeBaseException extends Exception {

  /**
   * Creates a new instance of <code>KnowledgeBaseException</code> without
   * detail message.
   */
  public KnowledgeBaseException() {

  }

  /**
   * Constructs an instance of <code>KnowledgeBaseException</code> with the
   * specified detail message.
   * 
   * @param message the detail message.
   */
  public KnowledgeBaseException(String message) {
    super(message);
  }

  /**
   * Constructs an instance of <code>KnowledgeBaseException</code> with the
   * specified detail cause.
   *
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>KnowledgeBaseException.getCause()</code> method).
   *              (A null value is permitted, and indicates that the cause is
   *              nonexistent or unknown.)
   */
  public KnowledgeBaseException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs an instance of <code>KnowledgeBaseException</code> with the
   * specified detail cause.
   *
   * @param message the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>KnowledgeBaseException.getCause()</code> method). (A
   *              null value is permitted, and indicates that the cause is
   *              nonexistent or unknown.)
   */
  public KnowledgeBaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
