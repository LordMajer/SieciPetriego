package com.petri.nets.gui;

import com.mxgraph.swing.mxGraphComponent;
import com.petri.nets.helpers.common.ObjectDeepCopier;
import com.petri.nets.helpers.transformation.CustomGraphToJGraphXAdapterTransformer;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Vertex;
import com.petri.nets.simulation.Simulator;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.*;
import java.awt.*;

public class SimulationProcessor {

    private final Simulator simulator;

    public SimulationProcessor(CustomGraph graph) {
        this.simulator = new Simulator(ObjectDeepCopier.getCopyOf(graph));
        JDialog jdialog = new JDialog();
        jdialog.setTitle("Symulator Sieci Petriego");
        jdialog.add(createActiveJGraphComponentWithoutLayout(CustomGraphToJGraphXAdapterTransformer.transform(simulator.getGraph())));
        jdialog.setJMenuBar(createJMenuBar());
        jdialog.setModal(true);
        jdialog.setAlwaysOnTop(true);
        jdialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jdialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jdialog.setLocationByPlatform(true);
        jdialog.setMinimumSize(new Dimension(500, 500));
        jdialog.pack();
        jdialog.setVisible(true);
    }

    private JMenuBar createJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setLayout(new GridBagLayout());
        JButton startButton = new JButton("Start");
        JButton stepButton = new JButton("Krok");
        JButton stopButton = new JButton("Stop");
        jMenuBar.add(startButton);
        jMenuBar.add(stepButton);
        jMenuBar.add(stopButton);
        return jMenuBar;
    }

    private JScrollPane createActiveJGraphComponentWithoutLayout(JGraphXAdapter<Vertex, Edge> graphAdapter) {
        graphAdapter.setAllowDanglingEdges(false);
        mxGraphComponent mxGraphComponent = new mxGraphComponent(graphAdapter);
        mxGraphComponent.setConnectable(false); // disable possibility of new edges creation
        mxGraphComponent.refresh();
        return mxGraphComponent;
    }
}

