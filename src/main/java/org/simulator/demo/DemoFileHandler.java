/**
 *
 */
package org.simulator.demo;

import static java.text.MessageFormat.format;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.TidySBMLWriter;
import org.simulator.math.odes.MultiTable;

/**
 * @author Andreas Dr&auml;ger
 */
public class DemoFileHandler {

  /**
   * A Logger for this class.
   */
  private static final transient Logger logger = Logger.getLogger(DemoFileHandler.class.getName());

  /**
   * Default example for a constraint based model.
   */
  public static final String defaultCOBRAmodel = "http://bigg.ucsd.edu/static/models/e_coli_core.xml.gz";

  /**
   * Default example model.
   */
  public static final String defaultDynamicModel =
      "https://www.ebi.ac.uk/biomodels/model/download/BIOMD0000000010.2?filename=BIOMD0000000010_url.xml";

  /**
   * Default example for a SED-ML file
   */
  public static final String defaultSEDMLfile = "";


  /**
   * Reads an SBML model from a given URL and writes it into a temporary file which is then returned.
   *
   * @param url
   * @return a pointer to a temporary file that contains the content read from the given URL.
   * @throws IOException
   * @throws XMLStreamException
   */
  private static File createTemporarySBMLfile(String url) throws IOException, XMLStreamException {
    URL urlobj = new URL(url);
    HttpURLConnection urlConnection = (HttpURLConnection) urlobj.openConnection();
    InputStream in = urlConnection.getInputStream();
    String fileName = url.substring(url.lastIndexOf('/'), url.lastIndexOf('.'));
    String ext = url.substring(url.lastIndexOf('.'));
    if (url.endsWith(".gz")) {
      in = new GZIPInputStream(in);
      fileName = fileName.substring(0, fileName.lastIndexOf('.'));
      ext = url.substring(0, url.length() - 3);
      ext = ext.substring(ext.lastIndexOf('.'));
    }
    SBMLDocument doc = SBMLReader.read(in);
    File outfile = File.createTempFile(fileName, ext);
    logger.info(format("Writing temporary file {0}.", outfile.getAbsolutePath()));
    OutputStream out = new FileOutputStream(outfile);
    TidySBMLWriter.write(doc, out, ' ', (short) 2);
    out.close();
    return outfile;
  }

  /**
   * @return {@code true} if url is valid
   */
  public static boolean isValid(String url)
  {
    /* Try creating a valid URL */
    try {
      new URL(url).toURI();
      return true;
    } catch (Exception e) {
      // catches any exception while creating URL object
      return false;
    }
  }

  /**
   * Attempts to access the given file or URL and possibly creates a temporary
   * File containing the URL's content.
   *
   * @param fileOrURL
   * @return a pointer to a file with the given content (can be a temporary file
   *   if a URL was given) or null if it cannot be read at all.
   * @throws XMLStreamException
   * @throws IOException
   */
  public static File load(String fileOrURL) throws IOException, XMLStreamException {
    File f = new File(fileOrURL);
    if (f.isFile() && f.canRead()) {
      return f;
    } else if (isValid(fileOrURL)){
      return createTemporarySBMLfile(fileOrURL);
    }
    return null;
  }


  /**
   * Prints the given time course simulation to the given output stream.
   *
   * @param solution
   * @param out
   */
  public static void printResult(MultiTable solution, PrintStream out) {
    for (int i = 0; i < solution.getColumnCount(); i++) {
      out.print(solution.getColumnName(i) + ",");
    }
    out.println();
    for (int i = 0; i < solution.getRowCount(); i++) {
      out.print(solution.getTimePoint(i) + ",");
      for (int j = 0; j < solution.getColumnCount(); j++) {
        out.print(solution.getValueAt(i, j) + ",");
      }
      out.println();
    }
  }

}
