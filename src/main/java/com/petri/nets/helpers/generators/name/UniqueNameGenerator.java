package com.petri.nets.helpers.generators.name;

import com.petri.nets.helpers.VertexType;

public interface UniqueNameGenerator {

    String getNext(VertexType vertexType);
}
