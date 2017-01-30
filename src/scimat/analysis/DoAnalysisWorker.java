/*
 * DoAnalysisWorker.java
 *
 * Created on 04-abr-2011, 11:55:13
 */
package scimat.analysis;

import java.util.ArrayList;
import scimat.api.analysis.network.labeller.AggregatedCouplingBasicClusterLabellerBasedOnMainNode;
import scimat.api.analysis.network.labeller.AggregatedCouplingNodeLabeller;
import scimat.api.analysis.network.labeller.BasicClusterLabellerBasedOnMainNode;
import scimat.api.analysis.network.labeller.BasicNodeLabeller;
import scimat.api.analysis.network.labeller.ClusterLabeller;
import scimat.api.analysis.network.labeller.ClusterSetLabelSetter;
import scimat.api.analysis.network.labeller.CouplingClusterLabellerBasedOnMainNode;
import scimat.api.analysis.network.labeller.CouplingNodeLabeller;
import scimat.api.analysis.network.labeller.NodeLabelSetter;
import scimat.api.analysis.network.labeller.NodeLabeller;
import scimat.api.analysis.network.statistics.CalculateNormalizedRange;
import scimat.api.analysis.network.statistics.CallonCentrality;
import scimat.api.analysis.network.statistics.CallonDensity;
import scimat.api.analysis.network.statistics.ClusterSetNetworkMeasureSetter;
import scimat.api.analysis.performance.ClusterSetAggregationDocumentsMeasureSetter;
import scimat.api.analysis.performance.DocumentAggregationMeasure;
import scimat.api.analysis.performance.WholeNetworkAggregationDocumentsMeasureSetter;
import scimat.api.analysis.performance.docmapper.AggregatedCouplingNodeDocumentMapper;
import scimat.api.analysis.performance.docmapper.BasicNodeDocumentMapper;
import scimat.api.analysis.performance.docmapper.ClusterSetDocumentsSetter;
import scimat.api.analysis.performance.docmapper.CoreDocumentMapper;
import scimat.api.analysis.performance.docmapper.CouplingDocumentMapper;
import scimat.api.analysis.performance.docmapper.CouplingNodeDocumentMapper;
import scimat.api.analysis.performance.docmapper.DocumentMapper;
import scimat.api.analysis.performance.docmapper.IntersectionDocumentMapper;
import scimat.api.analysis.performance.docmapper.KCoreDocumentMapper;
import scimat.api.analysis.performance.docmapper.NodeDocumentMapper;
import scimat.api.analysis.performance.docmapper.SecondaryDocumentMapper;
import scimat.api.analysis.performance.docmapper.UnionDocumentMapper;
import scimat.api.analysis.performance.docmapper.WholeNetworkDocumentsSetter;
import scimat.api.analysis.performance.quality.AverageCitationAggregationMeasure;
import scimat.api.analysis.performance.quality.GIndex;
import scimat.api.analysis.performance.quality.HGIndex;
import scimat.api.analysis.performance.quality.HIndex;
import scimat.api.analysis.performance.quality.MaxCitationAggregationMeasure;
import scimat.api.analysis.performance.quality.MinCitationAggregationMeasure;
import scimat.api.analysis.performance.quality.Q2Index;
import scimat.api.analysis.performance.quality.SumCitationAggregationMeasure;
import scimat.api.analysis.performance.quantity.DocumentCountAggregationMeasure;
import scimat.api.analysis.temporal.EvolutionMap;
import scimat.api.analysis.temporal.EvolutionMapBuilder;
import scimat.api.analysis.temporal.OverlappingMap;
import scimat.api.analysis.temporal.OverlappingMapBuilder;
import scimat.api.analysis.temporal.OverlappingMeasure;
import scimat.api.dataset.AggregatedDataset;
import scimat.api.dataset.Dataset;
import scimat.api.dataset.UndirectNetworkMatrix;
import scimat.api.dataset.datasetbuilder.AggregatedDatasetBuilder;
import scimat.api.dataset.datasetbuilder.AggregatedDatasetByAuthorBuilder;
import scimat.api.dataset.datasetbuilder.AggregatedDatasetByJournalBuilder;
import scimat.api.dataset.datasetbuilder.DatasetBasedOnAuthorReferencesBuilder;
import scimat.api.dataset.datasetbuilder.DatasetBasedOnAuthorsBuilder;
import scimat.api.dataset.datasetbuilder.DatasetBasedOnReferencesBuilder;
import scimat.api.dataset.datasetbuilder.DatasetBasedOnReferencesSourcesBuilder;
import scimat.api.dataset.datasetbuilder.DatasetBasedOnWordsBuilder;
import scimat.api.dataset.datasetbuilder.DatasetBuilder;
import scimat.api.dataset.networkbuilder.NetworkAggregatedCouplingBuilder;
import scimat.api.dataset.networkbuilder.NetworkBuilder;
import scimat.api.dataset.networkbuilder.NetworkCoOccurrenceBuilder;
import scimat.api.dataset.networkbuilder.NetworkCouplingBuilder;
import scimat.api.mapping.Node;
import scimat.api.mapping.WholeNetwork;
import scimat.api.mapping.clustering.AverageLinkClustering;
import scimat.api.mapping.clustering.CentersSimpleGroupingAlgorithm;
import scimat.api.mapping.clustering.ClusteringAlgorithm;
import scimat.api.mapping.clustering.CompleteLinkClustering;
import scimat.api.mapping.clustering.SingleLinkClustering;
import scimat.api.mapping.clustering.SumLinkClustering;
import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.similaritymeasure.CoOccurrenceNormalizer;
import scimat.api.similaritymeasure.CouplingNormalizer;
import scimat.api.similaritymeasure.Normalizer;
import scimat.api.similaritymeasure.direct.AssociationStrengthMeasure;
import scimat.api.similaritymeasure.direct.DirectSimilarityMeasure;
import scimat.api.similaritymeasure.direct.EquivalenceIndexMeasure;
import scimat.api.similaritymeasure.direct.InclusionIndexMeasure;
import scimat.api.similaritymeasure.direct.JaccardIndexMeasure;
import scimat.api.similaritymeasure.direct.SaltonCosineMeasure;
import scimat.api.preprocessing.reduction.data.FilterItemByFrequency;
import scimat.api.preprocessing.reduction.network.FilterByEdgeValue;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.observabletask.ObservableTask;
import scimat.observabletask.TaskStatus;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DoAnalysisWorker extends ObservableTask<GlobalAnalysisResult, Void> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private KnowledgeBaseManager kbm;
  private AnalysisConfiguration analysisConfiguration;

  private int globalStep;
  private int maxGlobalStep;
  private int subStep;
  private int maxSubStep;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   * @param analysisConfiguration
   */
  public DoAnalysisWorker(KnowledgeBaseManager kbm, AnalysisConfiguration analysisConfiguration) {
    
    this.kbm = kbm;
    this.analysisConfiguration = analysisConfiguration;

    setDeterminateMode(true);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @return
   * @throws Exception
   */
  @Override
  protected GlobalAnalysisResult doInBackground() throws Exception {

    int i, periodsCount;
    GlobalAnalysisResult result = new GlobalAnalysisResult(this.analysisConfiguration);
    AnalysisPeriodResult analysisPeriodResult;
    ArrayList<ClusterSet> clusterSets = new ArrayList<ClusterSet>();
    ArrayList<Dataset> datasets = new ArrayList<Dataset>();
    EvolutionMap evolutionMap;
    OverlappingMap overlappingMap;

    try {

      // Indicamos que la tarea ha comenzado.
      setTaskStatus(TaskStatus.STARTED);
      
      periodsCount = this.analysisConfiguration.getPeriods().size();

      this.maxGlobalStep = periodsCount + 1;

      i = 0;
      this.globalStep = 0;

      while ((i < periodsCount) && (! isCancelled())) {

        analysisPeriodResult = doPeriod(i);
        result.addAnalysisPeriodResult(analysisPeriodResult);
        clusterSets.add(analysisPeriodResult.getClusterSet());
        datasets.add(analysisPeriodResult.getOriginalDataset());
        //datasets.add(analysisPeriodResult.getPreprocessedDataset());

        i ++;
        this.globalStep ++;
      }

      subStep = 1;

      evolutionMap = buildEvolutionMap(clusterSets);
      setProgress(calculateProgress(globalStep, maxGlobalStep, subStep, maxSubStep));

      subStep ++;

      overlappingMap = buildOverlappingMap(datasets);      
      setProgress(calculateProgress(globalStep, maxGlobalStep, subStep, maxSubStep));

      result.setLongitudinalResult(new LongitudinalResult(evolutionMap, overlappingMap));

      this.globalStep ++;
      
    } catch (Exception e) {

      setException(e);
      throw e;
    }

    setProgress(100);

    return result;
  }

  /**
   *
   * @return
   * @throws Exception
   */
  private AnalysisPeriodResult doPeriod(int period) throws Exception {
    
    Dataset originalDataset = null;
    Dataset dataset = null;
    UndirectNetworkMatrix network = null;
    ClusterSet clusterSet = null;

    this.maxSubStep = 14;

    subStep = 1;

    System.out.println("Period " + period);
    
    while ((this.subStep <= this.maxSubStep) && (!isCancelled())) {

      switch (this.subStep) {

        case 1:
          System.out.println("  Building the dataset...");
          originalDataset = buildDataSet(period);
          dataset = originalDataset.clone();
          break;

        case 2:
          System.out.println("  Building the aggregated dataset...");
          dataset = buildAggregatedDataset(dataset);
          break;

        case 3:
          System.out.println("  Data reduction...");
          dataReduction(period, dataset);
          break;

        case 4:
          System.out.println("  Building the network...");
          network = buildNetwork(dataset);
          break;

        case 5:
          System.out.println("  Network reduction...");
          networkReduction(period, network);
          break;

        case 6:
          System.out.println("  Normalizing...");
          normalization(dataset, network);
          break;

        case 7:
          System.out.println("  Clustering...");
          clusterSet = clustering(network);
          break;

        case 8:
          System.out.println("  Calculating networks measures...");
          setNetworkMeasures(clusterSet);
          break;

        case 9:
          System.out.println("  Cluster labelling...");
          setClusterLabels(dataset, clusterSet);
          break;

        case 10:
          System.out.println("  Node labelling...");
          setNodeLabels(dataset, clusterSet.getWholeNetwork().getNodes());
          break;

        case 11:
          System.out.println("  Cluster document mapping...");
          clusterDocumentMapper(clusterSet, clusterSet.getWholeNetwork(), dataset);
          break;

        case 12:
          System.out.println("  Node document mapping...");
          nodeDocumentMapper(dataset, clusterSet.getWholeNetwork());
          break;

        case 13:
          System.out.println("  Cluster quality measures...");
          setQualityMeasures(dataset, clusterSet);
          break;

        case 14:
          System.out.println("  Node frequency...");
          setNodeFrequency(dataset, clusterSet.getWholeNetwork());
          break;
      }
      
      setProgress(calculateProgress(globalStep, maxGlobalStep, subStep, maxSubStep));

      subStep ++;
    }

    if (isCancelled()) {

      return null;

    } else {

      return new AnalysisPeriodResult(originalDataset, dataset, network, clusterSet);
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * 
   * @return
   * @throws KnowledgeBaseException
   */
  private Dataset buildDataSet(int period) throws KnowledgeBaseException {
    
    ArrayList<PublishDate> publishDates;
    DatasetBuilder datasetBuilder = null;

    publishDates = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPublishDates(this.analysisConfiguration.getPeriods().get(period).getPeriodID());

    switch (this.analysisConfiguration.getUnitOfAnalysis()) {

      case Authors:
        datasetBuilder = new DatasetBasedOnAuthorsBuilder(kbm);
        break;

      case AuthorsReference:
        datasetBuilder = new DatasetBasedOnAuthorReferencesBuilder(kbm);
        break;

      case References:
        datasetBuilder = new DatasetBasedOnReferencesBuilder(kbm);
        break;

      case ReferenceSources:
        datasetBuilder = new DatasetBasedOnReferencesSourcesBuilder(kbm);
        break;

      case Words:
        datasetBuilder = new DatasetBasedOnWordsBuilder(kbm,
                this.analysisConfiguration.isAuthorWords(),
                this.analysisConfiguration.isSourceWords(),
                this.analysisConfiguration.isExtractedWords());
        break;
    }

    return datasetBuilder.execute(publishDates);
  }
  
  /**
   * 
   * @param dataset
   * @return
   * @throws KnowledgeBaseException 
   */
  private Dataset buildAggregatedDataset(Dataset dataset) throws KnowledgeBaseException {
    
    AggregatedDatasetBuilder builder = null;

    switch (this.analysisConfiguration.getKindOfMatrix()) {
      
      case AggregatedCouplingBasedOnAuthor:
        builder = new AggregatedDatasetByAuthorBuilder(this.kbm);
        break;
      case AggregatedCouplingBasedOnJournal:
        builder = new AggregatedDatasetByJournalBuilder(this.kbm);
        break;
    }

    if (builder == null) {
    
      return dataset;
      
    } else {
    
      return builder.execute(dataset);
      
    }
  }

  /**
   * 
   * @param dataset
   */
  private void dataReduction(int period, Dataset dataset) {

    FilterItemByFrequency frequencyFilter;

    if (this.analysisConfiguration.isFrequencyDataReduction(period)) {

      System.out.println("  Min frequency: " + this.analysisConfiguration.getMinFrequency(period));
      frequencyFilter = new FilterItemByFrequency(this.analysisConfiguration.getMinFrequency(period));

      frequencyFilter.execute(dataset);
    }
  }

  /**
   *
   * @param dataset
   * @return
   */
  private UndirectNetworkMatrix buildNetwork(Dataset dataset) {
    
    NetworkBuilder networkBuilder = null;

    switch (this.analysisConfiguration.getKindOfMatrix()) {

      case CoOccurrence:
        networkBuilder = new NetworkCoOccurrenceBuilder(dataset);
        break;
      case BasicCoupling:
        networkBuilder = new NetworkCouplingBuilder(dataset);
        break;
      case AggregatedCouplingBasedOnAuthor:
        networkBuilder = new NetworkAggregatedCouplingBuilder((AggregatedDataset)dataset);
        break;
      case AggregatedCouplingBasedOnJournal:
        networkBuilder = new NetworkAggregatedCouplingBuilder((AggregatedDataset)dataset);
        break;
    }

    return networkBuilder.execute();
  }

  /**
   * 
   * @param network
   */
  private void networkReduction(int period, UndirectNetworkMatrix network) {

    FilterByEdgeValue coOccurrenceFilter;

    if (this.analysisConfiguration.isFrequencyDataReduction(period)) {

      System.out.println("  Min edge value: " + this.analysisConfiguration.getMinCoOccurrence(period));
      coOccurrenceFilter = new FilterByEdgeValue(this.analysisConfiguration.getMinCoOccurrence(period));

      coOccurrenceFilter.execute(network);
    }
  }

  /**
   * 
   * @param dataset
   * @param network
   */
  private void normalization(Dataset dataset, UndirectNetworkMatrix network) {

    Normalizer normalizer = null;
    DirectSimilarityMeasure measure = null;

    switch (this.analysisConfiguration.getNormalizationMeasure()) {

      case AssociationStrength:
        measure = new AssociationStrengthMeasure();
        break;

      case EquivalenceIndex:
        measure = new EquivalenceIndexMeasure();
        break;

      case InclusionIndex:
        measure = new InclusionIndexMeasure();
        break;

      case JaccardIndex:
        measure = new JaccardIndexMeasure();
        break;

      case SaltonCosine:
        measure = new SaltonCosineMeasure();
        break;
    }

    if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence)) {

      normalizer = new CoOccurrenceNormalizer(measure);

    } else {

      normalizer = new CouplingNormalizer(measure);
    }

    normalizer.execute(dataset, network);
  }

  /**
   * 
   * @param network
   * @return
   */
  private ClusterSet clustering(UndirectNetworkMatrix network) {

    ClusteringAlgorithm clusterAlgorithm = null;

    switch (this.analysisConfiguration.getClusteringAlgorithm()) {

      case CentersSimples:
        clusterAlgorithm = new CentersSimpleGroupingAlgorithm(this.analysisConfiguration.getMinNetworkSize(), this.analysisConfiguration.getMaxNetworkSize());
        break;
        
      case AverageLink:
        clusterAlgorithm = new AverageLinkClustering(this.analysisConfiguration.getMinNetworkSize(), this.analysisConfiguration.getMaxNetworkSize(), this.analysisConfiguration.getCutOff());
        break;
      
      case CompleteLink:
        clusterAlgorithm = new CompleteLinkClustering(this.analysisConfiguration.getMinNetworkSize(), this.analysisConfiguration.getMaxNetworkSize(), this.analysisConfiguration.getCutOff());
        break;
        
      case SingleLink:
        clusterAlgorithm = new SingleLinkClustering(this.analysisConfiguration.getMinNetworkSize(), this.analysisConfiguration.getMaxNetworkSize(), this.analysisConfiguration.getCutOff());
        break;
        
      case SumLink:
        clusterAlgorithm = new SumLinkClustering(this.analysisConfiguration.getMinNetworkSize(), this.analysisConfiguration.getMaxNetworkSize(), this.analysisConfiguration.getCutOff());
        break;
    }

    return clusterAlgorithm.execute(network);
  }

  /**
   * 
   * @param clusterSet
   */
  private void setNetworkMeasures(ClusterSet clusterSet) {

    ClusterSetNetworkMeasureSetter clusterSetNetworkMeasureSetter;
    CalculateNormalizedRange calculateNormalizedRange;

    clusterSetNetworkMeasureSetter = new ClusterSetNetworkMeasureSetter(new CallonCentrality());
    clusterSetNetworkMeasureSetter.execute(clusterSet, KeyProperties.__KEY_CALLON_CENTRALITY);
    
    clusterSetNetworkMeasureSetter = new ClusterSetNetworkMeasureSetter(new CallonDensity());
    clusterSetNetworkMeasureSetter.execute(clusterSet, KeyProperties.__KEY_CALLON_DENSITY);
    
    calculateNormalizedRange = new CalculateNormalizedRange();
    calculateNormalizedRange.calculateMeasures(clusterSet, 
                                               KeyProperties.__KEY_CALLON_CENTRALITY,
                                               KeyProperties.__KEY_CALLON_CENTRALITY_RANGE);
    calculateNormalizedRange.calculateMeasures(clusterSet, 
                                               KeyProperties.__KEY_CALLON_DENSITY,
                                               KeyProperties.__KEY_CALLON_DENSITY_RANGE);
  }

  /**
   * 
   * @param dataset
   * @param clusterSet
   */
  private void setClusterLabels(Dataset dataset, ClusterSet clusterSet) {

    ClusterLabeller clusterLabeller;
    ClusterSetLabelSetter setter;

    if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence)) {

      clusterLabeller = new BasicClusterLabellerBasedOnMainNode(dataset);

    } else if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.BasicCoupling)) {

      clusterLabeller = new CouplingClusterLabellerBasedOnMainNode(dataset);

    } else {

      clusterLabeller = new AggregatedCouplingBasicClusterLabellerBasedOnMainNode((AggregatedDataset)dataset);
    }

    setter = new ClusterSetLabelSetter(clusterLabeller);
    setter.execute(clusterSet, KeyProperties.__KEY_CLUSTER_LABEL);
  }

  /**
   * 
   * @param dataset
   * @param nodes
   */
  private void setNodeLabels(Dataset dataset, ArrayList<Node> nodes) {

    NodeLabeller nodeLabeller;
    NodeLabelSetter setter;

    if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence)) {

      nodeLabeller = new BasicNodeLabeller(dataset);

    } else if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.BasicCoupling)) {

      nodeLabeller = new CouplingNodeLabeller(dataset);

    } else {

      nodeLabeller = new AggregatedCouplingNodeLabeller((AggregatedDataset)dataset);
    }

    setter = new NodeLabelSetter(nodeLabeller);
    setter.execute(nodes, KeyProperties.__KEY_NODE_LABEL);
  }

  /**
   * 
   * @param clusterSet
   * @param wholeNetwork
   * @param dataset 
   */
  private void clusterDocumentMapper(ClusterSet clusterSet, WholeNetwork wholeNetwork, Dataset dataset) {

    DocumentMapper mapper = null;
    ClusterSetDocumentsSetter setter;

    if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence) ||
        this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnAuthor) ||
        this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnJournal)) {

      if (this.analysisConfiguration.isCoreMapper() && (! isCancelled())) {

        mapper = new CoreDocumentMapper(wholeNetwork, dataset);
        setter = new ClusterSetDocumentsSetter(mapper);
        setter.execute(clusterSet, KeyProperties.__KEY_CORE_DOCUMENTS);
      }

      if (this.analysisConfiguration.isSecondaryMapper() && (! isCancelled())) {

        mapper = new SecondaryDocumentMapper(dataset);
        setter = new ClusterSetDocumentsSetter(mapper);
        setter.execute(clusterSet, KeyProperties.__KEY_SECONDARY_DOCUMENTS);
      }

      if (this.analysisConfiguration.iskCoreMapper() && (! isCancelled())) {

        mapper = new KCoreDocumentMapper(this.analysisConfiguration.getkCore(), dataset);
        setter = new ClusterSetDocumentsSetter(mapper);
        setter.execute(clusterSet, KeyProperties.__KEY_KCORE_DOCUMENTS);
      }

      if (this.analysisConfiguration.isIntersectionMapper() && (! isCancelled())) {

        mapper = new IntersectionDocumentMapper(dataset);
        setter = new ClusterSetDocumentsSetter(mapper);
        setter.execute(clusterSet, KeyProperties.__KEY_INTERSECTION_DOCUMENTS);
      }

      if (this.analysisConfiguration.isUnionMapper() && (! isCancelled())) {

        mapper = new UnionDocumentMapper(dataset);
        setter = new ClusterSetDocumentsSetter(mapper);
        setter.execute(clusterSet, KeyProperties.__KEY_UNION_DOCUMENTS);
      }

    } else {

      if (this.analysisConfiguration.isBasicCouplingMapper() && (! isCancelled())) {

        mapper = new CouplingDocumentMapper();
        setter = new ClusterSetDocumentsSetter(mapper);
        setter.execute(clusterSet, KeyProperties.__KEY_COUPLING_DOCUMENTS);
      }
    }
  }

  /**
   * 
   * @param dataset
   * @param network
   */
  private void nodeDocumentMapper(Dataset dataset, WholeNetwork network) {

    NodeDocumentMapper mapper = null;
    WholeNetworkDocumentsSetter setter;

    if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence)) {

      mapper = new BasicNodeDocumentMapper(dataset);
      
    } else if ((this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnAuthor) ||
               this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnJournal)) && 
               (! isCancelled())) {

      mapper = new AggregatedCouplingNodeDocumentMapper((AggregatedDataset)dataset);
      
    } else {

      if (this.analysisConfiguration.isBasicCouplingMapper() && (! isCancelled())) {

        mapper = new CouplingNodeDocumentMapper();
      }
    }
    
    setter = new WholeNetworkDocumentsSetter(mapper);
    setter.execute(network, KeyProperties.__KEY_NODE_DOCUMENTS);
  }

  /**
   * 
   * @param dataset
   * @param clusterSet
   */
  private void setQualityMeasures(Dataset dataset, ClusterSet clusterSet) {

    if (this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence) ||
        this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnAuthor) ||
        this.analysisConfiguration.getKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnJournal)) {

      if (this.analysisConfiguration.isCoreMapper() && (! isCancelled())) {

        setGroupQualityMasures(dataset, clusterSet, KeyProperties.__KEY_CORE_DOCUMENTS);
      }

      if (this.analysisConfiguration.isSecondaryMapper() && (! isCancelled())) {

        setGroupQualityMasures(dataset, clusterSet, KeyProperties.__KEY_SECONDARY_DOCUMENTS);
      }

      if (this.analysisConfiguration.iskCoreMapper() && (! isCancelled())) {

        setGroupQualityMasures(dataset, clusterSet, KeyProperties.__KEY_KCORE_DOCUMENTS);
      }

      if (this.analysisConfiguration.isIntersectionMapper() && (! isCancelled())) {

        setGroupQualityMasures(dataset, clusterSet, KeyProperties.__KEY_INTERSECTION_DOCUMENTS);
      }

      if (this.analysisConfiguration.isUnionMapper() && (! isCancelled())) {

        setGroupQualityMasures(dataset, clusterSet, KeyProperties.__KEY_UNION_DOCUMENTS);
      }

    } else {

      if (this.analysisConfiguration.isBasicCouplingMapper() && (! isCancelled())) {

        setGroupQualityMasures(dataset, clusterSet, KeyProperties.__KEY_COUPLING_DOCUMENTS);
      }
    }
  }

  /**
   *
   * @param dataset
   * @param clusterSet
   * @param propertyKey
   */
  private void setGroupQualityMasures(Dataset dataset, ClusterSet clusterSet, String propertyKey) {

    DocumentAggregationMeasure measure = null;
    ClusterSetAggregationDocumentsMeasureSetter setter;

    measure = new DocumentCountAggregationMeasure();
    setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
    setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_DOCUMENTS_COUNT);

    if (this.analysisConfiguration.isHIndex() && (! isCancelled())) {

      measure = new HIndex(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_HINDEX);
    }
    
    if (this.analysisConfiguration.isGIndex() && (! isCancelled())) {

      measure = new GIndex(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_GINDEX);
    }
    
    if (this.analysisConfiguration.isHgIndex() && (! isCancelled())) {

      measure = new HGIndex(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_HGINDEX);
    }
    
    if (this.analysisConfiguration.isQ2Index() && (! isCancelled())) {

      measure = new Q2Index(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_Q2INDEX);
    }

    if (this.analysisConfiguration.isAverageCitations() && (! isCancelled())) {

      measure = new AverageCitationAggregationMeasure(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_AVERAGE_CITATIONS);
    }

    if (this.analysisConfiguration.isSumCitations() && (! isCancelled())) {

      measure = new SumCitationAggregationMeasure(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_SUM_CITATIONS);
    }

    if (this.analysisConfiguration.isMaxCitations() && (! isCancelled())) {

      measure = new MaxCitationAggregationMeasure(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_MAX_CITATIONS);
    }

    if (this.analysisConfiguration.isMinCitations() && (! isCancelled())) {

      measure = new MinCitationAggregationMeasure(dataset);
      setter = new ClusterSetAggregationDocumentsMeasureSetter(measure);
      setter.execute(clusterSet, propertyKey, propertyKey + KeyProperties.__KEY_MIN_CITATIONS);
    }
  }

  /**
   * 
   * @param dataset
   * @param wholeNetwork
   * @param propertyKey
   */
  private void setNodeFrequency(Dataset dataset, WholeNetwork wholeNetwork) {

    DocumentAggregationMeasure measure;
    WholeNetworkAggregationDocumentsMeasureSetter setter;

    measure = new DocumentCountAggregationMeasure();
    setter = new WholeNetworkAggregationDocumentsMeasureSetter(measure);
    setter.execute(dataset, wholeNetwork, KeyProperties.__KEY_NODE_DOCUMENTS, KeyProperties.__KEY_NODE_FREQUENCY);
  }

  /**
   * 
   * @param clusterSets
   * @return
   */
  private EvolutionMap buildEvolutionMap(ArrayList<ClusterSet> clusterSets) {

    EvolutionMapBuilder builder;
    DirectSimilarityMeasure measure = null;

    switch (this.analysisConfiguration.getEvolutionMapMeasure()) {

      case AssociationStrength:
        measure = new AssociationStrengthMeasure();
        break;

      case EquivalenceIndex:
        measure = new EquivalenceIndexMeasure();
        break;

      case InclusionIndex:
        measure = new InclusionIndexMeasure();
        break;

      case JaccardIndex:
        measure = new JaccardIndexMeasure();
        break;

      case SaltonCosine:
        measure = new SaltonCosineMeasure();
        break;
    }

    builder = new EvolutionMapBuilder(new OverlappingMeasure(measure));

    return builder.buildEvolutionMap(clusterSets);
  }

  /**
   * 
   * @param datasets
   * @return
   */
  private OverlappingMap buildOverlappingMap(ArrayList<Dataset> datasets) {
    
    OverlappingMapBuilder builder;
    DirectSimilarityMeasure measure = null;

    switch (this.analysisConfiguration.getOverlappingMapMeasure()) {

      case AssociationStrength:
        measure = new AssociationStrengthMeasure();
        break;

      case EquivalenceIndex:
        measure = new EquivalenceIndexMeasure();
        break;

      case InclusionIndex:
        measure = new InclusionIndexMeasure();
        break;

      case JaccardIndex:
        measure = new JaccardIndexMeasure();
        break;

      case SaltonCosine:
        measure = new SaltonCosineMeasure();
        break;
    }

    builder = new OverlappingMapBuilder(new OverlappingMeasure(measure));

    return builder.buildOverlappingMap(datasets);
  }

  /**
   * 
   * @param step
   * @param maxStep
   * @param subStep
   * @param maxSubstep
   * @return
   */
  private int calculateProgress(int step, int maxStep, int subStep, int maxSubstep) {

    return (int)(100 * (((double)subStep / maxSubstep) / maxStep) + ((double) step / maxStep * 100));
  }
}
