/**
 *
 */
package org.simulator.demo;

import static java.text.MessageFormat.format;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jfree.ui.RefineryUtilities;
import org.jlibsedml.AbstractTask;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Output;
import org.jlibsedml.Plot2D;
import org.jlibsedml.SedML;
import org.jlibsedml.execution.IProcessedSedMLSimulationResults;
import org.jlibsedml.execution.IRawSedmlSimulationResults;
import org.simulator.plot.PlotProcessedSedmlResults;
import org.simulator.sedml.SedMLSBMLSimulatorExecutor;

/**
 * @author Andreas Dr&auml;ger
 *
 */
public class SEDMLdemoSimple {

  /** A Logger for this class. */
  private static final transient Logger logger = Logger.getLogger(SEDMLdemoSimple.class.getName());

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    File file = new File(args[0]);
    SedML sedml = Libsedml.readDocument(file).getSedMLModel();
    // We assume our SED-ML file to have just one output. If there were several, we could either
    // iterate or get the user to decide which one to run.
    Output wanted = sedml.getOutputs().get(0);
    SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted, file.getParent());
    // This gets the raw simulation results - one for each Task that was run.
    logger.info("Collecting tasks...");
    Map<AbstractTask, List<IRawSedmlSimulationResults>> res = exe.run();
    if ((res == null) || res.isEmpty() || !exe.isExecuted()) {
      logger.warning(format("Simulatation failed: {0}", exe.getFailureMessages().get(0)));
      return;
    }
    // Now process: In this case, there's no processing performed - we're displaying the raw results.
    logger.info(format("Outputs wanted: {0}", wanted.getId()));
    IProcessedSedMLSimulationResults prRes = exe.processSimulationResults(wanted, res);

    if (wanted.isPlot2d()) {
      Plot2D plots = (Plot2D) wanted;
      // plot all processed results as per curve descriptions
      PlotProcessedSedmlResults p = new PlotProcessedSedmlResults(prRes, plots.getListOfCurves(), plots.getElementName());
      p.pack();
      RefineryUtilities.centerFrameOnScreen(p);
      p.setVisible(true);
    }
  }
}
