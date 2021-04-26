/**
 *
 */
package org.simulator.demo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.jlibsedml.Libsedml;
import org.jlibsedml.Output;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.XMLException;
import org.simulator.math.odes.MultiTable;
import org.simulator.sedml.SedMLSBMLSimulatorExecutor;

/**
 * @author Andreas Dr&auml;ger
 *
 */
public class SEDMLdemo {

  private  MultiTable solution;

  /**
   *
   * @param doc
   */
  public SEDMLdemo(SEDMLDocument doc) {
    SedML sedml = doc.getSedMLModel();

    Output wanted = sedml.getOutputs().get(0);

    SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted, ".");

    Map res = exe.runSimulations();

    solution = (MultiTable) exe.processSimulationResults(wanted, res);
  }

  /**
   * @param args
   * @throws XMLStreamException
   * @throws IOException
   * @throws XMLException
   */
  public static void main(String[] args) throws IOException, XMLStreamException, XMLException {
    // Load SBML model from file
    File demoFile;
    if (args.length > 0) {
      demoFile = DemoFileHandler.load(args[0]);
    } else {
      demoFile = DemoFileHandler.load(DemoFileHandler.defaultSEDMLfile);
    }

    SEDMLdemo demo = new SEDMLdemo(Libsedml.readDocument(demoFile));

    DemoFileHandler.printResult(demo.getSolution(), System.out);
  }

  public MultiTable getSolution() {
    return solution;
  }

}
