/*
 * AnalysisConfiguration.java
 *
 * Created on 03-abr-2011, 21:18:37
 */
package scimat.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import scimat.model.knowledgebase.entity.Period;

/**
 *
 * @author mjcobo
 */
public class AnalysisConfiguration implements Serializable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Period> periods;

  private UnitOfAnalysisEnum unitOfAnalysis;
  private boolean authorWords;
  private boolean sourceWords;
  private boolean extractedWords;

  private KindOfMatrixEnum kindOfMatrix;

  private boolean[] frequencyDataReduction;
  private int[] minFrequency;
  private boolean[] coOccurrenceDataReduction;
  private int[] minCoOccurrence;

  private SimilarityMeasuresEnum normalizationMeasure;

  private ClusteringAlgorithmEnum clusteringAlgorithm;
  private int minNetworkSize;
  private int maxNetworkSize;
  private double cutOff;

  private boolean coreMapper;
  private boolean intersectionMapper;
  private boolean kCoreMapper;
  private int kCore;
  private boolean secondaryMapper;
  private boolean unionMapper;
  private boolean basicCouplingMapper;
  
  private boolean hIndex;
  private boolean gIndex;
  private boolean hgIndex;
  private boolean q2Index;
  private boolean averageCitations;
  private boolean sumCitations;
  private boolean minCitations;
  private boolean maxCitations;

  private SimilarityMeasuresEnum evolutionMapMeasure;
  private SimilarityMeasuresEnum overlappingMapMeasure;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param periods
   * @param unitOfAnalysis
   * @param kindOfMatrix
   * @param normalizationMeasures
   * @param clusteringAlgorithm
   * @param evolutionMapMeasure
   * @param overlappingMapMeasure
   */
  public AnalysisConfiguration(ArrayList<Period> periods,
          UnitOfAnalysisEnum unitOfAnalysis, KindOfMatrixEnum kindOfMatrix,
          SimilarityMeasuresEnum normalizationMeasures,
          ClusteringAlgorithmEnum clusteringAlgorithm,
          SimilarityMeasuresEnum evolutionMapMeasure,
          SimilarityMeasuresEnum overlappingMapMeasure) {
    
    this.periods = periods;
    this.unitOfAnalysis = unitOfAnalysis;
    this.authorWords = false;
    this.sourceWords = false;
    this.extractedWords = false;
    this.kindOfMatrix = kindOfMatrix;
    this.frequencyDataReduction = new boolean[periods.size()];
    this.minFrequency = new int[periods.size()];
    this.coOccurrenceDataReduction = new boolean[periods.size()];
    this.minCoOccurrence = new int[periods.size()];
    this.normalizationMeasure = normalizationMeasures;
    this.clusteringAlgorithm = clusteringAlgorithm;
    this.minNetworkSize = -1;
    this.maxNetworkSize = -1;
    this.cutOff = 0.0;
    this.coreMapper = false;
    this.intersectionMapper = false;
    this.kCoreMapper = false;
    this.kCore = -1;
    this.secondaryMapper = false;
    this.unionMapper = false;
    this.basicCouplingMapper = false;
    this.hIndex = false;
    this.gIndex = false;
    this.hgIndex = false;
    this.q2Index = false;
    this.averageCitations = false;
    this.sumCitations = false;
    this.minCitations = false;
    this.maxCitations = false;
    this.evolutionMapMeasure = evolutionMapMeasure;
    this.overlappingMapMeasure = overlappingMapMeasure;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public ArrayList<Period> getPeriods() {
    return periods;
  }

  public UnitOfAnalysisEnum getUnitOfAnalysis() {
    return unitOfAnalysis;
  }

  public boolean isAuthorWords() {
    return authorWords;
  }

  public void setAuthorWords(boolean authorWords) {
    this.authorWords = authorWords;
  }

  public boolean isSourceWords() {
    return sourceWords;
  }

  public void setSourceWords(boolean sourceWords) {
    this.sourceWords = sourceWords;
  }

  public boolean isExtractedWords() {
    return extractedWords;
  }

  public void setExtractedWords(boolean extractedWords) {
    this.extractedWords = extractedWords;
  }

  public KindOfMatrixEnum getKindOfMatrix() {
    return kindOfMatrix;
  }

  public boolean isFrequencyDataReduction(int period) {
    return frequencyDataReduction[period];
  }

  public void setFrequencyDataReduction(int period, boolean frequencyDataReduction) {
    this.frequencyDataReduction[period] = frequencyDataReduction;
  }

  public int getMinFrequency(int period) {
    return minFrequency[period];
  }

  public void setMinFrequency(int period, int minFrequency) {
    this.minFrequency[period] = minFrequency;
  }

  public boolean isCoOccurrenceDataReduction(int period) {
    return coOccurrenceDataReduction[period];
  }

  public void setCoOccurrenceDataReduction(int period, boolean coOccurrenceDataReduction) {
    this.coOccurrenceDataReduction[period] = coOccurrenceDataReduction;
  }

  public int getMinCoOccurrence(int period) {
    return minCoOccurrence[period];
  }

  public void setMinCoOccurrence(int period, int minCoOccurrence) {
    this.minCoOccurrence[period] = minCoOccurrence;
  }

  public SimilarityMeasuresEnum getNormalizationMeasure() {
    return normalizationMeasure;
  }

  public ClusteringAlgorithmEnum getClusteringAlgorithm() {
    return clusteringAlgorithm;
  }

  public int getMinNetworkSize() {
    return minNetworkSize;
  }

  public void setMinNetworkSize(int minNetworkSize) {
    this.minNetworkSize = minNetworkSize;
  }

  public int getMaxNetworkSize() {
    return maxNetworkSize;
  }

  public void setMaxNetworkSize(int maxNetworkSize) {
    this.maxNetworkSize = maxNetworkSize;
  }

  public double getCutOff() {
    return cutOff;
  }

  public void setCutOff(double cutOff) {
    this.cutOff = cutOff;
  }

  public boolean isCoreMapper() {
    return coreMapper;
  }

  public void setCoreMapper(boolean coreMapper) {
    this.coreMapper = coreMapper;
  }

  public boolean isIntersectionMapper() {
    return intersectionMapper;
  }

  public void setIntersectionMapper(boolean intersectionMapper) {
    this.intersectionMapper = intersectionMapper;
  }

  public boolean iskCoreMapper() {
    return kCoreMapper;
  }

  public void setKCoreMapper(boolean kCoreMapper) {
    this.kCoreMapper = kCoreMapper;
  }

  public int getkCore() {
    return kCore;
  }

  public void setKCore(int kCore) {
    this.kCore = kCore;
  }

  public boolean isSecondaryMapper() {
    return secondaryMapper;
  }

  public void setSecondaryMapper(boolean secondaryMapper) {
    this.secondaryMapper = secondaryMapper;
  }

  public boolean isUnionMapper() {
    return unionMapper;
  }

  public void setUnionMapper(boolean unionMapper) {
    this.unionMapper = unionMapper;
  }

  public boolean isBasicCouplingMapper() {
    return basicCouplingMapper;
  }

  public void setBasicCouplingMapper(boolean basicCouplingMapper) {
    this.basicCouplingMapper = basicCouplingMapper;
  }

  public boolean isHIndex() {
    return hIndex;
  }

  public void setHIndex(boolean hIndex) {
    this.hIndex = hIndex;
  }

  public boolean isGIndex() {
    return gIndex;
  }

  public void setGIndex(boolean gIndex) {
    this.gIndex = gIndex;
  }

  public boolean isHgIndex() {
    return hgIndex;
  }

  public void setHgIndex(boolean hgIndex) {
    this.hgIndex = hgIndex;
  }

  public boolean isQ2Index() {
    return q2Index;
  }

  public void setQ2Index(boolean q2Index) {
    this.q2Index = q2Index;
  }

  public boolean isAverageCitations() {
    return averageCitations;
  }

  public void setAverageCitations(boolean averageCitations) {
    this.averageCitations = averageCitations;
  }

  public boolean isSumCitations() {
    return sumCitations;
  }

  public void setSumCitations(boolean sumCitations) {
    this.sumCitations = sumCitations;
  }

  public boolean isMaxCitations() {
    return maxCitations;
  }

  public void setMaxCitations(boolean maxCitations) {
    this.maxCitations = maxCitations;
  }

  public boolean isMinCitations() {
    return minCitations;
  }

  public void setMinCitations(boolean minCitations) {
    this.minCitations = minCitations;
  }

  public SimilarityMeasuresEnum getEvolutionMapMeasure() {
    return evolutionMapMeasure;
  }

  public SimilarityMeasuresEnum getOverlappingMapMeasure() {
    return overlappingMapMeasure;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
