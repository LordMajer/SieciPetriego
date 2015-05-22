package com.petri.nets.helpers.common;

import com.petri.nets.model.Position;
import com.petri.nets.model.Vertex;

import java.awt.*;

public class PointToPositionTransformer {

    public static Position getVertexMiddlePointPosition(Point point) {
        return new Position(point.getX() - Vertex.DEFAULT_WIDTH / 2, point.getY() - Vertex.DEFAULT_HEIGHT / 2);
    }
}
