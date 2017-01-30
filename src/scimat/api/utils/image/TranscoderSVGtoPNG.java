/*
 * TranscoderSVGtoPNG.java
 *
 * Created on 06-jun-2011, 18:48:21
 */
package scimat.api.utils.image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 *
 * @author mjcobo
 */
public class TranscoderSVGtoPNG {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public static void transcoder(String fileSVG, String fileOutput)
    throws FileNotFoundException, TranscoderException, IOException {

    // Create a PNG transcoder
    PNGTranscoder t = new PNGTranscoder();
    // Set the transcoding hints.
    //t.addTranscodingHint(PNGTranscoder.KEY_GAMMA, new Float(0.9));

    // Create the transcoder input.
    //String svgURI = new File(fileSVG).toURI().toString();
    TranscoderInput input = new TranscoderInput(new FileInputStream(fileSVG));

    // Create the transcoder output.
    OutputStream ostream = new FileOutputStream(fileOutput);
    TranscoderOutput output = new TranscoderOutput(ostream);

    // Save the image.
    t.transcode(input, output);

    // Flush and close the stream.
    ostream.flush();
    ostream.close();
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
