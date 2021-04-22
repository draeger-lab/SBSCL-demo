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
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.validator.ModelOverdeterminedException;
import org.simulator.examples.BiomodelsExample;
import org.simulator.examples.CompExample;
import org.simulator.examples.ConstraintExample;
import org.simulator.examples.FBAExample;
import org.simulator.examples.OMEXExample;
import org.simulator.examples.SEDMLExample;
import org.simulator.examples.SimulatorExample;

public class SBSCLdemo {

  private static final transient Logger logger = Logger.getLogger(SBSCLdemo.class.getName());
  private static final String defaultCOBRAmodel = "http://bigg.ucsd.edu/static/models/e_coli_core.xml.gz";

  private BiomodelsExample biomodels;
  private CompExample comp;
  private ConstraintExample constraint;
  private FBAExample fba;
  private OMEXExample omex;
  private SEDMLExample sedml;
  private SimulatorExample sim;

  /**
   * Performs exemplary simulations with a given SBML file.
   * @param sbml
   * @throws IOException
   * @throws XMLStreamException
   * @throws ModelOverdeterminedException
   * @throws SBMLException
   */
  public SBSCLdemo(File sbml) throws SBMLException, ModelOverdeterminedException, XMLStreamException, IOException {
    fba = new FBAExample(sbml);
  }

  /**
   * If no arguments are given, a default model is loaded from the web.
   *
   * @param args A URL or path to a file in SBML format.
   *
   * @throws XMLStreamException
   * @throws IOException
   * @throws ModelOverdeterminedException
   * @throws SBMLException
   */
  public static void main(String args[]) throws XMLStreamException, IOException, SBMLException, ModelOverdeterminedException {
    if (args.length > 0) {
      File f = new File(args[0]);
      if (f.isFile() && f.canRead()) {
        new SBSCLdemo(f);
      } else if (isValid(args[0])){
        new SBSCLdemo(createTemporarySBMLfile(args[0]));
      }
    } else {
      new SBSCLdemo(createTemporarySBMLfile(defaultCOBRAmodel));
    }
  }

  /**
   * Reads an SBML model from a given URL and writes it into a temporary file which is then returned.
   *
   * @param url
   * @return
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

}