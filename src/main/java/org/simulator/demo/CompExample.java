/**
 *
 */
package org.simulator.demo;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.simulator.comp.CompSimulator;
import org.simulator.math.odes.MultiTable;

/**
 * @author Andreas Dr&auml;ger
 */
public class CompExample {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    double timeEnd = 100d, stepSize = 0.1d;
    CompSimulator compSimulator = new CompSimulator(new File(args[0]));
    MultiTable solution = compSimulator.solve(timeEnd, stepSize);
    // Display simulation result to the user
    JScrollPane resultDisplay = new JScrollPane(new JTable(solution));
    resultDisplay.setPreferredSize(new Dimension(400, 400));
    JOptionPane.showMessageDialog(null, resultDisplay, "Comp Results", JOptionPane.INFORMATION_MESSAGE);
  }
}
