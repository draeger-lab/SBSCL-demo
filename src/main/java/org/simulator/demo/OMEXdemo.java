/**
 *
 */
package org.simulator.demo;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jlibsedml.AbstractTask;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Output;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.execution.IRawSedmlSimulationResults;
import org.simulator.omex.OMEXArchive;
import org.simulator.sedml.MultTableSEDMLWrapper;
import org.simulator.sedml.SedMLSBMLSimulatorExecutor;

/**
 * @author Andreas Dr&auml;ger
 *
 */
public class OMEXdemo {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    OMEXArchive archive = new OMEXArchive(new File(args[0]));

    if (archive.containsSBMLModel() && archive.containsSEDMLDescp()) {
      // Execute SED-ML file and run simulations
      SEDMLDocument doc = Libsedml.readDocument(archive.getSEDMLDescription());
      SedML sedml = doc.getSedMLModel();

      Output wanted = sedml.getOutputs().get(0);
      SedMLSBMLSimulatorExecutor exe = new SedMLSBMLSimulatorExecutor(sedml, wanted, archive.getSEDMLDescription().getParentFile().getAbsolutePath());

      Map<AbstractTask, List<IRawSedmlSimulationResults>> res = exe.run();

      for (List<IRawSedmlSimulationResults> re_list : res.values()) {
        for (IRawSedmlSimulationResults re : re_list) {
          MultTableSEDMLWrapper wrapper = (MultTableSEDMLWrapper) re;
          // process the result
        }
      }
    }
  }
}
