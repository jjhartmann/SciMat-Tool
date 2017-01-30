/*
 * WholeNetworkPajek.java
 *
 * Created on 03-jun-2011, 15:51:27
 */
package scimat.api.visualization.graph;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import scimat.api.dataset.NetworkPair;
import scimat.api.mapping.Node;
import scimat.api.mapping.clustering.result.ClusterSet;
import scimat.api.utils.property.DoubleProperty;

/**
 *
 * @author mjcobo
 */
public class WholeNetworkPajek {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ClusterSet clusterSet;
  
  private String volumePropertyName;
  private String labelPropertyName;
  
  private String[] colors = new String[] {"Red", "Yellow", "Blue", "Green", 
    "Pink", "Dandelion", "GreenYellow", "Lavender", "PineGreen", "Peach", 
    "Magenta", "Fuchsia", "Bittersweet", "Salmon", "Mulberry", "RedViolet", 
    "CornflowerBlue", "BlueGreen", "RawSienna", "Tan", "LightYellow", 
    "LightCyan", "LightMagenta", "LightPurple", "MidnightBlue", "LFadedGreen", 
    "Orchid", "Gray05", "Gray20", "Gray35", "Gray70", "Gray95"};
  
  private NumberFormat numberFormat = new DecimalFormat ( "0.0000" );
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param clusterSet 
   */
  public WholeNetworkPajek(ClusterSet clusterSet, String frequencyKey, String nodeLabelKey) {
    
    this.clusterSet = clusterSet;
    this.volumePropertyName = frequencyKey;
    this.labelPropertyName = nodeLabelKey;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param path
   * @param maxSize
   * @throws FileNotFoundException 
   */
  public void execute(String path, double maxSize) throws FileNotFoundException {

    int i;
    Integer themeOfNode, themeOfNodeA, themeOfNodeB;
    double nodeSize;
    TreeMap<Integer, Integer> indexMap = new TreeMap<Integer, Integer>();
    PrintStream out = new PrintStream(path);
    String icColor, bcColor;
    ArrayList<NetworkPair> networkPairs;
    NetworkPair networkPair;
    

    ArrayList<Node> nodes = this.clusterSet.getWholeNetwork().getNodes();

    // Calculamos el indice de los nodos en el fichero.
    for (i = 0; i < nodes.size(); i++) {

      indexMap.put(nodes.get(i).getNodeID(), i + 1);

    }

    // Pintamos la zona de vertices.
    out.println("*vertices " + this.clusterSet.getWholeNetwork().getNodesCount());

    for (i = 0; i < nodes.size(); i++) {

      // Get the color of the node
      themeOfNode = this.clusterSet.getClusterOfNode(nodes.get(i).getNodeID());
      
      if (themeOfNode != -1) {
      
        if (themeOfNode < this.colors.length) {
        
          icColor = bcColor = this.colors[themeOfNode];
          
        } else {
        
          icColor = bcColor = this.colors[this.colors.length - 1];
        }
        
      } else {
      
        icColor = "White";
        bcColor = "Black";
      }
      
      //out.println((i + 1) + "\t\"" + kg.getName() + "\"\t");
      out.print(indexMap.get(nodes.get(i).getNodeID()));
      out.print(" ");
      out.print("\"" + nodes.get(i).getProperties().getProperty(this.labelPropertyName).getValue() + "\"");
      out.print(" ");
      out.print("ic " + icColor);
      out.print(" ");
      out.print("bc " + bcColor);
      out.print(" ");

      nodeSize = ((DoubleProperty)nodes.get(i).getProperties().getProperty(this.volumePropertyName)).getValue() * 10.0 / maxSize;

      out.print("x_fact " + Math.ceil(nodeSize));
      out.print(" ");
      out.println("y_fact " + Math.ceil(nodeSize));
    }

    networkPairs = this.clusterSet.getWholeNetwork().getNetworkPairs();

    out.println("*edges");

    for (i = 0; i < networkPairs.size(); i++) {

      networkPair = networkPairs.get(i);

      out.print(indexMap.get(networkPair.getID().getElementA()));
      out.print(" ");
      out.print(indexMap.get(networkPair.getID().getElementB()));
      out.print(" ");
      //out.print(_numberFormatter.format(pair.getEquivalenceIndex() * 10.0));
      out.print(this.numberFormat.format(networkPair.getValue() * 10.0));
      out.print(" ");
      
      themeOfNodeA = this.clusterSet.getClusterOfNode(networkPair.getID().getElementA());
      themeOfNodeB = this.clusterSet.getClusterOfNode(networkPair.getID().getElementB());
      
      if (themeOfNodeA == themeOfNodeB) {
      
        if ((themeOfNodeA != -1) && (themeOfNodeA < this.colors.length)) {
        
          out.println("c " + this.colors[themeOfNodeA]);
          
        } else {
        
          out.println("c " + this.colors[this.colors.length - 1]);
        }
        
      } else {
      
        out.println("c Black");
      }

    }

    out.close();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
