/*
 * ResultsByPeriodPanel.java
 *
 * Created on 05-abr-2011, 13:04:16
 */
package scimat.gui.components.analysisview;

import java.util.ArrayList;
import scimat.api.analysis.category.StrategicDiagram;
import scimat.api.analysis.category.StrategicDiagramBuildier;
import scimat.api.mapping.Node;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.utils.property.PropertySet;
import scimat.analysis.BuildPerformanceMeasuresAvailable;
import scimat.analysis.CurrentAnalysis;
import scimat.analysis.AnalysisConfiguration;
import scimat.analysis.KeyProperties;
import scimat.analysis.KindOfMatrixEnum;
import scimat.analysis.PerformanceMeasuresAvailable;
import scimat.analysis.PropertyDocumentSetItem;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.observer.SelectionObserver;
import scimat.gui.components.tablemodel.ClusterPropertiesTableModel;
import scimat.gui.components.tablemodel.ClusterTableModel;
import scimat.gui.components.tablemodel.NodeTableModel;
import scimat.gui.components.tablemodel.PerformanceMeasuresAvailableTableModel;
import scimat.gui.components.tablemodel.PeriodsInAnalysisTableModel;
import scimat.model.knowledgebase.entity.Period;

/**
 *
 * @author mjcobo
 */
public class ResultsByPeriodPanel extends javax.swing.JPanel {

  /** Creates new form ResultsByPeriodPanel */
  public ResultsByPeriodPanel() {

    this.periodsListPanel = new GenericItemsListPanel<Period>(new PeriodsInAnalysisTableModel());
    this.clustersListPanel = new GenericItemsListPanel<Cluster>(new ClusterTableModel());
    this.clusterPropertiesListPanel = new GenericItemsListPanel<PropertyDocumentSetItem>(new ClusterPropertiesTableModel());
    this.performanceMeasuresAvailableListPanel = new GenericItemsListPanel<PerformanceMeasuresAvailable>(new PerformanceMeasuresAvailableTableModel());
    this.nodeListPanel = new GenericItemsListPanel<Node>(new NodeTableModel());

    initComponents();

    this.periodsPanel.add(this.periodsListPanel);
    this.clustersPanel.add(this.clustersListPanel);
    this.clusterPropertiesPanel.add(this.clusterPropertiesListPanel);
    this.performanceMeasuresPanel.add(this.performanceMeasuresAvailableListPanel);
    this.nodesPanel.add(this.nodeListPanel);

    this.periodsListPanel.addSelectionObserver(new PeriodsListPanelObserver());
    this.clustersListPanel.addSelectionObserver(new ClusterListPanelObserver());
    this.performanceMeasuresAvailableListPanel.addSelectionObserver(new PerformanceMeasuresAvailableObserver());
  }

  /**
   *
   */
  public void refresh() {

    this.periodsListPanel.refreshItems(CurrentAnalysis.getInstance().getResults().getAnalysisConfiguration().getPeriods());
    this.clustersListPanel.refreshItems(new ArrayList<Cluster>());
    this.clusterPropertiesListPanel.refreshItems(new ArrayList<PropertyDocumentSetItem>());
    this.performanceMeasuresAvailableListPanel.refreshItems(this.buildPerformanceMeasuresAvailable.build());
    this.strategicDiagramPanel.refreshItems(new StrategicDiagram());
    this.clusterNetworkPanel.refresh();
    this.selectedMeasure = null;
  }
  
  private void refreshStrategicDiagram() {
  
    if (selectedClusterSet != null) {

      if (selectedMeasure != null) {

        strategicDiagramPanel.refreshItems(strategicDiagramBuildier.buildStrategicDiagram(selectedClusterSet,
                selectedMeasure.getMapper() + selectedMeasure.getPropertyKey()));

      } else {

        strategicDiagramPanel.refreshItems(strategicDiagramBuildier.buildStrategicDiagram(selectedClusterSet, null));
      }
      
    } /*else {

      strategicDiagramPanel.refreshItems(new StrategicDiagram());
    }*/
  }

  /**
   * 
   */
  private class PeriodsListPanelObserver implements SelectionObserver {

    public PeriodsListPanelObserver() {

      strategicDiagramBuildier = new StrategicDiagramBuildier(KeyProperties.__KEY_CALLON_CENTRALITY_RANGE,
              KeyProperties.__KEY_CALLON_DENSITY_RANGE,
              KeyProperties.__KEY_CLUSTER_LABEL);
    }

    public void selectionChangeHappened(int[] selection) {

      int i;
      double tmp;
      ArrayList<Node> nodes;

      if (selection.length == 1) {

        clustersListPanel.refreshItems(CurrentAnalysis.getInstance().getResults().getAnalysisPeriodResult(selection[0]).getClusterSet().getClusters());
        clusterPropertiesListPanel.refreshItems(new ArrayList<PropertyDocumentSetItem>());

        selectedClusterSet = CurrentAnalysis.getInstance().getResults().getAnalysisPeriodResult(selection[0]).getClusterSet();

        refreshStrategicDiagram();
        
        /*if (selectedMeasure != null) {
        
          strategicDiagramPanel.refreshItems(strategicDiagramBuildier.buildStrategicDiagram(selectedClusterSet, 
                selectedMeasure.getMapper() + selectedMeasure.getPropertyKey()));
          
        } else {
        
          strategicDiagramPanel.refreshItems(strategicDiagramBuildier.buildStrategicDiagram(selectedClusterSet, null));
        }*/

        networkMaxSize = 0.0;

        nodes = selectedClusterSet.getWholeNetwork().getNodes();
        
        for (i = 0; i < nodes.size(); i++) {

          tmp = (Double) nodes.get(i).getProperties().getProperty(KeyProperties.__KEY_NODE_FREQUENCY).getValue();

          if (tmp > networkMaxSize) {

            networkMaxSize = tmp;
          }
        }

      } else {

        clustersListPanel.refreshItems(new ArrayList<Cluster>());
        clusterPropertiesListPanel.refreshItems(new ArrayList<PropertyDocumentSetItem>());
        strategicDiagramPanel.refreshItems(new StrategicDiagram());
      }
    }
  }

  /**
   *
   */
  private class ClusterListPanelObserver implements SelectionObserver {

    /**
     *
     * @param selection
     */
    public void selectionChangeHappened(int[] selection) {

      int i;
      ArrayList<Node> nodes;
      ArrayList<Integer> nodesIDs;
      
      if (selection.length == 1) {
        
        clusterPropertiesListPanel.refreshItems(buildClusterProperties(clustersListPanel.getItem(selection[0])));
        clusterNetworkPanel.refreshData(clustersListPanel.getItem(selection[0]), selectedClusterSet.getWholeNetwork(), networkMaxSize);
        
        nodes = new ArrayList<Node>();
        nodesIDs = clustersListPanel.getItem(selection[0]).getNodes();
        
        for (i = 0; i < nodesIDs.size(); i++) {
        
          nodes.add(selectedClusterSet.getWholeNetwork().getNode(nodesIDs.get(i)));
        }
        
        nodeListPanel.refreshItems(nodes);

      } else {

        clusterPropertiesListPanel.refreshItems(new ArrayList<PropertyDocumentSetItem>());
      }
    }

    /**
     *
     * @return
     */
    private ArrayList<PropertyDocumentSetItem> buildClusterProperties(Cluster cluster) {

      ArrayList<PropertyDocumentSetItem> properties = new ArrayList<PropertyDocumentSetItem>();
      AnalysisConfiguration conf = CurrentAnalysis.getInstance().getResults().getAnalysisConfiguration();

      if (conf.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence) ||
          conf.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnAuthor) ||
          conf.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnJournal)) {

        if (conf.isCoreMapper()) {

          addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_CORE_DOCUMENTS, cluster.getProperties());
        }

        if (conf.isSecondaryMapper()) {

          addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_SECONDARY_DOCUMENTS, cluster.getProperties());
        }

        if (conf.iskCoreMapper()) {

          addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_KCORE_DOCUMENTS, cluster.getProperties());
        }

        if (conf.isIntersectionMapper()) {

          addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_INTERSECTION_DOCUMENTS, cluster.getProperties());
        }

        if (conf.isUnionMapper()) {

          addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_UNION_DOCUMENTS, cluster.getProperties());
        }

      } else {

        if (conf.isBasicCouplingMapper()) {

          addDocumentMapperProperties(conf, properties, KeyProperties.__KEY_COUPLING_DOCUMENTS, cluster.getProperties());
        }
      }

      return properties;
    }

    /**
     * 
     * @param conf
     * @param properties
     * @param mapper
     * @param propertySet
     */
    private void addDocumentMapperProperties(AnalysisConfiguration conf,
            ArrayList<PropertyDocumentSetItem> properties, String mapper, PropertySet propertySet) {

      properties.add(new PropertyDocumentSetItem(mapper,
              KeyProperties.__KEY_DOCUMENTS_COUNT,
              (Double)propertySet.getProperty(mapper + KeyProperties.__KEY_DOCUMENTS_COUNT).getValue()));

      if (conf.isHIndex()) {

        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_HINDEX,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_HINDEX).getValue()));
      }
      
      if (conf.isGIndex()) {

        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_GINDEX,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_GINDEX).getValue()));
      }
      
      if (conf.isHgIndex()) {

        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_HGINDEX,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_HGINDEX).getValue()));
      }
      
      if (conf.isQ2Index()) {

        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_Q2INDEX,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_Q2INDEX).getValue()));
      }

      if (conf.isAverageCitations()) {


        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_AVERAGE_CITATIONS,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_AVERAGE_CITATIONS).getValue()));
      }

      if (conf.isSumCitations()) {


        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_SUM_CITATIONS,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_SUM_CITATIONS).getValue()));
      }

      if (conf.isMaxCitations()) {


        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_MAX_CITATIONS,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_MAX_CITATIONS).getValue()));
      }

      if (conf.isMinCitations()) {


        properties.add(new PropertyDocumentSetItem(mapper,
                KeyProperties.__KEY_MIN_CITATIONS,
                (Double) propertySet.getProperty(mapper + KeyProperties.__KEY_MIN_CITATIONS).getValue()));
      }
    }
  }
  
  private class PerformanceMeasuresAvailableObserver implements SelectionObserver {

    public void selectionChangeHappened(int[] selection) {
      
      if (selection.length == 1) {
      
        selectedMeasure = performanceMeasuresAvailableListPanel.getItem(selection[0]);
        refreshStrategicDiagram();
        
      } else {
      
        selectedMeasure = null;

      }
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    strategicDiagramHolderPanel = new javax.swing.JPanel();
    strategicDiagramPanel = new scimat.gui.components.analysisview.StrategicDiagramPanel();
    clusterNetworkPanel = new scimat.gui.components.analysisview.ClusterNetworkPanel();
    clusterPropertiesPanel = new javax.swing.JPanel();
    clustersPanel = new javax.swing.JPanel();
    periodsPanel = new javax.swing.JPanel();
    performanceMeasuresPanel = new javax.swing.JPanel();
    nodesPanel = new javax.swing.JPanel();

    strategicDiagramHolderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Strategic diagram"));

    javax.swing.GroupLayout strategicDiagramHolderPanelLayout = new javax.swing.GroupLayout(strategicDiagramHolderPanel);
    strategicDiagramHolderPanel.setLayout(strategicDiagramHolderPanelLayout);
    strategicDiagramHolderPanelLayout.setHorizontalGroup(
      strategicDiagramHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(strategicDiagramPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
    );
    strategicDiagramHolderPanelLayout.setVerticalGroup(
      strategicDiagramHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(strategicDiagramPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
    );

    clusterNetworkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cluster's network"));

    clusterPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cluster's properties"));
    clusterPropertiesPanel.setLayout(new javax.swing.BoxLayout(clusterPropertiesPanel, javax.swing.BoxLayout.LINE_AXIS));

    clustersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Clusters"));
    clustersPanel.setLayout(new javax.swing.BoxLayout(clustersPanel, javax.swing.BoxLayout.LINE_AXIS));

    periodsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Periods"));
    periodsPanel.setLayout(new javax.swing.BoxLayout(periodsPanel, javax.swing.BoxLayout.LINE_AXIS));

    performanceMeasuresPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Performance measures"));
    performanceMeasuresPanel.setLayout(new javax.swing.BoxLayout(performanceMeasuresPanel, javax.swing.BoxLayout.LINE_AXIS));

    nodesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nodes"));
    nodesPanel.setLayout(new javax.swing.BoxLayout(nodesPanel, javax.swing.BoxLayout.LINE_AXIS));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(periodsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
          .addComponent(nodesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
          .addComponent(performanceMeasuresPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(clustersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
          .addComponent(strategicDiagramHolderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(clusterPropertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
          .addComponent(clusterNetworkPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(clustersPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
          .addComponent(clusterPropertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
          .addComponent(periodsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(clusterNetworkPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
          .addComponent(strategicDiagramHolderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(performanceMeasuresPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(nodesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))))
    );
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private scimat.gui.components.analysisview.ClusterNetworkPanel clusterNetworkPanel;
  private javax.swing.JPanel clusterPropertiesPanel;
  private javax.swing.JPanel clustersPanel;
  private javax.swing.JPanel nodesPanel;
  private javax.swing.JPanel performanceMeasuresPanel;
  private javax.swing.JPanel periodsPanel;
  private javax.swing.JPanel strategicDiagramHolderPanel;
  private scimat.gui.components.analysisview.StrategicDiagramPanel strategicDiagramPanel;
  // End of variables declaration//GEN-END:variables
  private GenericItemsListPanel<Period> periodsListPanel;
  private GenericItemsListPanel<Cluster> clustersListPanel;
  private GenericItemsListPanel<PropertyDocumentSetItem> clusterPropertiesListPanel;
  private GenericItemsListPanel<PerformanceMeasuresAvailable> performanceMeasuresAvailableListPanel;
  private GenericItemsListPanel<Node> nodeListPanel;
  private double networkMaxSize = 0.0;
  private ClusterSet selectedClusterSet;
  private BuildPerformanceMeasuresAvailable buildPerformanceMeasuresAvailable = new BuildPerformanceMeasuresAvailable();
  private PerformanceMeasuresAvailable selectedMeasure = null;
  private StrategicDiagramBuildier strategicDiagramBuildier;
}
