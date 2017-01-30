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
import java.util.Collections;
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
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class MakeExtendedReportHTML implements ReportGenericBuilder {

    /**
     * ************************************************************************
     */
    /*                        Private attributes                               */
    /**
     * ************************************************************************
     */
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
     * Ancho grande para los diagramas estrategicos
     */
    private static int __STRATEGIC_DIAGRAM_BIG_WIDTH = 800;

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
     * Ancho grande para las redes tematicas
     */
    private static int __CLUSTER_NETWORK_BIG_WIDTH = 800;

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

    private DomToString domToString;

    private DocumentFormatter documentFormatter;

    private ArrayList<String> documentMappers;
    
    private KnowledgeBaseManager kbm;            

    /**
     * ************************************************************************
     */
    /*                            Constructors                                 */
    /**
     * ************************************************************************
     */
    /**
     *
     * @param path
     * @param globalExperimentResult
     */
    public MakeExtendedReportHTML(String path, GlobalAnalysisResult globalExperimentResult, KnowledgeBaseManager kbm) {

        this.path = path;
        this.kbm = kbm;
        this.globalExperimentResult = globalExperimentResult;

        this.performanceMeasuresAvailable = new BuildPerformanceMeasuresAvailable().build();
        this.domToString = new DomToString();
        this.documentFormatter = new DocumentFormatter(kbm);
        this.documentMappers = new BuildDocumentMappersAvailable().build();
    }

    /**
     * ************************************************************************
     */
    /*                           Public Methods                                */
    /**
     * ************************************************************************
     */
    /**
     *
     * @throws ReportBuilderException
     */
    public void execute() throws ReportBuilderException {

        try {

            makeFolderStructure();

            doMainHTMLPage();

        } catch (Exception e) {

            e.printStackTrace(System.err);

            throw new ReportBuilderException(e);
        }
    }

    /**
     * ************************************************************************
     */
    /*                           Private Methods                               */
    /**
     * ************************************************************************
     */
    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws TranscoderException
     */
    private void doMainHTMLPage() throws FileNotFoundException, IOException, TranscoderException, KnowledgeBaseException {

        int i;
        String escapedPeriodName, svg;
        ArrayList<Period> periods;
        OverlappingMapSVG overlappingMapSVG;
        EvolutionMapSVG evolutionMapSVG;
        EvolutionMapPajek evolutionMapPajek;
        PerformanceMeasuresAvailable measuresAvailable;
        ArrayList<ClusterSet> clusterSets;
        PrintStream out = new PrintStream(getFileAbsolutePathMainHTMLPage());

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"es\">");
        out.println("  <head>");
        out.println("    <title>SciMAT report</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>SciMAT HTML report</h1>");
        out.println("    <p>This resport has been automatically generated by SciMAT</p>");
        out.println("    <hr/>");
        out.println("    <h2>Analysis Configuration</h2>");
        out.println("    <ul>");
        out.print("      <li>Unit of analysis: ");

        switch (this.globalExperimentResult.getAnalysisConfiguration().getUnitOfAnalysis()) {

            case Authors:
                out.print("Authors");
                break;

            case Words:
                out.print("Words (authorRole="
                        + this.globalExperimentResult.getAnalysisConfiguration().isAuthorWords()
                        + ", sourceRole="
                        + this.globalExperimentResult.getAnalysisConfiguration().isSourceWords()
                        + ", addedRole=" + this.globalExperimentResult.getAnalysisConfiguration().isExtractedWords() + ")");
                break;

            case References:
                out.print("References");
                break;

            case AuthorsReference:
                out.print("Author-References");
                break;

            case ReferenceSources:
                out.print("Source-References");
                break;
        }

        out.println("</li>");

        out.print("      <li>Kind of network: ");

        switch (this.globalExperimentResult.getAnalysisConfiguration().getKindOfMatrix()) {

            case CoOccurrence:
                out.print("Co-occurence");
                break;

            case BasicCoupling:
                out.print("Basic coupling");
                break;

            case AggregatedCouplingBasedOnAuthor:
                out.print("Aggregated coupling based on author");
                break;

            case AggregatedCouplingBasedOnJournal:
                out.print("Aggregated coupling base on journal");
                break;
        }

        out.println("</li>");

        out.print("      <li>Normalization measure: ");

        switch (this.globalExperimentResult.getAnalysisConfiguration().getNormalizationMeasure()) {

            case AssociationStrength:
                out.print("Association strength");
                break;

            case EquivalenceIndex:
                out.print("Equivalence index");
                break;

            case InclusionIndex:
                out.print("Inclusion index");
                break;

            case JaccardIndex:
                out.print("Jaccard index");
                break;

            case SaltonCosine:
                out.print("Salton cosine");
                break;
        }

        out.println("</li>");

        out.print("      <li>Cluster algorithm: ");

        switch (this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm()) {

            case CentersSimples:
                out.println("Centers simples");
                break;

            case SingleLink:
                out.println("Single link");
                break;

            case CompleteLink:
                out.println("Complete link");
                break;

            case AverageLink:
                out.println("Average link");
                break;

            case SumLink:
                out.println("Sum link");
                break;
        }

        out.println("        <ul>");
        out.println("          <li>Max cluster size: " + this.globalExperimentResult.getAnalysisConfiguration().getMaxNetworkSize() + "</li>");
        out.println("          <li>Min cluster size: " + this.globalExperimentResult.getAnalysisConfiguration().getMinNetworkSize() + "</li>");

        if (this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.SingleLink)
                || this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.CompleteLink)
                || this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.AverageLink)
                || this.globalExperimentResult.getAnalysisConfiguration().getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.SumLink)) {

            out.println("          <li>Cutt off: " + this.globalExperimentResult.getAnalysisConfiguration().getCutOff() + "</li>");
        }

        out.println("        </ul>");

        out.println("      </li>");

        out.print("      <li>Evolution measure: ");

        switch (this.globalExperimentResult.getAnalysisConfiguration().getEvolutionMapMeasure()) {

            case AssociationStrength:
                out.print("Association strength");
                break;

            case EquivalenceIndex:
                out.print("Equivalence index");
                break;

            case InclusionIndex:
                out.print("Inclusion index");
                break;

            case JaccardIndex:
                out.print("Jaccard index");
                break;

            case SaltonCosine:
                out.print("Salton cosine");
                break;
        }

        out.println("</li>");

        out.print("      <li>Overlapping measure: ");

        switch (this.globalExperimentResult.getAnalysisConfiguration().getOverlappingMapMeasure()) {

            case AssociationStrength:
                out.print("Association strength");
                break;

            case EquivalenceIndex:
                out.print("Equivalence index");
                break;

            case InclusionIndex:
                out.print("Inclusion index");
                break;

            case JaccardIndex:
                out.print("Jaccard index");
                break;

            case SaltonCosine:
                out.print("Salton cosine");
                break;
        }

        out.println("</li>");

        out.println("    </ul>");

        out.println("    <h2>Period's results</h2>");
        out.println("    <ul>");

        periods = this.globalExperimentResult.getAnalysisConfiguration().getPeriods();

        for (i = 0; i < periods.size(); i++) {

            escapedPeriodName = StringEscapeUtils.escapeHtml4(periods.get(i).getName());

            out.println("      <li><a href=\"" + getFilePathPeriodHTMLPage(i) + "\">" + escapedPeriodName + "</a></li>");

            doPeriodHTLMPage(escapedPeriodName, i, this.globalExperimentResult.getAnalysisPeriodResult(i));
        }

        periods.clear();

        out.println("    </ul>");
        out.println("    <h2>Longidtudinal results</h2>");
        out.println("    <h3>Overlapping map</h3>");

        overlappingMapSVG = new OverlappingMapSVG(40, 80, 40, 10);

        svg = this.domToString.convert(overlappingMapSVG.buildXML(this.globalExperimentResult.getLongitudinalResult().getOverlappingMap()));
        svgToFile(getFileAbsolutePathOverlappingMap(__SVG_EXTENSION), svg);
        TranscoderSVGtoPNG.transcoder(getFileAbsolutePathOverlappingMap(__SVG_EXTENSION), getFileAbsolutePathOverlappingMap(__PNG_EXTENSION));

        out.println("      <img alt=\"Overlapping map\" src=\"" + getFilePathOverlappingMap(__PNG_EXTENSION) + "\"/>");
        out.println("    <h3>Evolution map</h3>");

        evolutionMapSVG = new EvolutionMapSVG(5, 30, 150, 0, 10);

        out.println("      <table border=\"1\" summary=\"this table shows the evolution maps\">");
        out.println("        <caption><em>Evolution maps</em></caption>");
        out.println("        <tr>");
        out.println("          <td>Performance measure</td>");
        out.println("          <td>Evolution map</td>");
        out.println("          <td>Pajek file</td>");
        out.println("        </tr>");

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

            out.println("        <tr>");
            out.println("          <td>" + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + "</td>");
            out.println("          <td><img alt=\"Evolution map\" src=\""
                    + getFilePathEvolutiongMap(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __PNG_EXTENSION) + "\"/></td>");

            evolutionMapPajek.execute(getFileAbsolutePathEvolutionMapPajek(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey()),
                    measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(),
                    KeyProperties.__KEY_CLUSTER_LABEL);

            out.println("          <td><a href=\"" + getFilePathEvolutiongMapPajek(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey()) + "\">Pakej file</a></td>");

            out.println("        </tr>");
        }

        clusterSets.clear();

        out.println("      </table>");

        out.println("  </body>");
        out.println("</html>");

        out.close();
    }

    /**
     *
     * @param period
     * @param periodPosition
     * @param results
     * @throws FileNotFoundException
     * @throws IOException
     * @throws TranscoderException
     */
    private void doPeriodHTLMPage(String escapedPeriodName, int periodPosition, AnalysisPeriodResult results)
            throws FileNotFoundException, IOException, TranscoderException, KnowledgeBaseException {

        int clusterIndex, j;
        double maxFrequency;
        String svg, escapedClusterName;
        StrategicDiagramBuildier strategicDiagramBuildier;
        PerformanceMeasuresAvailable measuresAvailable;
        StrategicDiagram strategicDiagram;
        StrategicDiagramSVG strategicDiagramSVG;
        WholeNetworkPajek wholeNetworkPajek;
        Cluster cluster;
        ClusterNetworkSVG clusterNetworkSVG;
        PrintStream out;

        out = new PrintStream(getFileAbsolutePathPeriodHTMLPage(periodPosition));

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"es\">");
        out.println("<head>");
        out.println("    <title>SciMAT report - subperiod " + escapedPeriodName + "</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>SciMAT HTML report - " + escapedPeriodName + "</h1>");
        out.println("    <hr/>");
        out.println("    <h2>Period's parameters</h2>");
        out.println("    <ul>");
        out.println("      <li>Min frequency: " + this.globalExperimentResult.getAnalysisConfiguration().getMinFrequency(periodPosition) + "</li>");
        out.println("      <li>Min co-occurrence: " + this.globalExperimentResult.getAnalysisConfiguration().getMinCoOccurrence(periodPosition) + "</li>");
        out.println("    </ul>");
        out.println("    <h2>Strategic diagrams</h2>");

        strategicDiagramBuildier = new StrategicDiagramBuildier(KeyProperties.__KEY_CALLON_CENTRALITY_RANGE,
                KeyProperties.__KEY_CALLON_DENSITY_RANGE,
                KeyProperties.__KEY_CLUSTER_LABEL);

        strategicDiagramSVG = new StrategicDiagramSVG(__STRATEGIC_DIAGRAM_FILE_WIDTH,
                __STRATEGIC_DIAGRAM_MAX_RADIUS,
                __STRATEGIC_DIAGRAM_MIN_RADIUS,
                __TEXT_PX);

        out.println("      <table border=\"1\" summary=\"this table shows the strategic diagram of the period " + escapedPeriodName + "\">");
        out.println("        <caption><em>Strategic diagram</em></caption>");
        out.println("        <tr>");
        out.println("          <td>Performance measure</td>");
        out.println("          <td>Strategic diagram</td>");
        out.println("        </tr>");

        for (clusterIndex = 0; clusterIndex < this.performanceMeasuresAvailable.size(); clusterIndex++) {

            measuresAvailable = this.performanceMeasuresAvailable.get(clusterIndex);

            strategicDiagram = strategicDiagramBuildier.buildStrategicDiagram(results.getClusterSet(), measuresAvailable.getMapper() + measuresAvailable.getPropertyKey());

            svg = this.domToString.convert(strategicDiagramSVG.buildXML(strategicDiagram));

            svgToFile(getFileAbsolutePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __SVG_EXTENSION), svg);
            TranscoderSVGtoPNG.transcoder(getFileAbsolutePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __SVG_EXTENSION), getFileAbsolutePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __PNG_EXTENSION));

            out.println("        <tr>");
            out.println("          <td>" + measuresAvailable.getMapper() + "-" + measuresAvailable.getPropertyKey() + "</td>");
            out.println("          <td><img alt=\"Strategic diagram of the period "
                    + periodPosition + "\" src=\"" + getFilePathStrategicDiagram(periodPosition, measuresAvailable.getMapper() + measuresAvailable.getPropertyKey(), __PNG_EXTENSION)
                    + "\" width=\"" + __STRATEGIC_DIAGRAM_BIG_WIDTH + "\" /></td>");
            out.println("        </tr>");
        }

        out.println("      </table>");

        out.println("    <h2>Whole network</h2>");

        maxFrequency = maxPeriodNodeFrequency(results.getClusterSet().getWholeNetwork());

        wholeNetworkPajek = new WholeNetworkPajek(results.getClusterSet(), KeyProperties.__KEY_NODE_FREQUENCY, KeyProperties.__KEY_NODE_LABEL);

        wholeNetworkPajek.execute(getFileAbsolutePathWholeNetworkPajek(periodPosition), maxFrequency);

        out.println("      <a href=\"" + getFilePathWholeNetworkPajekm(periodPosition) + "\">Whole network - Pajek File</a>");

        out.println("    <h2>Clusters</h2>");
        out.println("      <table border=\"1\" summary=\"this table shows the basic measures of the clusters of the period " + escapedPeriodName + "\">");
        out.println("        <caption><em>Clusters information</em></caption>");
        out.println("        <tr>");
        out.println("          <td>Name</td>");
        out.println("          <td>Centrality</td>");
        out.println("          <td>Centrality range</td>");
        out.println("          <td>Density</td>");
        out.println("          <td>Density range</td>");
        out.println("        </tr>");

        for (clusterIndex = 0; clusterIndex < results.getClusterSet().getClustersCount(); clusterIndex++) {

            cluster = results.getClusterSet().getCluster(clusterIndex);

            out.println("        <tr>");
            out.println("          <td>" + StringEscapeUtils.escapeHtml4(cluster.getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()) + "</td>");
            out.println("          <td>" + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY).getValue()) + "</td>");
            out.println("          <td>" + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY_RANGE).getValue()) + "</td>");
            out.println("          <td>" + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY).getValue()) + "</td>");
            out.println("          <td>" + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY_RANGE).getValue()) + "</td>");
            out.println("        </tr>");
        }

        out.println("      </table>");

        out.println("      <table border=\"1\" summary=\"this table shows the performance measures of the clusters of the period " + escapedPeriodName + "\">");
        out.println("        <caption><em>Clusters information</em></caption>");
        out.println("        <tr>");
        out.println("          <td>Name</td>");

        for (clusterIndex = 0; clusterIndex < this.performanceMeasuresAvailable.size(); clusterIndex++) {

            measuresAvailable = this.performanceMeasuresAvailable.get(clusterIndex);

            out.println("          <td>" + measuresAvailable.getMapper() + measuresAvailable.getPropertyKey() + "</td>");
        }

        out.println("        </tr>");

        for (clusterIndex = 0; clusterIndex < results.getClusterSet().getClustersCount(); clusterIndex++) {

            cluster = results.getClusterSet().getCluster(clusterIndex);

            out.println("        <tr>");
            out.println("          <td>" + StringEscapeUtils.escapeHtml4(cluster.getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()) + "</td>");

            for (j = 0; j < this.performanceMeasuresAvailable.size(); j++) {

                measuresAvailable = this.performanceMeasuresAvailable.get(j);

                out.println("          <td>" + this.numberFormatter.format(cluster.getProperties().getProperty(measuresAvailable.getMapper() + measuresAvailable.getPropertyKey()).getValue()) + "</td>");
            }

            out.println("        </tr>");
        }

        out.println("      </table>");

        out.println("    <h2>Clusters' network</h2>");

        clusterNetworkSVG = new ClusterNetworkSVG(__CLUSTER_NETWORK_FILE_WIDTH,
                __CLUSTER_NETWORK_MAX_RADIUS,
                __CLUSTER_NETWORK_MIN_RADIUS,
                __TEXT_PX);

        out.println("      <table border=\"1\" summary=\"this table shows the performance measures of the clusters of the period " + escapedPeriodName + "\">");
        out.println("        <caption><em>Clusters information</em></caption>");
        out.println("        <tr>");
        out.println("          <td>Name</td>");
        out.println("          <td>Cluster's network</td>");
        out.println("        </tr>");

        for (clusterIndex = 0; clusterIndex < results.getClusterSet().getClustersCount(); clusterIndex++) {

            cluster = results.getClusterSet().getCluster(clusterIndex);

            svg = this.domToString.convert(clusterNetworkSVG.buildXML(cluster,
                    results.getClusterSet().getWholeNetwork(),
                    KeyProperties.__KEY_NODE_FREQUENCY,
                    KeyProperties.__KEY_NODE_LABEL, maxFrequency));

            svgToFile(getFileAbsolutePathClusterNetwork(periodPosition, clusterIndex, __SVG_EXTENSION), svg);
            TranscoderSVGtoPNG.transcoder(getFileAbsolutePathClusterNetwork(periodPosition, clusterIndex, __SVG_EXTENSION), getFileAbsolutePathClusterNetwork(periodPosition, clusterIndex, __PNG_EXTENSION));

            escapedClusterName = StringEscapeUtils.escapeHtml4(cluster.getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString());

            out.println("        <tr>");
            out.println("          <td><a href=\"" + getFilePathClusterHTMLPage(periodPosition, clusterIndex) + "\">" + escapedClusterName + "</td>");
            out.println("          <td><img alt=\"Cluster network\" src=\"" + getFilePathClusterNetwork(periodPosition, clusterIndex, __PNG_EXTENSION)
                    + "\" width=\"" + __CLUSTER_NETWORK_BIG_WIDTH + "\" /></td>");
            out.println("        </tr>");

            doClusterHTLMPage(periodPosition, escapedPeriodName, escapedClusterName, cluster, clusterIndex, results.getClusterSet());
        }

        out.println("      </table>");

        out.println("  </body>");
        out.println("</html>");

        out.close();
    }

    /**
     *
     * @param periodPosition
     * @param escapedPeriodName
     * @param escapedClusterName
     * @param clusterSet
     * @throws FileNotFoundException
     * @throws IOException
     * @throws TranscoderException
     */
    private void doClusterHTLMPage(int periodPosition, String escapedPeriodName,
            String escapedClusterName, Cluster cluster, int clusterIndex,
            ClusterSet clusterSet)
            throws FileNotFoundException, IOException, TranscoderException, KnowledgeBaseException {

        int i, j, clusterOfNode;
        ArrayList<NetworkPair> pairs;
        String mapper;
        NetworkPair pair;
        PrintStream out;

        out = new PrintStream(getFileAbsolutePathClusterHTMLPage(periodPosition, clusterIndex));

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"es\">");
        out.println("<head>");
        out.println("    <title>SciMAT report - subperiod " + escapedPeriodName
                + " - cluster " + escapedClusterName + "</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>SciMAT report - subperiod " + escapedPeriodName
                + " - cluster " + escapedClusterName + "</h1>");
        out.println("    <hr/>");

        out.println("    <h2>Cluster info:</h2>");
        out.println("    <ul>");
        out.println("      <li>Name: " + escapedClusterName + "</li>");
        out.println("      <li>Density: " + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY).getValue()) + "</li>");
        out.println("      <li>Densisty range: " + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_DENSITY_RANGE).getValue()) + "</li>");
        out.println("      <li>Centrality: " + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY).getValue()) + "</li>");
        out.println("      <li>Centrality range: " + this.numberFormatter.format(cluster.getProperties().getProperty(KeyProperties.__KEY_CALLON_CENTRALITY_RANGE).getValue()) + "</li>");
        out.println("    </ul>");

        out.println("    <h2>Cluster network:</h2>");

        out.println("<img alt=\"Cluster network\" src=\"" + getFilePathClusterNetwork(periodPosition, clusterIndex, __PNG_EXTENSION)
                + "\" width=\"" + __CLUSTER_NETWORK_BIG_WIDTH + "\" />");

        out.println("    <h2>Internal links:</h2>");

        out.println("      <table border=\"1\" summary=\"this table shows the internal links of the cluster " + escapedClusterName + " of the period " + escapedPeriodName + "\">");
        out.println("        <caption><em>Internal links</em></caption>");
        out.println("        <tr>");
        out.println("          <td>Node A</td>");
        out.println("          <td>Node B</td>");
        out.println("          <td>Weight</td>");
        out.println("        </tr>");

        pairs = clusterSet.getClusterInternalPairs(clusterIndex);

        for (i = 0; i < pairs.size(); i++) {

            pair = pairs.get(i);
            out.println("        <tr>");
            out.println("          <td>" + StringEscapeUtils.escapeHtml4(clusterSet.getWholeNetwork().getNode(pair.getID().getElementA()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + "</td>");
            out.println("          <td>" + StringEscapeUtils.escapeHtml4(clusterSet.getWholeNetwork().getNode(pair.getID().getElementB()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + "</td>");
            out.println("          <td>" + this.numberFormatter.format(pair.getValue()) + "</td>");
            out.println("        </tr>");
        }

        pairs.clear();

        out.println("      </table>");

        out.println("    <h2>External links:</h2>");

        out.println("      <table border=\"1\" summary=\"this table shows the external links of the cluster " + escapedClusterName + " of the period " + escapedPeriodName + "\">");
        out.println("        <caption><em>External links</em></caption>");
        out.println("        <tr>");
        out.println("          <td>Node A</td>");
        out.println("          <td>Cluster node A</td>");
        out.println("          <td>Node B</td>");
        out.println("          <td>Cluster node B</td>");
        out.println("          <td>Weight</td>");
        out.println("        </tr>");

        pairs = clusterSet.getClusterExternalPairs(clusterIndex);

        for (i = 0; i < pairs.size(); i++) {

            pair = pairs.get(i);

            out.println("        <tr>");
            out.println("          <td>" + StringEscapeUtils.escapeHtml4(clusterSet.getWholeNetwork().getNode(pair.getID().getElementA()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + "</td>");

            clusterOfNode = clusterSet.getClusterOfNode(pair.getID().getElementA());

            if (clusterOfNode != -1) {

                out.println("          <td>" + StringEscapeUtils.escapeHtml4(clusterSet.getCluster(clusterOfNode).getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()) + "</td>");

            } else {

                out.println("          <td></td>");
            }

            out.println("          <td>" + StringEscapeUtils.escapeHtml4(clusterSet.getWholeNetwork().getNode(pair.getID().getElementB()).getProperties().getProperty(KeyProperties.__KEY_NODE_LABEL).getValue().toString()) + "</td>");

            clusterOfNode = clusterSet.getClusterOfNode(pair.getID().getElementB());

            if (clusterOfNode != -1) {

                out.println("          <td>" + StringEscapeUtils.escapeHtml4(clusterSet.getCluster(clusterOfNode).getProperties().getProperty(KeyProperties.__KEY_CLUSTER_LABEL).toString()) + "</td>");

            } else {

                out.println("          <td></td>");
            }

            out.println("          <td>" + this.numberFormatter.format(pair.getValue()) + "</td>");
            out.println("        </tr>");
        }

        pairs.clear();

        out.println("      </table>");

        out.println("      <h2>Documents associated with the cluster</h2>");

        ArrayList<Integer> idDocuments;
        DocumentDAO documentDAO = new DocumentDAO(this.kbm);

        for (i = 0; i < this.documentMappers.size(); i++) {

            mapper = this.documentMappers.get(i);

            out.println("      <h3>" + mapper + " (100 first documents with highest impact) </h3>");

            idDocuments = ((DocumentSet) clusterSet.getCluster(clusterIndex).getProperties().getProperty(mapper).getValue()).getDocuments();

            Collections.sort(idDocuments, (Integer d1, Integer d2) -> {

                int comp;
                Integer citations1, citations2;
                comp = 0;
                try {

                    citations1 = documentDAO.getDocument(d1).getCitationsCount();
                    citations2 = documentDAO.getDocument(d2).getCitationsCount();

                    comp = citations2.compareTo(citations1);

                } catch (KnowledgeBaseException kbe) {

                    kbe.printStackTrace(System.err);
                }
                return comp;
            });

            out.println("      <ul>");

            for (j = 0; j < idDocuments.size() && j < 100; j++) {

                out.println("        <li>" + this.documentFormatter.format(idDocuments.get(j)) + "</li>");
            }

            idDocuments.clear();

            out.println("      </ul>");
        }

        out.println("  </body>");
        out.println("</html>");

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

        nodes.clear();

        return max;
    }

    /**
     *
     * @param path
     * @param document
     * @throws FileNotFoundException
     */
    private void svgToFile(String path, String document) throws FileNotFoundException {

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

            return file.mkdirs();
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
     * @param period
     * @return
     */
    private String getFileAbsolutePathClusterHTMLPage(int period, int cluster) {

        return path + File.separator + getFilePathClusterHTMLPage(period, cluster);
    }

    /**
     *
     * @param period
     * @return
     */
    private String getFilePathClusterHTMLPage(int period, int cluster) {

        return "period" + period + "-cluster" + cluster + ".html";
    }

    /**
     *
     * @return
     */
    private String getFileAbsolutePathMainHTMLPage() {

        return path + File.separator + getFilePathMainHTMLPage();
    }

    /**
     *
     * @return
     */
    private String getFilePathMainHTMLPage() {

        return "index.html";
    }

    /**
     *
     * @return
     */
    private String getFileAbsolutePathStrategicDiagram(int period, String subName, String extension) {

        return path + File.separator + __IMAGES_FOLDER_NAME + File.separator
                + "strategicDiagram-period" + String.valueOf(period) + "-" + subName + "." + extension;
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

        return path + File.separator + __IMAGES_FOLDER_NAME + File.separator
                + "clusterNetwork-period" + String.valueOf(period) + "-cluster"
                + String.valueOf(theme + 1) + "." + extension;
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

        return __IMAGES_FOLDER_NAME + "/"
                + "clusterNetwork-period" + String.valueOf(period) + "-cluster"
                + String.valueOf(theme + 1) + "." + extension;
    }

    /**
     *
     * @param extension
     * @return
     */
    private String getFileAbsolutePathOverlappingMap(String extension) {

        return path + File.separator + __IMAGES_FOLDER_NAME + File.separator
                + "overlappingMap" + "." + extension;
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

        return path + File.separator + __IMAGES_FOLDER_NAME + File.separator
                + "evolutionMap-" + subname + "." + extension;
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

        return path + File.separator + __MISC_FOLDER_NAME + File.separator
                + "evolutionMap-" + subname + ".net";
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

        return path + File.separator + __MISC_FOLDER_NAME + File.separator
                + "wholeNetwork-period" + String.valueOf(period) + ".net";
    }

    /**
     *
     * @return
     */
    private String getFilePathWholeNetworkPajekm(int period) {

        return __MISC_FOLDER_NAME + "/" + "wholeNetwork-period" + String.valueOf(period) + ".net";
    }

}
