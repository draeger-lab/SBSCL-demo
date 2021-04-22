package org.simulator.demo;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.validator.ModelOverdeterminedException;
import org.simulator.examples.BiomodelsExample;
import org.simulator.examples.CompExample;
import org.simulator.examples.ConstraintExample;
import org.simulator.examples.FBAExample;
import org.simulator.examples.OMEXExample;
import org.simulator.examples.SEDMLExample;
import org.simulator.examples.SimulatorExample;

/**
 *  @author Andreas Dr&auml;ger
 */
public class SBSCLdemo {

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
    File demoFile;
    if (args.length > 0) {
      demoFile = DemoFileLoader.load(args[0]);
    } else {
      demoFile = DemoFileLoader.load(DemoFileLoader.defaultCOBRAmodel);
    }
    new SBSCLdemo(demoFile);
  }

}
