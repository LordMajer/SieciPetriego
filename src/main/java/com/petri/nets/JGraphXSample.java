package com.petri.nets;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.CustomGraphInitializer;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Vertex;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import javax.swing.*;

public class JGraphXSample {

    private static void createAndShowGui() {
        JFrame frame = new JFrame("DemoGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ListenableGraph<Vertex, Edge> g = buildGraph();
        JGraphXAdapter<Vertex, Edge> graphAdapter = new JGraphXAdapter<>(g);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        frame.add(new mxGraphComponent(graphAdapter));

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    public static ListenableGraph<Vertex, Edge> buildGraph() {
        ListenableDirectedWeightedGraph<Vertex, Edge> g =
                new ListenableDirectedWeightedGraph<>(Edge.class);

        CustomGraph graph = new CustomGraph();
        CustomGraphInitializer.initialize(graph);

        for (Vertex vertex : graph.getVertices().values()) {
            g.addVertex(vertex);
        }

        for (Edge edge : graph.getEdges().values()) {
            g.addEdge(graph.getVertex(edge.getSourceId()), graph.getVertex(edge.getDestinationId()), edge);
        }

        return g;
    }
}