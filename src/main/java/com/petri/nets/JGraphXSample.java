package com.petri.nets;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import com.petri.nets.model.*;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import javax.swing.*;
import java.util.Hashtable;
import java.util.Map;

public class JGraphXSample {

    private static void createAndShowGui() {
        JFrame frame = new JFrame("DemoGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ListenableGraph<Vertex, Edge> g = buildGraph();
        JGraphXAdapter<Vertex, Edge> graphAdapter = new JGraphXAdapter<>(g);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        mxStylesheet stylesheet = graphAdapter.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        stylesheet.putCellStyle("ROUNDED", style);
        graphAdapter.setStylesheet(stylesheet);

        for (Map.Entry<Vertex, mxICell> vertex : graphAdapter.getVertexToCellMap().entrySet()) {
            Vertex currentVertex = vertex.getKey();
            mxICell currentVertexCell = vertex.getValue();
            currentVertexCell.setGeometry(new mxGeometry(currentVertex.getX(), currentVertex.getY(), currentVertex.getWidth(), currentVertex.getHeight()));
            if (currentVertex instanceof Place) {
                currentVertexCell.setStyle("ROUNDED");
            }
        }

        mxGraphComponent mxGraphComponent = new mxGraphComponent(graphAdapter);
        mxGraphComponent.refresh(); // to do the changes visible

        frame.add(mxGraphComponent);


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