/**
 *
 */
package org.simulator.demo;

import java.io.File;
import java.io.PrintWriter;

import fern.network.Network;
import fern.network.sbml.SBMLNetwork;
import fern.simulation.Simulator;
import fern.simulation.algorithm.GillespieEnhanced;
import fern.simulation.observer.AmountIntervalObserver;
import fern.simulation.observer.Observer;
import fern.tools.NetworkTools;
import fern.tools.NumberTools;

/**
 * @author Andreas Dr&auml;ger
 *
 */
public class StochasticSimulation {

  public static void main(String args[]) throws Exception {
    Network net = NetworkTools.loadNetwork(new File(args[0]));
    Simulator sim = new GillespieEnhanced(net);
    ((SBMLNetwork) net).registerEvents(sim);
    String[] species = NetworkTools.getSpeciesNames(sim.getNet(),
      NumberTools.getNumbersTo(sim.getNet().getNumSpecies() - 1));
    Observer observer = new AmountIntervalObserver(sim, 0.1d, 5, species);
    sim.addObserver(observer);
    sim.start(5d); // end time
    observer.setPrintWriter(new PrintWriter(System.out));
    observer.print();
  }

}
