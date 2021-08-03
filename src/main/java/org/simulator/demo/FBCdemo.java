package org.simulator.demo;

import static java.text.MessageFormat.format;

import java.io.File;
import java.util.logging.Logger;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.simulator.fba.FluxBalanceAnalysis;

/**
 * A minimal example to demonstrate how to run a constraint-based analysis.
 *
 * @author Andreas Dr&auml;ger
 */
public class FBCdemo {

  /** A logger for this class */
  private static final transient Logger logger = Logger.getLogger(FBCdemo.class.getName());

  /**
   * Passes the given {@link SBMLDocument} to the {@link FluxBalanceAnalysis}
   * solver and launches the analysis. It logs the solution or an error if needed.
   *
   * @param doc
   * @throws Exception
   */
  public FBCdemo(SBMLDocument doc) throws Exception {
    FluxBalanceAnalysis solver = new FluxBalanceAnalysis(doc);
    if (solver.solve()) {
      logger.info(format("Objective value:\t{0}", solver.getObjectiveValue()));
      logger.info(format("Fluxes:\t{0}", solver.getSolution()));
    } else {
      logger.warning(format("Solver returned null for model {0}.", doc.getModel().getName()));
    }
  }

  /**
   * Launches a flux balance analysis and prints the objective value.
   *
   * @param args Path to an SBML file with Flux Balance Constraints (fbc) package
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new FBCdemo(SBMLReader.read(new File(args[0])));
  }

}