package com.petri.nets.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.petri.nets.helpers.common.ObjectDeepCopier;
import com.petri.nets.helpers.transformation.CustomGraphToJGraphXAdapterTransformer;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;
import com.petri.nets.model.Vertex;
import com.petri.nets.simulation.Simulator;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationProcessor {

    private static final String INFORMATION_MESSAGE_TITLE = "INFORMACJA";
    private static final String ERROR_MESSAGE_TITLE = "BŁĄD";

    private CustomGraph baseGraph;
    private JDialog jDialog;
    private Simulator simulator;
    private JScrollPane simulationPanel = new JScrollPane();
    private JButton startButton;
    private JButton stepButton;
    private JButton stopButton;

    public SimulationProcessor(CustomGraph baseGraph) {
        this.simulator = new Simulator(ObjectDeepCopier.getCopyOf(baseGraph));
        this.baseGraph = baseGraph;
        jDialog = new JDialog();
        jDialog.setTitle("Symulator");
        simulationPanel = createActiveJGraphComponentWithoutLayout(CustomGraphToJGraphXAdapterTransformer.transform(simulator.getGraph()));
        jDialog.add(simulationPanel);
        jDialog.setJMenuBar(createJMenuBar());
        jDialog.setModal(true);
        jDialog.setAlwaysOnTop(true);
        jDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jDialog.setMinimumSize(new Dimension(500, 500));
        jDialog.setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
        jDialog.setMaximumSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
        jDialog.setVisible(true);
    }

    private JScrollPane createActiveJGraphComponentWithoutLayout(JGraphXAdapter<Vertex, Edge> graphAdapter) {
        graphAdapter.setAllowDanglingEdges(false);
        graphAdapter.setCellsMovable(false);
        graphAdapter.refresh();
        mxGraphComponent mxGraphComponent = new mxGraphComponent(graphAdapter);
        mxGraphComponent.setConnectable(false); // disable possibility of new edges creation
        mxGraphComponent.refresh();
        return mxGraphComponent;
    }

    private JMenuBar createJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setLayout(new GridBagLayout());
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButtonActionPerformed(e);
            }
        });
        stepButton = new JButton("Krok");
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepButtonActionPerformed(e);
            }
        });
        stepButton.setEnabled(false);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopButtonActionPerformed(e);
            }
        });
        stopButton.setEnabled(false);
        jMenuBar.add(startButton);
        jMenuBar.add(stepButton);
        jMenuBar.add(stopButton);
        return jMenuBar;
    }

    private void stopButtonActionPerformed(ActionEvent e) {
        simulator = new Simulator(ObjectDeepCopier.getCopyOf(baseGraph));
        jDialog.remove(simulationPanel);
        simulationPanel = createActiveJGraphComponentWithoutLayout(CustomGraphToJGraphXAdapterTransformer.transform(simulator.getGraph()));
        jDialog.add(simulationPanel);
        jDialog.revalidate();
        startButton.setEnabled(true);
        stepButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    private void startButtonActionPerformed(ActionEvent e) {
        stepButtonActionPerformed(null);
        startButton.setEnabled(false);
        stepButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
        java.util.List<Transition> possibleSteps = simulator.getPossibleSteps();
        if (possibleSteps.size() == 0) {
            JOptionPane.showMessageDialog(jDialog, "Brak możliwych kroków!", INFORMATION_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
            stepButton.setEnabled(false);
            return;
        } else if (possibleSteps.size() == 1) {
            Transition transition = possibleSteps.get(0);
            simulator.takeStep(transition);
            JOptionPane.showMessageDialog(jDialog, "Wykonano przejście : " + transition.getName(), INFORMATION_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
            jDialog.remove(simulationPanel);
            simulationPanel = createActiveJGraphComponentWithoutLayout(CustomGraphToJGraphXAdapterTransformer.transform(simulator.getGraph()));
            jDialog.add(simulationPanel);
            jDialog.revalidate();
        } else {
            String optionChosen = JOptionPane.showInputDialog("Wybierz jedno z możliwych przejść: " + possibleSteps.toString()); // TODO Display options as buttons to chose
            Transition transition = baseGraph.getTransitionByName(possibleSteps, optionChosen);
            if (transition != null) {
                simulator.takeStep(transition);
                JOptionPane.showMessageDialog(jDialog, "Wykonano przejście : " + transition.getName(), INFORMATION_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
                jDialog.remove(simulationPanel);
                simulationPanel = createActiveJGraphComponentWithoutLayout(CustomGraphToJGraphXAdapterTransformer.transform(simulator.getGraph()));
                jDialog.add(simulationPanel);
                jDialog.revalidate();
            } else {
                if (optionChosen != null) {
                    JOptionPane.showMessageDialog(jDialog, "Wskazane przejście (" + optionChosen + ") nie jest w tej chwili możliwe", ERROR_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

