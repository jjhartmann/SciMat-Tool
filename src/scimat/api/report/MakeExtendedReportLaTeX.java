/*
 * MakeExtendedReportHTML.java
 *
 * Created on 02-jun-2011, 18:25:39
 */
package scimat.api.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.lang3.StringEscapeUtils;
import scimat.api.analysis.category.StrategicDiagram;
import scimat.api.analysis.category.StrategicDiagramBuildier;
import scimat.api.mapping.Node;
import scimat.api.mapping.WholeNetwork;
import scimat.api.mapping.clustering.result.Cluster;
import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.utils.image.TranscoderSVGtoPNG;
import scimat.api.utils.xml.DomToString;
import scimat.api.visualization.category.StrategicDiagramSVG;
import scimat.api.visualization.graph.ClusterNetworkSVG;
import scimat.api.visualization.graph.WholeNetworkPajek;
import scimat.api.visualization.temporal.EvolutionMapPajek;
import scimat.api.visualization.temporal.EvolutionMapSVG;
import scimat.api.visualization.temporal.OverlappingMapSVG;
import scimat.analysis.BuildPerformanceMeasuresAvailable;
import scimat.analysis.AnalysisPeriodResult;
import scimat.analysis.BuildDocumentMappersAvailable;
import scimat.analysis.ClusteringAlgorithmEnum;
import scimat.analysis.GlobalAnalysisResult;
import scimat.analysis.KeyProperties;
import scimat.analysis.PerformanceMeasuresAvailable;
import scimat.api.analysis.performance.docmapper.DocumentSet;
import scimat.api.dataset.NetworkPair;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class MakeExtendedReportLaTeX implements ReportGenericBuilder {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * Nombre de la carpeta donde guardaremos los archivos miscelanea
   */
  private static String __MISC_FOLDER_NAME = "misc";

  /**
   * Nombre de la carpeta donde guardaremos las imagenes
   */
  private static String __IMAGES_FOLDER_NAME = "images";

  /**
   * 
   */
  private static int __TEXT_PX = 12;

  /**
   * Ancho en el fichero para el diagrama estrategico
   */
  private static int __STRATEGIC_DIAGRAM_FILE_WIDTH = 800;

  /**
   * Tamaño maximo del radio de la esfera en el diagrama estrategico.
   */
  private static int __STRATEGIC_DIAGRAM_MAX_RADIUS = 60;

  /**
   * Tamaño minimo del radio de la esfera en el diagrama estrategico.
   */
  private static int __STRATEGIC_DIAGRAM_MIN_RADIUS = 20;

  /**
   * Ancho en el fichero para la red tematica
   */
  private static int __CLUSTER_NETWORK_FILE_WIDTH = 800;

  /**
   * Tamaño maximo del radio de la esfera en la red tematica
   */
  private static int __CLUSTER_NETWORK_MAX_RADIUS = 100;

  /**
   * Tamaño minimo del radio de la esfera en la red tematica
   */
  private static int __CLUSTER_NETWORK_MIN_RADIUS = 20;

  /**
   * Extension para los archivos png
   */
  private static String __PNG_EXTENSION = "png";

  /**
   * Extension para los archivos svg
   */
  private static String __SVG_EXTENSION = "svg";

  /**
   * Ruta donde almacenaremos los ficheros
   */
  private String path;

  /**
   * 
   */
  private NumberFormat numberFormatter = new DecimalFormat(",##0.##");
  
  private GlobalAnalysisResult globalExperimentResult;
  
  private ArrayList<PerformanceMeasuresAvailable> performanceMeasuresAvailable;
  
  private DocumentFormatter documentFormatter;
  
  private ArrayList<String> documentMappers;
  
  private DomToString domToString;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param path
   * @param globalExperimentResult
   * @param kbm 
   */
  public MakeExtendedReportLaTeX(String path, GlobalAnalysisResult globalExperimentResult, KnowledgeBaseManager kbm) {
    
    this.path = path;
    this.globalExperimentResult = globalExperimentResult;
    
    this.performanceMeasuresAvailable = new BuildPerformanceMeasuresAvailable().build();
    this.domToString = new DomToString();
    
    this.documentFormatter = new DocumentFormatter(kbm);
    this.documentMappers = new BuildDocumentMappersAvailable().build();
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   */
  public void execute() throws ReportBuilderException {
  
    try {
    
      makeFolderStructure();
      
      doMainLatex();
      
    } catch (Exception e) {
    
      throw new ReportBuilderException(e);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /**
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * @throws TranscoderException 
   */
  private void doMainLatex() throws FileNotFoundException, IOException, TranscoderException, KnowledgeBaseException {
    
    int periodPosition, clusterIndex, clusterOfNode, i, j;
    String escapedPeriodName, svg, clusterName, mapper;
    ArrayList<Period> periods;
    OverlappingMapSVG overlappingMapSVG;
    EvolutionMapSVG evolutionMapSVG;
    EvolutionMapPajek evolutionMapPajek;
    PerformanceMeasuresAvailable measuresAvailable;
    ArrayList<ClusterSet> clusterSets;
    PrintStream out = new PrintStream(getFileAbsolutePathReport());
    double maxFrequency;
    StrategicDiagramBuildier strategicDiagramBuildier;
    StrategicDiagram strategicDiagram;
    StrategicDiagramSVG strategicDiagramSVG;
    WholeNetworkPajek wholeNetworkPajek;
    Cluster cluster;
    ClusterNetworkSVG clusterNetworkSVG;
    AnalysisPeriodResult results;
    ArrayList<NetworkPair> pairs;
    NetworkPair pair;

    out.println("\\documentclass[10pt]{article}");
    out.println("\\usepackage{graphicx}");
    out.println("\\usepackage{float}");
    out.println("\\usepackage{multirow}");
    out.println();
    out.println("\\title{SciMAT LaTeX report}");
    out.println();
    out.println("\\begin{document}");
    out.println("\\maketitle");
    out.println();
    
    out.println("\\section{Analysis Configuration}");
    out.println();
    out.println("\\begin{itemize}");
    out.println();
    out.print("\\item Unit of analysis: ");
    
    switch (this.globalExperimentResult.getAnalysisConfiguration().getUnitOfAnalysis()) {
    
      case Authors: out.println("Authors");
        break;
        
      case Words: out.println("Words (authorRole=" + 
              this.globalExperimentResult.getAnalysisConfiguration().isAuthorWords() + 
              ", sourceRole=" + 
              this.globalExperimentResult.getAnalysisConfiguration().isSourceWords() + 
              ", addedRole=" + this.globalExperimentResult.getAnalysisConfiguration().isExtractedWords() + ")");
        break;
        
      case References: out.println("References");
        break;
        
      case AuthorsReference: out.println("Author-References");
        break;
        
      case ReferenceSources: out.println("Source-References");
        break;
    }
    
    out.println();
    out.print("\\item Kind of network: ");
    
    switch (this.globalExperimentResult.getAnalysisConfiguration().getKindOfMatrix()) {
      
      case CoOccurrence: out.println("Co-occurence");
        break;
        
      case BasicCoupling: out.println("Basic coupling");
        break;
        
      case AggregatedCouplingBasedOnAuthor: out.println("Aggregated coupling based on author");
        break;
        
      case AggregatedCouplingBasedOnJournal: out.println("Aggregated coupling base on journal");
        break;
    }
    
    out.println();
    out.print("\\item Normalization measure: ");
    
    switch (this.globalExperimentResult.getAnalysisConfiguration().getNormalizationMeasure()) {
      
      case AssociationStrength: out.println("Association strength");
        break;
        
      case EquivalenceIndex: out.println("Equivalence index");
        break;
        
      case InclusionIndex: out.println("Inclusion index");
        break;
        
      case JaccardIndex: out.println("Jaccard index");
        break;
      
      case SaltonCosine: out.println("Salton cosine");
        break;
    }
    
    out.println();
    out.print("\\item Cluster algorithm: ");
    
    switch (this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm()) {
    
      case CentersSimples: out.println("Centers simples");
        break;
        
      case SingleLink: out.println("Single link");
        break;
        
      case CompleteLink: out.println("Complete link");
        break;
        
      case AverageLink: out.println("Average link");
        break;
        
      case SumLink: out.println("Sum link");
        break;
    }
    
    out.println();
    out.println("\\begin{itemize}");
    out.println();
    out.println("\\item Max cluster size: " + this.globalExperimentResult.getAnalysisConfiguration().getMaxNetworkSize());
    out.println();
    out.println("\\item Min cluster size: " + this.globalExperimentResult.getAnalysisConfiguration().getMinNetworkSize());
    
    if (this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.SingleLink) ||
        this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.CompleteLink) ||
        this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.AverageLink) ||
        this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.SumLink)) {
    
      out.println();
      out.println("\\item Cutt off: " + this.globalExperimentResult.getAnalysisConfiguration().getCutOff());
    }
    
    out.println();
    out.println("\\end{itemize}");
    
    out.println();
    out.print("\\item Evolution measure: ");
    
    switch (this.globalExperimentResult.getAnalysisConfiguration().getEvolutionMapMeasure()) {
      
      case AssociationStrength: out.println("Association strength");
        break;
        
      case EquivalenceIndex: out.println("Equivalence index");
        break;
        
      case InclusionIndex: out.println("Inclusion index");
        break;
        
      case JaccardIndex: out.println("Jaccard index");
        break;
      
      case SaltonCosine: out.println("Salton cosine");
        break;
    }
    
    out.println();
    out.print("\\item Overlapping measure: ");
    
    switch (this.globalExperimentResult.getAnalysisConfiguration().getOverlappingMapMeasure()) {
      
      case AssociationStrength: out.println("Association strength");
        break;
        
      case EquivalenceIndex: out.println("Equivalence index");
        break;
        
      case InclusionIndex: out.println("Inclusion index");
        break;
        
      case JaccardIndex: out.println("Jaccard index");
        break;
      
      case SaltonCosine: out.println("Salton cosine");
        break;
    }
    
    out.println();
    out.println("\\end{itemize}");
    
    out.println();
    out.println("\\section{Period's results}");
    
    periods = this.globalExperimentResult.getAnalysisConfiguration().getPeriods();
    
    for (periodPosition = 0; periodPosition < periods.size(); periodPosition++) {
      
      results = this.globalExperimentResult.getAnalysisPeriodResult(periodPosition);
      
      escapedPeriodName = StringEscapeUtils.escapeHtml4(periods.get(periodPosition).getName());

      out.println();
      out.println("\\subsection{" + escapedPeriodName + "}");

      out.println();
      out.println("The parameters chosen for this period are:");
      out.println("\\begin{itemize}");
      out.println("\\item Min frequency: " + this.globalExperimentResult.getAnalysisConfiguration().getMinFrequency(periodPosition));
      out.println("\\item Min co-occurrence: " + this.globalExperimentResult.getAnalysisConfiguration().getMinCoOccurrence(periodPosition));
      out.println("\\end{itemize}");

      strategicDiagramBuildier = new StrategicDiagramBuildier(KeyProperties.__KEY_CALLON_CENTRALITY_RANGE,
              KeyProperties.__KEY_CALLON_DENSITY_RANGE,
              KeyProperties.__KEY_CLUSTER_LABEL);

      strategicDiagramSVG = new StrategicDiagramSVG(__STRATEGIC_DIAGRAM_FILE_WIDTH,
              __STRATEGIC_DIAGRAM_MAX_RADIUS,
              __STRATEGIC_DIAGRAM_MIN_RADIUS,
              __TEXT_PX);

      for (i = 0; i < this.performanceMeasuresAvailable.size(); i++) {

        measuresAvailable = this.performanceMeasuresAvailable.get(i);

        strategicDiagram = strategicDiagramBuildier.buildStrategicDiagram(results.getClusterSet(), measuresAvailable.getMapper() + measuresAvailable.getPropertyKey());

        svg = this.domToString.convert(strategicDiagramSVG.buildXML(strategicDiagram));

        svgToFile(getFileAbsolutePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __SVG_EXTENSION), svg);
        TranscoderSVGtoPNG.transcoder(getFileAbsolutePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __SVG_EXTENSION), getFileAbsolutePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __PNG_EXTENSION));
        
        out.println();
        out.println("In Figure \\ref{fig:strategicDiagram-" + periodPosition + "-" + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + "} "
                + "the strategic diagram is shown. The volume of the spheres is proportional to "
                + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + ".");
        out.println();
        out.println("\\begin{figure}[H]");
        out.println("  \\centering");
        out.println("  \\includegraphics[width=\\textwidth]{" + getFilePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __PNG_EXTENSION) + "}");
        out.println("  \\caption{Strategic diagram: " + escapedPeriodName + "-" + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + ".}");
        out.println("  \\label{fig:strategicDiagram-" + periodPosition + "-" + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + "}");
        out.println("\\end{figure}");
      }

      // Building the whole network
      maxFrequency = maxPeriodNodeFrequency(results.getClusterSet().getWholeNetwork());
      wholeNetworkPajek = new WholeNetworkPajek(results.getClusterSet(), KeyProperties.__KEY_NODE_FREQUENCY, KeyProperties.__KEY_NODE_LABEL);
      wholeNetworkPajek.execute(getFileAbsolutePathWholeNetworkPajek(periodPosition), maxFrequency);

      out.println();
      out.println("In Table \\ref{tab:basicMeasures-per" + periodPosition + "} the basic measures of the clusters of the period " + escapedPeriodName + " is shown.");
      out.println();
      out.println("\\begin{table}[H]");
      out.println("  \\centering");
      out.println("  \\begin{small}");
      out.println("  \\begin{tabular}{|l|r|r|r|r|}");
      out.println("  \\hline");
      out.println("  Name & Centrality & Centrality range & Density & Density range \\\\");
      out.println("  \\hline");

      for (i = 0; i < results.getClusterSet().getClustersCount(); i++) {

        cluster = results.getClusterSet().getCluster(i);
        
        out.println(StringEscapeUtils.escapeHtml4(cluster.getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()) + " & " + 
                this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY).getValue()) + " & " +
                this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY_RANGE).getValue()) + " & " +
                this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY).getValue()) + " & " +
                this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY_RANGE).getValue()) + "\\\\");
        out.println("  \\hline");
      }
      
      out.println("  \\end{tabular}");
      out.println("  \\end{small}");
      out.println("  \\caption{Basic measures of the clusters of the period " + escapedPeriodName + "}");
      out.println("  \\label{tab:basicMeasures-per" + periodPosition + "}");
      out.println("\\end{table}");

      out.println();
      out.println("In Table \\ref{tab:performanceMeasures-per" + periodPosition + "} the performance measures of the clusters of the period " + escapedPeriodName + " is shown.");
      out.println();
      
      out.println("\\begin{table}[H]");
      out.println("  \\centering");
      out.println("  \\begin{small}");
      out.println("  \\resizebox{\\linewidth}{!}{");
      out.print("  \\begin{tabular}{|l|");
      
      for (i = 0; i < this.performanceMeasuresAvailable.size(); i++) {
        
        out.print("r|");
      }
      
      out.println("}");
      
      out.println("  \\hline");
      out.print("  \\multirow{2}{*}{Name}");
      
      
      for (i = 0; i < this.performanceMeasuresAvailable.size(); i++) {
        
        measuresAvailable = this.performanceMeasuresAvailable.get(i);
        
        out.print(" & " + measuresAvailable.getMapper());
      }
      
      out.println("\\\\");
      out.println("  \\cline{1-" + i + "}");
      out.print("      ");
      
      for (i = 0; i < this.performanceMeasuresAvailable.size(); i++) {
        
        measuresAvailable = this.performanceMeasuresAvailable.get(i);
        
        out.print(" & " + measuresAvailable.getPropertyKey());
      }
      
      out.println("\\\\");
      out.println("  \\hline");

      for (i = 0; i < results.getClusterSet().getClustersCount(); i++) {

        cluster = results.getClusterSet().getCluster(i);
        
        out.print(StringEscapeUtils.escapeHtml4(cluster.getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()));

        for (j = 0; j < this.performanceMeasuresAvailable.size(); j++) {

          measuresAvailable = this.performanceMeasuresAvailable.get(j);

          out.print(" & " + this.numberFormatter.format(cluster.getProperties().getProperty(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey()).getValue()));
        }

        out.println("\\\\");
      }
      
      out.println("  \\hline");
      out.println("  \\end{tabular}");
      out.println("  }");
      out.println("  \\end{small}");
      out.println("  \\caption{Basic measures of the clusters of the period " + escapedPeriodName + "}");
      out.println("  \\label{tab:performanceMeasures-per" + periodPosition + "}");
      out.println("\\end{table}");
      
      clusterNetworkSVG = new ClusterNetworkSVG(__CLUSTER_NETWORK_FILE_WIDTH,
              __CLUSTER_NETWORK_MAX_RADIUS,
              __CLUSTER_NETWORK_MIN_RADIUS,
              __TEXT_PX);

      for (clusterIndex = 0; clusterIndex < results.getClusterSet().getClustersCount(); clusterIndex++) {

        cluster = results.getClusterSet().getCluster(clusterIndex);

        svg = this.domToString.convert(clusterNetworkSVG.buildXML(cluster,
                results.getClusterSet().getWholeNetwork(),
                KeyProperties.__KEY_NODE_FREQUENCY,
                KeyProperties.__KEY_NODE_LABEL, maxFrequency));

        svgToFile(getFileAbsolutePathClusterNetwork(periodPosition, clusterIndex, __SVG_EXTENSION), svg);
        TranscoderSVGtoPNG.transcoder(getFileAbsolutePathClusterNetwork(periodPosition, clusterIndex, __SVG_EXTENSION), 
                getFileAbsolutePathClusterNetwork(periodPosition, clusterIndex, __PNG_EXTENSION));

        clusterName = StringEscapeUtils.escapeHtml4(cluster.getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString());
        
        out.println();
        out.println("\\subsubsection{Cluster: " + clusterName + "}");

        out.println();
        
        out.println("In Figure \\ref{fig:clusterNetwork-period" + periodPosition + "-" + clusterName + "} the cluster network of " + clusterName + " is shown.");
        out.println();
        out.println("\\begin{figure}[H]");
        out.println("  \\centering");
        out.println("  \\includegraphics[width=\\textwidth]{" + getFilePathClusterNetwork(periodPosition, clusterIndex, __PNG_EXTENSION) + "}");
        out.println("  \\caption{Cluster network - period " + periodPosition + " - " + clusterName + ".}");
        out.println("  \\label{fig:clusterNetwork-period" + periodPosition + "-" + clusterName + "}");
        out.println("\\end{figure}");
        out.println();
        
        out.println("In Table \\ref{tab:internalLink-per" + periodPosition + 
                "-cluster" + clusterIndex + "} the internal links of the cluster " + 
                clusterName + " are shown.");
        
        out.println("\\begin{table}[H]");
        out.println("  \\centering");
        out.println("  \\begin{small}");
        out.println("  \\begin{tabular}{|l|r|r|}");
        out.println("  \\hline");
        out.println("  Node A & Node B & Weight \\\\");
        out.println("  \\hline");
        
        pairs = results.getClusterSet().getClusterInternalPairs(clusterIndex);

        for (j = 0; j < pairs.size(); j++) {

          pair = pairs.get(j);
          out.println(StringEscapeUtils.escapeHtml4(results.getClusterSet().getWholeNetwork().getNode(pair.getID().getElementA()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + " & " +
                  StringEscapeUtils.escapeHtml4(results.getClusterSet().getWholeNetwork().getNode(pair.getID().getElementB()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + " & " +
                  this.numberFormatter.format(pair.getValue()) + " \\\\");
        }

        out.println("  \\hline");
        out.println("  \\end{tabular}");
        out.println("  \\end{small}");
        out.println("  \\caption{Internal links - " + escapedPeriodName + " - " + clusterName + "}");
        out.println("  \\label{tab:internalLink-per" + periodPosition + "-cluster" + clusterIndex + "}");
        out.println("\\end{table}");
        
        out.println();
        out.println("In Table \\ref{tab:externalLink-per" + periodPosition + 
                "-cluster" + clusterIndex + "} the external links of the cluster " + 
                clusterName + " are shown.");
        
        out.println();
        out.println("\\begin{table}[H]");
        out.println("  \\centering");
        out.println("  \\begin{small}");
        out.println("  \\resizebox{\\linewidth}{!}{");
        out.println("  \\begin{tabular}{|l|l|l|l|l|}");
        out.println("  \\hline");
        out.println("  Node A & Cluster node A & Node B & Cluster node B & Weight \\\\");
        out.println("  \\hline");
        
        pairs = results.getClusterSet().getClusterExternalPairs(clusterIndex);

        for (j = 0; j < pairs.size(); j++) {

          pair = pairs.get(j);

          out.print(  StringEscapeUtils.escapeHtml4(results.getClusterSet().getWholeNetwork().getNode(pair.getID().getElementA()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + " & ");

          clusterOfNode = results.getClusterSet().getClusterOfNode(pair.getID().getElementA());

          if (clusterOfNode != -1) {

            out.print(StringEscapeUtils.escapeHtml4(results.getClusterSet().getCluster(clusterOfNode).getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()) + " & ");

          } else {

            out.print(" & ");
          }

          out.print(StringEscapeUtils.escapeHtml4(results.getClusterSet().getWholeNetwork().getNode(pair.getID().getElementB()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + " & ");

          clusterOfNode = results.getClusterSet().getClusterOfNode(pair.getID().getElementB());

          if (clusterOfNode != -1) {

            out.print(StringEscapeUtils.escapeHtml4(results.getClusterSet().getCluster(clusterOfNode).getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()) + " & ");

          } else {

            out.print(" & ");
          }

          out.println(this.numberFormatter.format(pair.getValue()) + "\\\\");
        }
          
        out.println("  \\hline");
        out.println("  \\end{tabular}");
        out.println("  }");
        out.println("  \\end{small}");
        out.println("  \\caption{External links - " + escapedPeriodName + " - " + clusterName + "}");
        out.println("  \\label{tab:externalLink-per" + periodPosition + "-cluster" + clusterIndex + "}");
        out.println("\\end{table}");
        
        ArrayList<Integer> idDocuments;

        for (i = 0; i < this.documentMappers.size(); i++) {

          mapper = this.documentMappers.get(i);
          
          out.println("Documents associated with the cluster (" + mapper + "):");
          out.println();
          
          out.println("\\begin{itemize}");
          out.println();

          idDocuments = ((DocumentSet) results.getClusterSet().getCluster(clusterIndex).getProperties().getProperty(mapper).getValue()).getDocuments();

          for (j = 0; j < idDocuments.size(); j++) {

            out.println("\\item" + this.documentFormatter.format(idDocuments.get(j)));
          }

          out.println("\\end{itemize}");
        }
        
      }
    }
    
    out.println();
    out.println("\\section{Longitudinal results}");
    out.println();
    out.println("\\subsection{Overlapping map}");
    out.println();
    
    overlappingMapSVG = new OverlappingMapSVG(40, 80, 40, 10);
    
    svg = this.domToString.convert(overlappingMapSVG.buildXML(this.globalExperimentResult.getLongitudinalResult().getOverlappingMap()));
    svgToFile(getFileAbsolutePathOverlappingMap(__SVG_EXTENSION), svg);
    TranscoderSVGtoPNG.transcoder(getFileAbsolutePathOverlappingMap(__SVG_EXTENSION), getFileAbsolutePathOverlappingMap(__PNG_EXTENSION));
        
    out.println();
    out.println("In Figure \\ref{fig:overlappingMap} the overlapping map is shown.");
    out.println();
    out.println("\\begin{figure}[H]");
    out.println("  \\centering");
    out.println("  \\includegraphics[width=\\textwidth]{"+ getFilePathOverlappingMap(__PNG_EXTENSION) + "}");
    out.println("  \\caption{Overlapping map.}");
    out.println("  \\label{fig:overlappingMap}");
    out.println("\\end{figure}");
    
    out.println();
    out.println("\\subsection{Evolution map}");
    out.println();
        
    evolutionMapSVG = new EvolutionMapSVG(5, 30, 150, 0, 10);
    
    clusterSets = new ArrayList<ClusterSet>();
    
    for (i = 0; i < this.globalExperimentResult.getAnalysisPeriodResultsCount(); i++) {
    
      clusterSets.add(this.globalExperimentResult.getAnalysisPeriodResult(i).getClusterSet());
    }
    
    evolutionMapPajek = new EvolutionMapPajek(clusterSets, this.globalExperimentResult.getLongitudinalResult().getEvolutionMap());
    
    for (i = 0; i < this.performanceMeasuresAvailable.size(); i++) {
    
      measuresAvailable = this.performanceMeasuresAvailable.get(i);
      
      svg = this.domToString.convert(evolutionMapSVG.buildXML(clusterSets, 
              this.globalExperimentResult.getLongitudinalResult().getEvolutionMap(), 
              measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), 
              KeyProperties.__KEY_CLUSTER_LABEL));
      
      svgToFile(getFileAbsolutePathEvolutionMap(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __SVG_EXTENSION), svg);
      TranscoderSVGtoPNG.transcoder(getFileAbsolutePathEvolutionMap(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __SVG_EXTENSION), getFileAbsolutePathEvolutionMap(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __PNG_EXTENSION));
      
      out.println();
      out.println("In Figure \\ref{fig:evolutionMap-" + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + "} "
              + "the evolution map is shown. The volume of the spheres is proportional to -" + 
              measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + ".");
      out.println();
      out.println("\\begin{figure}[H]");
      out.println("  \\centering");
      out.println("  \\includegraphics[width=\\textwidth]{"+ getFilePathEvolutiongMap(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __PNG_EXTENSION) + "}");
      out.println("  \\caption{Evolution map: " + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + ".}");
      out.println("  \\label{fig:evolutionMap-" + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + "}");
      out.println("\\end{figure}");
      out.println();
      
      evolutionMapPajek.execute(getFileAbsolutePathEvolutionMapPajek(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey()),
              measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), 
              KeyProperties.__KEY_CLUSTER_LABEL);
    }
    
    out.println("\\end{document}");
    
    out.close();
  }
  
  /**
   * 
   * @param wholeNetwork
   * @return 
   */
  public double maxPeriodNodeFrequency(WholeNetwork wholeNetwork) {
  
    int i;
    double max, tmp;
    ArrayList<Node> nodes;
    
    max = 0.0;

    nodes = wholeNetwork.getNodes();

    for (i = 0; i < nodes.size(); i++) {

      tmp = (Double) nodes.get(i).getProperties().getProperty(KeyProperties.__KEY_NODE_FREQUENCY).getValue();

      if (tmp > max) {

        max = tmp;
      }
    }
    
    return max;
  }
  
  /**
   * 
   * @param path
   * @param document
   * @throws FileNotFoundException 
   */
  private void svgToFile(String path, String document) throws FileNotFoundException{
  
    PrintStream out = new PrintStream(path);
    
    out.println(document);
    
    out.close();
  }
  
  /**
   * 
   * @return
   */
  private boolean makeFolderStructure() {

    boolean successful = false;

    File file;

    file = new File(path + File.separator + __IMAGES_FOLDER_NAME);

    if (file.mkdirs()) {

      file = new File(path + File.separator + __MISC_FOLDER_NAME);
      
      file.mkdirs();
    }

    return successful;
  }
  
  /**
   * 
   * @param period
   * @return
   */
  private String getFileAbsolutePathPeriodHTMLPage(int period) {

    return path + File.separator + getFilePathPeriodHTMLPage(period);
  }

  /**
   *
   * @param period
   * @return
   */
  private String getFilePathPeriodHTMLPage(int period) {

    return "period" + period + ".html";
  }
  
  /**
   *
   * @return
   */
  private String getFileAbsolutePathReport() {

    return path + File.separator + getFilePathMainHTMLPage();
  }

  /**
   *
   * @return
   */
  private String getFilePathMainHTMLPage() {

    return "SciMAt-report.tex";
  }
  
  /**
   *
   * @return
   */
  private String getFileAbsolutePathStrategicDiagram(int period, String subName, String extension) {

    return path + File.separator + __IMAGES_FOLDER_NAME + File.separator +
      "strategicDiagram-period" + String.valueOf(period) + "-" + subName + "." + extension;
  }

  /**
   *
   * @return
   */
  private String getFilePathStrategicDiagram(int period, String subName, String extension) {

    return __IMAGES_FOLDER_NAME + "/" + "strategicDiagram-period" + String.valueOf(period) + "-" + subName + "." + extension;
  }

  /**
   *
   * @param period
   * @param theme
   * @param extension
   * @return
   */
  private String getFileAbsolutePathClusterNetwork(int period, int theme,
    String extension) {

    return path + File.separator + __IMAGES_FOLDER_NAME + File.separator +
      "clusterNetwork-period" + String.valueOf(period) + "-cluster" +
      String.valueOf(theme + 1) + "." + extension;
  }

  /**
   * 
   * @param period
   * @param theme
   * @param extension
   * @return
   */
  private String getFilePathClusterNetwork(int period, int theme,
    String extension) {

    return __IMAGES_FOLDER_NAME + "/" +
      "clusterNetwork-period" + String.valueOf(period) + "-cluster" +
      String.valueOf(theme + 1) + "." + extension;
  }
  
  /**
   * 
   * @param extension
   * @return 
   */
  private String getFileAbsolutePathOverlappingMap(String extension) {

    return path + File.separator + __IMAGES_FOLDER_NAME + File.separator +
      "overlappingMap" + "." + extension;
  }

  /**
   * 
   * @param extension
   * @return 
   */
  private String getFilePathOverlappingMap(String extension) {

    return __IMAGES_FOLDER_NAME + "/" + "overlappingMap" + "." + extension;
  }
  
  /**
   * 
   * @param subname
   * @param extension
   * @return 
   */
  private String getFileAbsolutePathEvolutionMap(String subname, String extension) {

    return path + File.separator + __IMAGES_FOLDER_NAME + File.separator +
      "evolutionMap-" + subname + "." + extension;
  }

  /**
   * 
   * @param subname
   * @param extension
   * @return 
   */
  private String getFilePathEvolutiongMap(String subname, String extension) {

    return __IMAGES_FOLDER_NAME + "/" + "evolutionMap-" + subname + "." + extension;
  }
  
  /**
   * 
   * @param subname
   * @param extension
   * @return 
   */
  private String getFileAbsolutePathEvolutionMapPajek(String subname) {

    return path + File.separator + __MISC_FOLDER_NAME + File.separator +
      "evolutionMap-" + subname + ".net";
  }

  /**
   * 
   * @param subname
   * @param extension
   * @return 
   */
  private String getFilePathEvolutiongMapPajek(String subname) {

    return __MISC_FOLDER_NAME + "/" + "evolutionMap-" + subname + ".net";
  }
  
  /**
   *
   * @return
   */
  private String getFileAbsolutePathWholeNetworkPajek(int period) {

    return path + File.separator + __MISC_FOLDER_NAME + File.separator +
      "wholeNetwork-period" + String.valueOf(period) + ".net";
  }

  /**
   *
   * @return
   */
  private String getFilePathWholeNetworkPajek(int period) {

    return __MISC_FOLDER_NAME + "/" + "wholeNetwork-period" + String.valueOf(period) + ".net";
  }
  
}
