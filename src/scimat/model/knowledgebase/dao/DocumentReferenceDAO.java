/*
 * DocumentReferenceDAO.java
 *
 * Created on 02-mar-2011, 14:24:15
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.DocumentRelationReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceWithoutGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DocumentReferenceDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;
  /**
   * <rpe>
   * INSERT INTO Document_Reference(Reference_idReference,Document_idDocument) VALUES(?,?);
   * </pre>
   */
  private final static String __ADD_DOCUMENT_REFERENCE = "INSERT INTO Document_Reference(Reference_idReference,Document_idDocument) VALUES(?,?);";
  /**
   * <rpe>
   * DELETE Document_Reference
   * WHERE Reference_idReference = ? AND
   *       Document_idDocument = ?;
   * </pre>
   */
  private final static String __REMOVE_DOCUMENT_REFERENCE = "DELETE FROM Document_Reference "
          + "WHERE Reference_idReference = ? AND "
          + "      Document_idDocument = ?;";
  private final static String __CHECK_DOCUMENT_REFERENCE = "SELECT "
          + "Document_idDocument FROM Document_Reference WHERE "
          + "Document_idDocument = ? AND Reference_idReference = ?;";
  private PreparedStatement statAddDocumentReference;
  private PreparedStatement statCheckDocumentReference;
  private PreparedStatement statRemoveDocumentReference;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /**
   * 
   * @param kbm
   * @throws KnowledgeBaseException 
   */
  public DocumentReferenceDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {

    this.kbm = kbm;

    try {

      this.statAddDocumentReference = this.kbm.getConnection().prepareStatement(__ADD_DOCUMENT_REFERENCE);
      this.statCheckDocumentReference = this.kbm.getConnection().prepareStatement(__CHECK_DOCUMENT_REFERENCE);
      this.statRemoveDocumentReference = this.kbm.getConnection().prepareStatement(__REMOVE_DOCUMENT_REFERENCE);

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  /**
   * 
   * @param documentID
   * @param referenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addDocumentReference(Integer documentID, Integer referenceID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddDocumentReference.clearParameters();

      this.statAddDocumentReference.setInt(1, referenceID);
      this.statAddDocumentReference.setInt(2, documentID);

      result = this.statAddDocumentReference.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      ReferenceDAO referenceDAO = new ReferenceDAO(kbm);
      Reference reference = referenceDAO.getReference(referenceID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(reference));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationReferenceEvent());

      // Reference group

      ReferenceGroup referenceGroup = referenceDAO.getReferenceGroup(referenceID);

      if (referenceGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(referenceGroup));

      } else {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }

      // Author-Reference 

      ArrayList<AuthorReference> authorReferences = referenceDAO.getAuthorReferences(referenceID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(authorReferences));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(referenceDAO.getAuthorReferencesWithoutGroup(referenceID)));

      // Author-Reference group

      AuthorReferenceGroup authorReferenceGroup;
      TreeSet<AuthorReferenceGroup> authorReferenceGroups = new TreeSet<AuthorReferenceGroup>();

      for (int i = 0; i < authorReferences.size(); i++) {

        authorReferenceGroup = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReferenceGroup(authorReferences.get(i).getAuthorReferenceID());

        if (authorReferenceGroup != null) {

          authorReferenceGroups.add(authorReferenceGroup);
        }
      }

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(new ArrayList<AuthorReferenceGroup>(authorReferenceGroups)));

      // Reference-Source

      ReferenceSource referenceSource = referenceDAO.getReferenceSource(referenceID);

      if (referenceSource != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(referenceSource));
        //KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(referenceDAO.getReferenceSourceWithoutGroup(referenceID)));

        // Reference-Source group

        ReferenceSourceGroup referenceSourceGroup;

        referenceSourceGroup = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().getReferenceSourceGroup(referenceSource.getReferenceSourceID());

        if (referenceSourceGroup != null) {

          KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(referenceSourceGroup));
        }
      }
    }

    return result;
  }

  /**
   * 
   * @param documentID
   * @param referenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeDocumentReference(Integer documentID, Integer referenceID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statRemoveDocumentReference.clearParameters();

      this.statRemoveDocumentReference.setInt(1, referenceID);
      this.statRemoveDocumentReference.setInt(2, documentID);

      result = this.statRemoveDocumentReference.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      ReferenceDAO referenceDAO = new ReferenceDAO(kbm);
      Reference reference = referenceDAO.getReference(referenceID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(reference));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationReferenceEvent());

      // Reference group

      ReferenceGroup referenceGroup = referenceDAO.getReferenceGroup(referenceID);

      if (referenceGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(referenceGroup));

      } else {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }

      // Author-Reference 

      ArrayList<AuthorReference> authorReferences = referenceDAO.getAuthorReferences(referenceID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(authorReferences));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(referenceDAO.getAuthorReferencesWithoutGroup(referenceID)));

      // Author-Reference group

      AuthorReferenceGroup authorReferenceGroup;
      TreeSet<AuthorReferenceGroup> authorReferenceGroups = new TreeSet<AuthorReferenceGroup>();

      for (int i = 0; i < authorReferences.size(); i++) {

        authorReferenceGroup = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReferenceGroup(authorReferences.get(i).getAuthorReferenceID());

        if (authorReferenceGroup != null) {

          authorReferenceGroups.add(authorReferenceGroup);
        }
      }

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(new ArrayList<AuthorReferenceGroup>(authorReferenceGroups)));

      // Reference-Source

      ReferenceSource referenceSource = referenceDAO.getReferenceSource(referenceID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(referenceSource));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(referenceDAO.getReferenceSourceWithoutGroup(referenceID)));

      // Reference-Source group

      ReferenceSourceGroup referenceSourceGroup;

      referenceSourceGroup = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().getReferenceSourceGroup(referenceSource.getReferenceSourceID());

      if (referenceSourceGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(referenceSourceGroup));
      }
    }

    return result;
  }

  /**
   * <p>Check if the <code>Document</code> and <Code>Reference<Code> are
   * associated.</p>
   *
   * @param idDocument the document's ID
   * @param idReference the reference's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkDocumentReference(Integer idDocument, Integer idReference)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckDocumentReference.clearParameters();

      this.statCheckDocumentReference.setInt(1, idDocument);
      this.statCheckDocumentReference.setInt(2, idReference);

      rs = this.statCheckDocumentReference.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
