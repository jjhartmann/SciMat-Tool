/*
 * SubjectCategoryDAO.java
 *
 * Created on 21-oct-2010, 17:49:07
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddSubjectCategoryEvent;
import scimat.knowledgebaseevents.event.relation.JournalRelationSubjectCategoryRelationPublishDateEvent;
import scimat.knowledgebaseevents.event.remove.RemoveSubjectCategoryEvent;
import scimat.knowledgebaseevents.event.update.UpdateSubjectCategoryEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class SubjectCategoryDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO SubjectCategory(subjectCategoryName) VALUES(?);
   * </pre>
   */
  private final static String __INSERT_SUBJECTCATEGORY = "INSERT INTO SubjectCategory(subjectCategoryName) VALUES(?);";

  /**
   * <pre>
   * INSERT INTO SubjectCategory(idSbjectCategory,subjectCategoryName) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_SUBJECTCATEGORY_WITH_ID = "INSERT INTO SubjectCategory(idSubjectCategory,subjectCategoryName) VALUES(?,?);";

  /**
   * <pre>
   * DELETE SubjectCategory WHERE idSubjectCategory = ?;
   * </pre>
   */
  private final static String __REMOVE_SUBJECTCATEGORY = "DELETE FROM SubjectCategory "
                                                       + "WHERE idSubjectCategory = ?;";

  /**
   * <pre>
   * UPDATE SubjectCategory
   * SET subjectCategoryName = ?
   * WHERE idSubjectCategory = ?;
   * </pre>
   */
  private final static String __UPDATE_SUBJECTCATEGORYNAME = "UPDATE SubjectCategory "
                                                           + "SET subjectCategoryName = ? "
                                                           + "WHERE idSubjectCategory = ?;";

  /**
   * <pre>
   * SELECT j.idJournal, j.source, j.conferenceInformation,
   *        s.idSubjectCategory, s.subjectCategoryName,
   *        p.idPublishDate, p.year, p.date
   * FROM PublishDate p, Journal j, SubjectCategory s, Journal_SubjectCategory_PublishDate jsp
   * WHERE j.idJournal = ? AND
   *       j.idJournal = jsp.Journal_idJournal AND
   *       jsp.SubjectCategory_idSubjectCategory = s.idSubjectCategory AND
   *       jsp.PublishDate_idPublishDate = p.idPublishDate;
   * </pre>
   */
  private final static String __SELECT_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE = "SELECT j.*, s.*, p.* "
                                                                             + "FROM PublishDate p, Journal j, SubjectCategory s, Journal_SubjectCategory_PublishDate jsp "
                                                                             + "WHERE s.idSubjectCategory = ? AND "
                                                                             + "      j.idJournal = jsp.Journal_idJournal AND "
                                                                             + "      jsp.SubjectCategory_idSubjectCategory = s.idSubjectCategory AND "
                                                                             + "      jsp.PublishDate_idPublishDate = p.idPublishDate;";
  
  private final static String __SELECT_SUBJECTCATEGORY_BY_ID = "SELECT * FROM SubjectCategory WHERE idSubjectCategory = ?;";
  private final static String __SELECT_SUBJECTCATEGORY_BY_CATEGORYNAME = "SELECT * FROM SubjectCategory WHERE subjectCategoryName = ?;";
  private final static String __SELECT_SUBJECTCATEGORIES = "SELECT * FROM SubjectCategory;";
  private final static String __CHECK_SUBJECTCATEGORY_BY_CATEGORYNAME = "SELECT idSubjectCategory FROM SubjectCategory WHERE subjectCategoryName = ?;";
  private final static String __CHECK_SUBJECTCATEGORY_BY_ID = "SELECT idSubjectCategory FROM SubjectCategory WHERE idSubjectCategory = ?;";
  
  private PreparedStatement statCheckSubjectCategoryByCategoryName;
  private PreparedStatement statCheckSubjectCategoryById;
  private PreparedStatement statAddSubjectCategory;
  private PreparedStatement statAddSubjectCategoryWithId;
  private PreparedStatement statRemoveSubjectCategory;
  private PreparedStatement statSelectJournalSubjectCategoryPublishDate;
  private PreparedStatement statSelectSubjectCategories;
  private PreparedStatement statSelectSubjectCategoryByCategoryName;
  private PreparedStatement statSelectSubjectCategoryById;
  private PreparedStatement statUpdateCategoryName;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public SubjectCategoryDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {

      this.statCheckSubjectCategoryByCategoryName = this.kbm.getConnection().prepareStatement(__CHECK_SUBJECTCATEGORY_BY_CATEGORYNAME);
      this.statCheckSubjectCategoryById = this.kbm.getConnection().prepareStatement(__CHECK_SUBJECTCATEGORY_BY_ID);
      this.statAddSubjectCategory = this.kbm.getConnection().prepareStatement(__INSERT_SUBJECTCATEGORY, Statement.RETURN_GENERATED_KEYS);
      this.statAddSubjectCategoryWithId = this.kbm.getConnection().prepareStatement(__INSERT_SUBJECTCATEGORY_WITH_ID);
      this.statRemoveSubjectCategory = this.kbm.getConnection().prepareStatement(__REMOVE_SUBJECTCATEGORY);
      this.statSelectJournalSubjectCategoryPublishDate = this.kbm.getConnection().prepareStatement(__SELECT_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE);
      this.statSelectSubjectCategories = this.kbm.getConnection().prepareStatement(__SELECT_SUBJECTCATEGORIES);
      this.statSelectSubjectCategoryByCategoryName = this.kbm.getConnection().prepareStatement(__SELECT_SUBJECTCATEGORY_BY_CATEGORYNAME);
      this.statSelectSubjectCategoryById = this.kbm.getConnection().prepareStatement(__SELECT_SUBJECTCATEGORY_BY_ID);
      this.statUpdateCategoryName = this.kbm.getConnection().prepareStatement(__UPDATE_SUBJECTCATEGORYNAME);

     } catch (SQLException e) {
     
       throw new KnowledgeBaseException(e.getMessage(), e.getCause());
     }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param subjectCategoryName
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addSubjectCategory(String subjectCategoryName, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddSubjectCategory.clearParameters();

      this.statAddSubjectCategory.setString(1, subjectCategoryName);

      if (this.statAddSubjectCategory.executeUpdate() == 1 ) {

        id = this.statAddSubjectCategory.getGeneratedKeys().getInt(1);
        this.statAddSubjectCategory.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddSubjectCategoryEvent(getSubjectCategory(id)));
    }

    return id;
  }

  /**
   *
   * @param subjectCategoryID
   * @param subjectCategoryName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addSubjectCategory(Integer subjectCategoryID, String subjectCategoryName, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddSubjectCategoryWithId.clearParameters();

      this.statAddSubjectCategoryWithId.setInt(1, subjectCategoryID);
      this.statAddSubjectCategoryWithId.setString(2, subjectCategoryName);

      result = this.statAddSubjectCategoryWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddSubjectCategoryEvent(getSubjectCategory(subjectCategoryID)));
    }

    return result;
  }

  /**
   * 
   * @param subjectCategory
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addSubjectCategory(SubjectCategory subjectCategory, boolean notifyObservers)
          throws KnowledgeBaseException {

    return addSubjectCategory(subjectCategory.getSubjectCategoryID(),
                              subjectCategory.getSubjectCategoryName(),
                              notifyObservers);
  }

  /**
   *
   * @param subjectCategoryID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeSubjectCategory(Integer subjectCategoryID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    SubjectCategory subjectCategory = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      subjectCategory = getSubjectCategory(subjectCategoryID);
    }

    try {

      this.statRemoveSubjectCategory.clearParameters();

      this.statRemoveSubjectCategory.setInt(1, subjectCategoryID);

      result = this.statRemoveSubjectCategory.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveSubjectCategoryEvent(subjectCategory));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new JournalRelationSubjectCategoryRelationPublishDateEvent());
    }
    
    return result;
  }

  /**
   *
   * @param idSubjectCategory the subject category's ID
   *
   * @return a <ocde>SubjectCategory</code> or null if there is not any
   *         subject category with this ID
   *
   * @throws KnowledgeBaseException
   */
  public SubjectCategory getSubjectCategory(Integer idSubjectCategory)
          throws KnowledgeBaseException {

    ResultSet rs;
    SubjectCategory subjectCategory = null;

    try {

      this.statSelectSubjectCategoryById.clearParameters();

      this.statSelectSubjectCategoryById.setInt(1, idSubjectCategory);

      rs = this.statSelectSubjectCategoryById.executeQuery();

      if (rs.next()) {

        subjectCategory = UtilsDAO.getInstance().getSubjectCategory(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return subjectCategory;
  }

  /**
   *
   * @param subjectCategoryName the subject category's name
   *
   * @return a <ocde>SubjectCategory</code> or null if there is not any
   *         subject category with this name
   *
   * @throws KnowledgeBaseException
   */
  public SubjectCategory getSubjectCategory(String subjectCategoryName)
          throws KnowledgeBaseException {

    ResultSet rs;
    SubjectCategory subjectCategory = null;

    try {

      this.statSelectSubjectCategoryByCategoryName.clearParameters();

      this.statSelectSubjectCategoryByCategoryName.setString(1, subjectCategoryName);

      rs = this.statSelectSubjectCategoryByCategoryName.executeQuery();

      if (rs.next()) {

        subjectCategory = UtilsDAO.getInstance().getSubjectCategory(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return subjectCategory;
  }

  /**
   *
   * @return a <ocde>SubjectCategory</code> or null if there is not any
   *         subject category with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<SubjectCategory> getSubjectCategories()
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<SubjectCategory> subjectCategoriesList = new ArrayList<SubjectCategory>();

    try {

      this.statSelectSubjectCategories.clearParameters();

      rs = this.statSelectSubjectCategories.executeQuery();

      while (rs.next()) {

        subjectCategoriesList.add(UtilsDAO.getInstance().getSubjectCategory(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return subjectCategoriesList;
  }

  /**
   * @param subjectCategoryName the subjectCategoryName to set
   */
  public boolean setSubjectCategoryName(Integer subjectCategoryID, String subjectCategoryName, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateCategoryName.clearParameters();

      this.statUpdateCategoryName.setString(1, subjectCategoryName);
      this.statUpdateCategoryName.setInt(2, subjectCategoryID);

      result = this.statUpdateCategoryName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateSubjectCategoryEvent(getSubjectCategory(subjectCategoryID)));
    }
    
    return result;
  }

  /**
   *
   * @return an array with the authors associated with this document
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<JournalSubjectCategoryPublishDate> getJournals(Integer subjectCategoryID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<JournalSubjectCategoryPublishDate> jspList = new ArrayList<JournalSubjectCategoryPublishDate>();

    try {

      this.statSelectJournalSubjectCategoryPublishDate.clearParameters();

      this.statSelectJournalSubjectCategoryPublishDate.setInt(1, subjectCategoryID);

      rs = this.statSelectJournalSubjectCategoryPublishDate.executeQuery();

      while (rs.next()) {

        jspList.add(UtilsDAO.getInstance().getJournalSubjectCategoryPublishDate(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return jspList;
  }

  /**
   * <p>Check if there is an <code>SubjectCategory</code> with this subject
   * category's name.</p>
   *
   * @param subjectCategoryName a string with the subject category's name
   *
   * @return true if there is an <code>SubjectCategoryName</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkSubjectCategory(String subjectCategoryName) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckSubjectCategoryByCategoryName.clearParameters();

      this.statCheckSubjectCategoryByCategoryName.setString(1, subjectCategoryName);

      rs = this.statCheckSubjectCategoryByCategoryName.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>SubjectCategory</code> with this ID.</p>
   *
   * @param idSubjectCategory the subject category's ID
   *
   * @return true if there is an <code>SubjectCategoryName</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkSubjectCategory(Integer idSubjectCategory)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckSubjectCategoryById.clearParameters();

      this.statCheckSubjectCategoryById.setInt(1, idSubjectCategory);

      rs = this.statCheckSubjectCategoryById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param subjectCategoriesToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<SubjectCategory> refreshSubjectCategorys(ArrayList<SubjectCategory> subjectCategoriesToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<SubjectCategory> subjectCategorys = new ArrayList<SubjectCategory>();
    
    i = 0;
    
    if (!subjectCategoriesToRefresh.isEmpty()) {

      query = "SELECT * FROM SubjectCategory WHERE idSubjectCategory IN (" + subjectCategoriesToRefresh.get(i).getSubjectCategoryID();
      
      for (i = 1; i < subjectCategoriesToRefresh.size(); i++) {
        
        query += ", " + subjectCategoriesToRefresh.get(i).getSubjectCategoryID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          subjectCategorys.add(UtilsDAO.getInstance().getSubjectCategory(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return subjectCategorys;
  }
  
  /**
   * 
   * @param subjectCategoryToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public SubjectCategory refreshSubjectCategory(SubjectCategory subjectCategoryToRefresh) throws KnowledgeBaseException {
  
    return getSubjectCategory(subjectCategoryToRefresh.getSubjectCategoryID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
