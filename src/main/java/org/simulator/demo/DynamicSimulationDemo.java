package org.simulator.demo;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.math.ode.DerivativeException;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.validator.ModelOverdeterminedException;
import org.simulator.math.odes.AbstractDESSolver;
import org.simulator.math.odes.MultiTable;
import org.simulator.math.odes.RosenbrockSolver;
import org.simulator.sbml.SBMLinterpreter;

/**
 * This clas shows how SBSCL can be used for a dynamic simulation of an example
 * SBML file.
 *
 * @author Andreas Dr&auml;ger
 */
public class DynamicSimulationDemo {

  private MultiTable solution;

  /**
   * Performs a dynamic simulation for the given model file at the given time
   * points.
   *
   * @param doc
   * @param timePoints
   * @throws SBMLException
   * @throws ModelOverdeterminedException
   * @throws DerivativeException
   */
  public DynamicSimulationDemo(SBMLDocument doc, double timePoints[])
      throws SBMLException, ModelOverdeterminedException, DerivativeException {
    SBMLinterpreter interpreter = new SBMLinterpreter(doc.getModel());
    AbstractDESSolver solver = new RosenbrockSolver();
    solution =
        solver.solve(interpreter, interpreter.getInitialValues(), timePoints);
  }


  /**
   * Loads a default model from the web, conducts a time-course simulation at
   * fixed time points and prints the solution to the standard out stream.
   *
   * @param args
   *        optional: if an absolute or relative path to a valid SBML file is
   *        given, this file will be loaded for simulation. If no arguments are
   *        given, a default file will be downloaded automatically.
   * @throws XMLStreamException
   * @throws IOException
   * @throws ModelOverdeterminedException
   * @throws SBMLException
   * @throws DerivativeException
   */
  public static void main(String[] args) throws IOException, XMLStreamException,
  SBMLException, ModelOverdeterminedException, DerivativeException {

    // Load SBML model from file
    File demoFile;
    if (args.length > 0) {
      demoFile = DemoFileLoader.load(args[0]);
    } else {
      demoFile = DemoFileLoader.load(DemoFileLoader.defaultDynamicModel);
    }

    // Conduct the simulation
    double[] timePoints = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5};
    DynamicSimulationDemo demo =
        new DynamicSimulationDemo(SBMLReader.read(demoFile), timePoints);

    // Print the result
    demo.printResult(demo.getSolution(), System.out);
  }


  /**
   * @return the result of the dynamic simulation.
   */
  public MultiTable getSolution() {
    return solution;
  }


  /**
   * Prints the given time course simulation to the given output stream.
   *
   * @param solution
   * @param out
   */
  public void printResult(MultiTable solution, PrintStream out) {
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
