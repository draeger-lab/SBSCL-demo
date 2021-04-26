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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.TidySBMLWriter;

/**
 * @author Andreas Dr&auml;ger
 */
public class DemoFileLoader {

  /**
   * A Logger for this class.
   */
  private static final transient Logger logger = Logger.getLogger(DemoFileLoader.class.getName());

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
    if (url.endsWith(".gz")) {
      in = new GZIPInputStream(in);
      fileName = fileName.substring(0, fileName.lastIndexOf('.'));
    }
    SBMLDocument doc = SBMLReader.read(in);
    File outfile = File.createTempFile(fileName, ".xml");
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

}
