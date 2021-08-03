/**
 *
 */
package org.simulator.demo;

import java.io.File;

import fern.network.Network;
import fern.network.sbml.SBMLNetwork;
import fern.simulation.Simulator;
import fern.simulation.algorithm.GillespieEnhanced;
import fern.simulation.observer.AmountIntervalObserver;
import fern.simulation.observer.Observer;
import fern.tools.NetworkTools;

/**
 * @author Andreas Dr&auml;ger
 *
 */
public class StochasticSimulation {

  public static void main(String args[]) throws Exception {
    Network net = NetworkTools.loadNetwork(new File(args[0]));
    Simulator sim = new GillespieEnhanced(net);
    ((SBMLNetwork) net).registerEvents(sim);
    Observer observer = new AmountIntervalObserver(sim, 0, fill(net.getNumSpecies()));
    sim.addObserver(observer);
    sim.start(5d); // end time
    observer.print();
  }


  /**
   * Helper function to fill an array with increasing values.
   * @param numSpecies
   * @return
   */
  private static int[] fill(int numSpecies) {
    int[] array = new int[numSpecies];
    for (int i = 0; i < numSpecies; i++) {
      array[i] = i;
    }
    return array;
  }
}
