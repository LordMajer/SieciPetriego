package com.petri.nets.gui;

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
        startButton.setEnabled(false);
        stepButton.setEnabled(true);
        stopButton.setEnabled(true);
        stepButtonActionPerformed(null);
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
            String[] transitionButtons = getTransitionButtons(possibleSteps);
            int chosenOption = JOptionPane.showOptionDialog(jDialog, "Wybierz jedno z możliwych przejść", "Wybór przejścia", JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, transitionButtons, transitionButtons[0]);
            if (chosenOption != JOptionPane.CLOSED_OPTION) {
                Transition transition = possibleSteps.get(chosenOption);
                simulator.takeStep(transition);
                JOptionPane.showMessageDialog(jDialog, "Wykonano przejście : " + transition.getName(), INFORMATION_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
                jDialog.remove(simulationPanel);
                simulationPanel = createActiveJGraphComponentWithoutLayout(CustomGraphToJGraphXAdapterTransformer.transform(simulator.getGraph()));
                jDialog.add(simulationPanel);
                jDialog.revalidate();
            }
        }
    }

    private String[] getTransitionButtons(java.util.List<Transition> transitions) {
        String[] transitionsName = new String[transitions.size()];
        for (int i = 0; i < transitions.size(); i++) {
            transitionsName[i] = transitions.get(i).getName();
        }
        return transitionsName;
    }
}

