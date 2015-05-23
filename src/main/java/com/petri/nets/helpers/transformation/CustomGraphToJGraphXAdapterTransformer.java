package com.petri.nets.helpers.transformation;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Place;
import com.petri.nets.model.Vertex;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import java.util.HashMap;
import java.util.Map;

public class CustomGraphToJGraphXAdapterTransformer {

    public static JGraphXAdapter<Vertex, Edge> transform(CustomGraph graph) {
        ListenableGraph<Vertex, Edge> listenableGraph = createJGraphXFrom(graph);
        return createJGraphAdapter(listenableGraph);
    }

    private static ListenableGraph<Vertex, Edge> createJGraphXFrom(CustomGraph graph) {
        ListenableGraph<Vertex, Edge> listenableGraph = new ListenableDirectedWeightedGraph<>(Edge.class);
        addVerticesFromTo(graph, listenableGraph);
        addEdgesFromTo(graph, listenableGraph);
        return listenableGraph;
    }

    private static void addVerticesFromTo(CustomGraph graphFrom, ListenableGraph<Vertex, Edge> graphTo) {
        for (Vertex vertex : graphFrom.getVertices().values()) {
            graphTo.addVertex(vertex);
        }
    }

    private static void addEdgesFromTo(CustomGraph graphFrom, ListenableGraph<Vertex, Edge> graphTo) {
        for (Edge edge : graphFrom.getEdges().values()) {
            graphTo.addEdge(graphFrom.getVertex(edge.getSourceId()), graphFrom.getVertex(edge.getDestinationId()), edge);
        }
    }

    private static JGraphXAdapter<Vertex, Edge> createJGraphAdapter(ListenableGraph<Vertex, Edge> listenableGraph) {
        JGraphXAdapter graphAdapter = new JGraphXAdapter<>(listenableGraph);
        setVerticesProperties(graphAdapter);
        setEdgesProperties(graphAdapter);
        return graphAdapter;
    }

    private static void setEdgesProperties(JGraphXAdapter<Vertex, Edge> graphAdapter) {
        for (mxICell currentEdgeCell : graphAdapter.getEdgeToCellMap().values()) {
            currentEdgeCell.setStyle("labelBackgroundColor=#D6D9DC;editable=false");
        }
    }

    private static void setVerticesProperties(JGraphXAdapter<Vertex, Edge> graphAdapter) {
        mxStylesheet stylesheet = graphAdapter.getStylesheet();
        Map<String, Object> style = new HashMap<>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        stylesheet.putCellStyle("ROUNDED", style);
        graphAdapter.setStylesheet(stylesheet);

        for (Map.Entry<Vertex, mxICell> vertex : graphAdapter.getVertexToCellMap().entrySet()) {
            Vertex currentVertex = vertex.getKey();
            mxICell currentVertexCell = vertex.getValue();
            currentVertexCell.setGeometry(new mxGeometry(currentVertex.getX(), currentVertex.getY(), currentVertex.getWidth(), currentVertex.getHeight()));
            if (currentVertex instanceof Place) {
                currentVertexCell.setStyle("ROUNDED;fillColor=yellow;editable=false");
            } else {
                currentVertexCell.setStyle("fillColor=white;editable=false");
            }
        }
    }
}
