/*
 * CurrentProject.java
 *
 * Created on 22-sep-2008
 */

package scimat.project;

import scimat.model.upgrade.KnowledgeBaseVersion;
import java.io.File;
import scimat.project.observer.KnowledgeBaseStateObserver;
import java.util.LinkedList;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.dao.FactoryDAO;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.observer.KnowledgeBaseObserver;

/**
 * Esta clase representa al proyecto que actualmente esta cargado. Maintiene
 * una instancia con la base de conocimiento asociada. Ademas contiene toda
 * la informacion sobre la ubicacion de los ficheros del proyecto.
 * 
 * Implementa el patron Singleton, por lo que unicamente existira una copia
 * de esta clase en memoria.
 * 
 * @author Manuel Jesus Cobo Martin
 */
public class CurrentProject {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * Singleton que tiene la unica instancia que existira de esta clase.
   */
  //private static CurrentProject __singleton = new CurrentProject();

  /**
   * s
   */
  private String currentProjectPath;
  
  /**
   * Base de conocimiento. Tendra un valor igual a null en caso de que no haya
   * sido cargada.
   */
  private KnowledgeBaseManager kbm = null;

  private FactoryDAO factoryDAO;
  
  /**
   * Lista de observadores del estado de la base de conocimiento.
   */
  private LinkedList<KnowledgeBaseStateObserver> knowledgeBaseStateObservers = new LinkedList<KnowledgeBaseStateObserver>();

  /**
   * 
   */
  private KnowledgeBaseObserver kbObserver = new KnowledgeBaseObserver();
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Constructor por defecto. Es privado para que unicamente esta clase tenga
   * la posibilidad de crear instancias.
   */
  private CurrentProject() {
  
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public static CurrentProject getInstance() {
    return CurrentProjectSingleton.INSTANCE;
  }

  /**
   * 
   */
  private static class CurrentProjectSingleton {
    private static final CurrentProject INSTANCE = new CurrentProject();
  }

  /**
   * Crea un nuevo proyecto. La carpeta del proyecto se guardara en la ruta
   * indicada por path. Dentro de esta ruta no puede existir un directorio
   * con el nombre directoryName.
   * 
   * La creacion del proyecto llevara asociada la creacion de una base de 
   * conocimiento.
   * 
   * En el momento de crear el proyecto no puede existir ninguna base de 
   * conocimiento cargada.
   * 
   * @param path ruta donde alojaremos la carpeta del proyecto.
   * @param folderName carpeta bajo la que se encontrara los ficheros del 
   *                   proyecto.
   * 
   * @return devuelve true en caso de que el proyecto se haya podido crear.
   */
  public void newProyect(String folderPath, String filePath) throws KnowledgeBaseException {

    if (! isKnowledbaseLoaded()) {

       try {

        this.currentProjectPath = folderPath;

        this.kbm = new KnowledgeBaseManager();

        this.kbm.createKnowledgeBase(folderPath + File.separator + filePath);
        
        this.factoryDAO = new FactoryDAO(this.kbm);

        this.notifyKnowledgeBaseObsever(isKnowledbaseLoaded());
        this.kbObserver.fireKnowledgeBaseRefresh();

      } catch (KnowledgeBaseException e) {

        this.kbm = null;

        throw e;
      }
    }
  }
  
  /**
   * Carga un proyecto de SciMAT, cargando en memoria la base de conocimiento.
   * 
   * @param path ruta donde se aloja el proyecto.
   */
  public void loadProyect(String path) throws KnowledgeBaseException {
    
    // Si no existe ninguna base de datos cargada.
    if (! isKnowledbaseLoaded()) {

      try {

        this.currentProjectPath = path;

        this.kbm = new KnowledgeBaseManager();

        this.kbm.loadKnowledgeBase(path, true);
        
        this.factoryDAO = new FactoryDAO(this.kbm);

        this.notifyKnowledgeBaseObsever(isKnowledbaseLoaded());
        this.kbObserver.fireKnowledgeBaseRefresh();

      } catch (KnowledgeBaseException e) {
        
        this.kbm = null;

        throw e;
      }
    }
  }
  
  /**
   * 
   * @param path
   * @return
   * @throws KnowledgeBaseException 
   */
  public KnowledgeBaseVersion checkKnowledgeBaseVersion(String path) throws KnowledgeBaseException {
    
    KnowledgeBaseVersion version = KnowledgeBaseVersion.UNDEFINED;
    KnowledgeBaseManager kbmTmp;
    
    // Si no existe ninguna base de datos cargada.
    if (!isKnowledbaseLoaded()) {

      this.currentProjectPath = path;

      kbmTmp = new KnowledgeBaseManager();

      kbmTmp.loadKnowledgeBase(path, false);
      
      if (kbmTmp.checkKnowledgeBaseStructure()) {
      
        version = KnowledgeBaseVersion.V_1_03;
        
      } else if (kbmTmp.checkKnowledgeBaseStructureV1_02()) {
      
        version = KnowledgeBaseVersion.V_1_02;
        
      } else if (kbmTmp.checkKnowledgeBaseStructureV1_01()) {

        version = KnowledgeBaseVersion.V_1_01;

      }
      
      kbmTmp.close();
    }

    return version;
  }
  
  /**
   * Cierra el proyecto actual en caso de que exista uno previamente cargado.
   * 
   * @return true si se cerro correctamente.s
   */
  public void close() throws KnowledgeBaseException {
  
    if (isKnowledbaseLoaded()) {
      
      this.kbm.close();
      this.kbm = null;
      this.factoryDAO = null;
      
      this.notifyKnowledgeBaseObsever(isKnowledbaseLoaded());
    }
  }
  
  /**
   * Devuelve la base de conocimiento asociada al proyecto o null si no se ha
   * creado todavia.
   * 
   * @return la base de conocimiento o null en caso de que no se haya creado.
   */
  public KnowledgeBaseManager getKnowledgeBase() {
  
    return this.kbm;
  }

  /**
   * 
   * @return 
   */
  public KnowledgeBaseObserver getKbObserver() {
    return kbObserver;
  }

  /**
   *
   * @return
   */
  public FactoryDAO getFactoryDAO() {
    return factoryDAO;
  }

  /**
   * 
   * @return
   */
  public String getCurrentProjectPath() {
    return currentProjectPath;
  }

  /**
   * Devuelve true en caso de que la base de conocimiento este cargada.
   * 
   * @return true si la base de concimiento esta cargada.
   */
  public boolean isKnowledbaseLoaded() {
    return this.kbm != null;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                          Observer's Methods                             */
  /***************************************************************************/
  
  //---------------------------------------------------------------------------
  // KnowledgeBaseObserver
  //---------------------------------------------------------------------------
  
  /**
   * Añade un nuevo observador del estado de la base de conocimiento.
   * 
   * @param observer observador de la base de concimiento.
   */
  public void addKnowledgeBaseStateObserver(KnowledgeBaseStateObserver observer) {
    // Añadimos el observador.
    this.knowledgeBaseStateObservers.add(observer);
    
    // Le notificamos el estado actual de la base de conocimiento.
    observer.knowledgeBaseStateChanged(isKnowledbaseLoaded());
  }
  
  /**
   * Elimina un observador de la lista de observadores del estado de la base
   * de conocimiento.
   * 
   * @param observer observador a ser elimiando.
   * 
   * @return true en caso de que el observador se haya eliminado.
   */
  public boolean removeKnowledgeBaseObserver(KnowledgeBaseStateObserver observer) {
    return this.knowledgeBaseStateObservers.remove(observer);
  }
  
  /**
   * Notifica el estado actual de la base de conocimiento a los observadores de
   * esta.
   * 
   * @param state estado actual de la base de conocimiento, true en caso de que
   *              este cargada.
   */
  private void notifyKnowledgeBaseObsever(boolean state) {
  
    for (int i = 0; i < knowledgeBaseStateObservers.size(); i++) {
      knowledgeBaseStateObservers.get(i).knowledgeBaseStateChanged(state);
    }
  }

}
