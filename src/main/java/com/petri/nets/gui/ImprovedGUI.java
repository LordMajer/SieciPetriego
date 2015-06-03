package com.petri.nets.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.petri.nets.algorithms.CoverageGraph;
import com.petri.nets.algorithms.CoverageTree;
import com.petri.nets.algorithms.ReachabilityGraph;
import com.petri.nets.algorithms.properties.*;
import com.petri.nets.archive.GraphReader;
import com.petri.nets.archive.GraphWriter;
import com.petri.nets.helpers.VertexType;
import com.petri.nets.helpers.common.CommonOperations;
import com.petri.nets.helpers.common.PointToPositionTransformer;
import com.petri.nets.helpers.transformation.CustomGraphToJGraphXAdapterTransformer;
import com.petri.nets.model.*;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class ImprovedGUI extends javax.swing.JFrame {

    private static final int DEL_KEY_CODE = 127;
    public static final String ERROR_MESSAGE_TITLE = "BŁĄD";
    public static final String INFORMATION_MESSAGE_TITLE = "INFORMACJA";
    private static int PLACE_ADDING_MASK = MouseEvent.SHIFT_DOWN_MASK;
    private static int TRANSITION_ADDING_MASK = MouseEvent.SHIFT_DOWN_MASK;

    private CustomGraph graphModel;
    private JGraphXAdapter<Vertex, Edge> graphAdapter;
    private JScrollPane mainGraphScrollPane = new JScrollPane();

    public ImprovedGUI() {
        initComponents();
        setLayout(new BorderLayout()); // Żeby się poprawnie wyświetlało menu i panel!!!!!!!!!!!
        graphModel = new CustomGraph();
        CustomGraphInitializer.initialize(graphModel);
        displayGraph(graphModel);
        this.add(mainGraphScrollPane);
    }

    // Sztuczka z usunięciem i ponownym dodaniem panelu, inaczej nie wyświetla go ponownie po modyfikacji i zostaje stary widok
    private void displayGraph(CustomGraph customGraph) {
        this.remove(mainGraphScrollPane);
        graphAdapter = CustomGraphToJGraphXAdapterTransformer.transform(customGraph);
        graphAdapter.setAllowDanglingEdges(false); // Zablokowanie możliwości przestawiania krawędzi po dodaniu (można je tylko usunąć)
        graphAdapter.setEdgeLabelsMovable(false); // Zablokowanie możliwości przesuwania etykiet krawędzi
        graphAdapter.refresh(); // Żeby scrollbar zadziałał
        mainGraphScrollPane = createMainJGraphComponent(graphAdapter);
        this.add(mainGraphScrollPane);
        this.revalidate();
    }

    private JScrollPane createMainJGraphComponent(final JGraphXAdapter<Vertex, Edge> graphAdapter) {
        final mxGraphComponent mxGraphComponent = new mxGraphComponent(graphAdapter);
        mxGraphComponent.setConnectable(true); // disable possibility of new edges creation
        mxGraphComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Object[] cells = graphAdapter.getSelectionCells();
                if (cells.length > 0 && e.getKeyCode() == DEL_KEY_CODE) {
                    removeVertexButtonActionPerformed(null);
                }
            }
        });
        mxGraphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    EditElementItemActionPerformed(null);
                }
            }
        });
        mxGraphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                revalidateModelVertexPosition(graphAdapter);
                int modifiersEx = e.getModifiersEx();
                if (SwingUtilities.isLeftMouseButton(e) && (modifiersEx & PLACE_ADDING_MASK) == PLACE_ADDING_MASK) {
                    graphModel.addVertex(new Place(graphModel.getNewID(), graphModel.getNewName(VertexType.PLACE), PointToPositionTransformer.getVertexMiddlePointPosition(e.getPoint())));
                    displayGraph(graphModel);
                } else if (SwingUtilities.isRightMouseButton(e) && (modifiersEx & TRANSITION_ADDING_MASK) == TRANSITION_ADDING_MASK) {
                    graphModel.addVertex(new Transition(graphModel.getNewID(), graphModel.getNewName(VertexType.TRANSITION), PointToPositionTransformer.getVertexMiddlePointPosition(e.getPoint())));
                    displayGraph(graphModel);
                }
            }
        });
        mxGraphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object o, mxEventObject mxEventObject) {
                revalidateModelVertexPosition(graphAdapter);
                Vertex sourceVertex = null;
                Vertex destinationVertex = null;
                mxCell mxCell = (mxCell) mxEventObject.getProperty("cell");
                if (mxCell.getSource() != null) {
                    sourceVertex = (Vertex) mxCell.getSource().getValue();
                }
                if (mxCell.getTarget() != null) {
                    destinationVertex = (Vertex) mxCell.getTarget().getValue();
                }
                if (sourceVertex != null && destinationVertex != null && CommonOperations.canBeConnected(sourceVertex, destinationVertex)) {
                    graphModel.addEdge(new Edge(sourceVertex.getID(), destinationVertex.getID()));
                    displayGraph(graphModel);
                } else {
                    System.out.println("Disconnect");
                    mxCell.removeFromParent(); // usuwanie krawędzi przy niepoprawnym połączeniu wierzchołków
                }
            }
        });
        mxGraphComponent.refresh(); // to do the changes visible
        return mxGraphComponent;
    }

    private void revalidateModelVertexPosition(JGraphXAdapter<Vertex, Edge> graphAdapter) {
        for (Map.Entry<Vertex, mxICell> vertex : graphAdapter.getVertexToCellMap().entrySet()) {
            Vertex currentVertex = vertex.getKey();
            mxICell currentVertexCell = vertex.getValue();
            mxGeometry currentVertexCellGeometry = currentVertexCell.getGeometry();
            currentVertex.setHeight(currentVertexCellGeometry.getHeight());
            currentVertex.setWidth(currentVertexCellGeometry.getWidth());
            currentVertex.setX(currentVertexCellGeometry.getX());
            currentVertex.setY(currentVertexCellGeometry.getY());
        }
    }

    private void removeVertexButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeVertexButtonActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        Object[] cells = graphAdapter.getSelectionCells();
        if (cells.length < 1) {
            JOptionPane.showMessageDialog(this, "Najpierw zaznacz element, który chcesz usunąć!!!!", ERROR_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < cells.length; i++) {
            Object obj = ((mxCell) cells[i]).getValue();
            if (obj instanceof Vertex) {
                Vertex vertex = (Vertex) obj;
                graphModel.removeVertex(vertex);
            } else if (obj instanceof Edge) {
                Edge edge = (Edge) obj;
                graphModel.removeEdge(edge);
            }
            displayGraph(graphModel);
        }
    }//GEN-LAST:event_removeVertexButtonActionPerformed

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        modelMenu = new javax.swing.JMenu();
        newModelItem = new javax.swing.JMenuItem();
        loadModelItem = new javax.swing.JMenuItem();
        saveModelItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        isPriorityNetItem = new javax.swing.JCheckBoxMenuItem();
        editMenu = new javax.swing.JMenu();
        addPlaceMenu = new javax.swing.JMenuItem();
        addTransitionItem = new javax.swing.JMenuItem();
        addEdgeItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        EditElementItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        deleteElementItem = new javax.swing.JMenuItem();
        netRepresentationMenu = new javax.swing.JMenu();
        reachabilityGraphItem = new javax.swing.JMenuItem();
        coverageGraphItem = new javax.swing.JMenuItem();
        coverageTreeItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        inputMatrixItem = new javax.swing.JMenuItem();
        outputMatrixItem = new javax.swing.JMenuItem();
        incidenceMatrixItem = new javax.swing.JMenuItem();
        simulationMenu = new javax.swing.JMenu();
        startSimulationItem = new javax.swing.JMenuItem();
        netPropertiesMenu = new javax.swing.JMenu();
        safenessItem = new javax.swing.JMenuItem();
        boudednessItem = new javax.swing.JMenuItem();
        reversibilityItem = new javax.swing.JMenuItem();
        conservationItem = new javax.swing.JMenuItem();
        placeLiveness = new javax.swing.JMenuItem();
        transitionLivenessItem = new javax.swing.JMenuItem();
        netLivenessItem = new javax.swing.JMenuItem();
        aboutProgramMenu = new javax.swing.JMenu();
        aboutAuthorsItem = new javax.swing.JMenuItem();
        shortcutsItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sieci Petriego");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(500, 500));

        modelMenu.setText("Sieć");
        modelMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                modelMenuMenuSelected(evt);
            }
        });

        newModelItem.setText("Nowa");
        newModelItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelItemActionPerformed(evt);
            }
        });
        modelMenu.add(newModelItem);

        loadModelItem.setText("Wczytaj");
        loadModelItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadModelItemActionPerformed(evt);
            }
        });
        modelMenu.add(loadModelItem);

        saveModelItem.setText("Zapisz");
        saveModelItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveModelItemActionPerformed(evt);
            }
        });
        modelMenu.add(saveModelItem);
        modelMenu.add(jSeparator4);

        isPriorityNetItem.setText("Sieć priorytetowa");
        isPriorityNetItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isPriorityNetItemActionPerformed(evt);
            }
        });
        modelMenu.add(isPriorityNetItem);

        jMenuBar1.add(modelMenu);

        editMenu.setText("Edytuj");

        addPlaceMenu.setText("Dodaj miejsce");
        addPlaceMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlaceMenuActionPerformed(evt);
            }
        });
        editMenu.add(addPlaceMenu);

        addTransitionItem.setText("Dodaj przejście");
        addTransitionItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTransitionItemActionPerformed(evt);
            }
        });
        editMenu.add(addTransitionItem);

        addEdgeItem.setText("Dodaj krawędź");
        addEdgeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEdgeItemActionPerformed(evt);
            }
        });
        editMenu.add(addEdgeItem);
        editMenu.add(jSeparator1);

        EditElementItem.setText("Edytuj");
        EditElementItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditElementItemActionPerformed(evt);
            }
        });
        editMenu.add(EditElementItem);
        editMenu.add(jSeparator3);

        deleteElementItem.setText("Usuń");
        deleteElementItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteElementItemActionPerformed(evt);
            }
        });
        editMenu.add(deleteElementItem);

        jMenuBar1.add(editMenu);

        netRepresentationMenu.setText("Reprezentacja");

        reachabilityGraphItem.setText("Graf osiągalności");
        reachabilityGraphItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reachabilityGraphItemActionPerformed(evt);
            }
        });
        netRepresentationMenu.add(reachabilityGraphItem);

        coverageGraphItem.setText("Graf pokrycia");
        coverageGraphItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverageGraphItemActionPerformed(evt);
            }
        });
        netRepresentationMenu.add(coverageGraphItem);

        coverageTreeItem.setText("Drzewo pokrycia");
        coverageTreeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverageTreeItemActionPerformed(evt);
            }
        });
        netRepresentationMenu.add(coverageTreeItem);
        netRepresentationMenu.add(jSeparator2);

        inputMatrixItem.setText("Macierz wejść");
        inputMatrixItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputMatrixItemActionPerformed(evt);
            }
        });
        netRepresentationMenu.add(inputMatrixItem);

        outputMatrixItem.setText("Macierz wyjść");
        outputMatrixItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputMatrixItemActionPerformed(evt);
            }
        });
        netRepresentationMenu.add(outputMatrixItem);

        incidenceMatrixItem.setText("Macierz incydencji");
        incidenceMatrixItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incidenceMatrixItemActionPerformed(evt);
            }
        });
        netRepresentationMenu.add(incidenceMatrixItem);

        jMenuBar1.add(netRepresentationMenu);

        simulationMenu.setText("Symulacja");

        startSimulationItem.setText("Rozpocznij");
        startSimulationItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationItemActionPerformed(evt);
            }
        });
        simulationMenu.add(startSimulationItem);

        jMenuBar1.add(simulationMenu);

        netPropertiesMenu.setText("Właściwości");

        safenessItem.setText("Bezpieczeństwo sieci");
        safenessItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                safenessItemActionPerformed(evt);
            }
        });
        netPropertiesMenu.add(safenessItem);

        boudednessItem.setText("K-ograniczoność miejsc");
        boudednessItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boudednessItemActionPerformed(evt);
            }
        });
        netPropertiesMenu.add(boudednessItem);

        reversibilityItem.setText("Odwracalność sieci");
        reversibilityItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reversibilityItemActionPerformed(evt);
            }
        });
        netPropertiesMenu.add(reversibilityItem);

        conservationItem.setText("Zachowawczość");
        conservationItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conservationItemActionPerformed(evt);
            }
        });
        netPropertiesMenu.add(conservationItem);

        placeLiveness.setText("Żywotność miejsc");
        placeLiveness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placeLivenessActionPerformed(evt);
            }
        });
        netPropertiesMenu.add(placeLiveness);

        transitionLivenessItem.setText("Żywotność przejść");
        transitionLivenessItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transitionLivenessItemActionPerformed(evt);
            }
        });
        netPropertiesMenu.add(transitionLivenessItem);

        netLivenessItem.setText("Żywotność sieci");
        netLivenessItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netLivenessItemActionPerformed(evt);
            }
        });
        netPropertiesMenu.add(netLivenessItem);

        jMenuBar1.add(netPropertiesMenu);

        aboutProgramMenu.setText("O programie");

        aboutAuthorsItem.setText("Autorzy");
        aboutAuthorsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutAuthorsItemActionPerformed(evt);
            }
        });
        aboutProgramMenu.add(aboutAuthorsItem);

        shortcutsItem.setText("Udogodnienia");
        shortcutsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shortcutsItemActionPerformed(evt);
            }
        });
        aboutProgramMenu.add(shortcutsItem);

        jMenuBar1.add(aboutProgramMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 930, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 552, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newModelItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelItemActionPerformed
        graphModel = new CustomGraph();
        displayGraph(graphModel);
    }//GEN-LAST:event_newModelItemActionPerformed

    private void loadModelItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadModelItemActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        graphModel = GraphReader.loadGraph(graphModel);
        displayGraph(graphModel);
    }//GEN-LAST:event_loadModelItemActionPerformed

    private void boudednessItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boudednessItemActionPerformed
        JOptionPane.showMessageDialog(null, new Boundedness(new CoverageGraph(graphModel).build()).calculate());
    }//GEN-LAST:event_boudednessItemActionPerformed

    private void conservationItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conservationItemActionPerformed
        JOptionPane.showMessageDialog(null, new Conservation(new CoverageGraph(graphModel).build()).calculate());
    }//GEN-LAST:event_conservationItemActionPerformed

    private void saveModelItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveModelItemActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        GraphWriter.saveGraph(graphModel);
    }//GEN-LAST:event_saveModelItemActionPerformed

    private void isPriorityNetItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isPriorityNetItemActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        graphModel.setPriority(isPriorityNetItem.isSelected());
        displayGraph(graphModel);
    }//GEN-LAST:event_isPriorityNetItemActionPerformed

    private void addPlaceMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlaceMenuActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        Place place = new Place(graphModel.getNewID(), graphModel.getNewName(Place.getVertexType()));
        graphModel.addVertex(place);
        displayGraph(graphModel);
    }//GEN-LAST:event_addPlaceMenuActionPerformed

    private void addTransitionItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTransitionItemActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        Transition przejscie = new Transition(graphModel.getNewID(), graphModel.getNewName(Transition.getVertexType()));
        graphModel.addVertex(przejscie);
        displayGraph(graphModel);
    }//GEN-LAST:event_addTransitionItemActionPerformed

    private void addEdgeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEdgeItemActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        Object[] cells = graphAdapter.getSelectionCells();
        if (cells.length != 2) {
            JOptionPane.showMessageDialog(this, "Aby dodać krawędź należy zaznaczyć dokładnie 2 wierzchołki!", ERROR_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        Vertex sourceVertex = (Vertex) ((mxCell) cells[0]).getValue();
        Vertex destinationVertex = (Vertex) ((mxCell) cells[1]).getValue();
        
        // Sprawdzenie czy wierzchołki są tych samych typów
        if (CommonOperations.canBeConnected(sourceVertex, destinationVertex)) {
            Edge edge = new Edge(sourceVertex.getID(), destinationVertex.getID());
            graphModel.addEdge(edge);
            displayGraph(graphModel);
        }
    }//GEN-LAST:event_addEdgeItemActionPerformed

    protected void EditElementItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditElementItemActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        Object[] selectedElements = graphAdapter.getSelectionCells();
        if (selectedElements.length == 1) {
            Object selectedElement = ((mxCell) selectedElements[0]).getValue();
            if (selectedElement instanceof Place) {
                editPlace(selectedElement);
            } else if (selectedElement instanceof Transition) {
                editTransition(selectedElement);
            } else if (selectedElement instanceof Edge) {
                editEdge(selectedElement);
            } else {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas zaznaczania!", ERROR_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        } else if (selectedElements.length == 2 && selectedElements[0] instanceof Vertex && selectedElements[1] instanceof Vertex) {
            editEdge(selectedElements);
        } else {
            JOptionPane.showMessageDialog(this, "Aby edytować elementy grafu należy zaznaczyć pojedynczy element lub dwa wierzchołki w celu edycji krawędzi pomiędzy nimi!", ERROR_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_EditElementItemActionPerformed

    private void deleteElementItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteElementItemActionPerformed
        revalidateModelVertexPosition(graphAdapter);
        Object[] cells = graphAdapter.getSelectionCells();
        if (cells.length < 1) {
            JOptionPane.showMessageDialog(this, "Najpierw zaznacz element, który chcesz usunąć!!!!", ERROR_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < cells.length; i++) {
            Object obj = ((mxCell) cells[i]).getValue();
            if (obj instanceof Vertex) {
                Vertex vertex = (Vertex) obj;
                graphModel.removeVertex(vertex);
            } else if (obj instanceof Edge) {
                Edge edge = (Edge) obj;
                graphModel.removeEdge(edge);
            }
            displayGraph(graphModel);
        }
    }//GEN-LAST:event_deleteElementItemActionPerformed

    private void modelMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_modelMenuMenuSelected
        isPriorityNetItem.setState(graphModel.isPriority());
    }//GEN-LAST:event_modelMenuMenuSelected

    private void reachabilityGraphItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reachabilityGraphItemActionPerformed
        createJDialog("Graf osiągalności", createActiveJGraphComponentWithLayout(CustomGraphToJGraphXAdapterTransformer.transform(new ReachabilityGraph(graphModel).build())));
    }//GEN-LAST:event_reachabilityGraphItemActionPerformed

    private void coverageGraphItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverageGraphItemActionPerformed
        createJDialog("Graf pokrycia", createActiveJGraphComponentWithLayout(CustomGraphToJGraphXAdapterTransformer.transform(new CoverageGraph(graphModel).build())));
    }//GEN-LAST:event_coverageGraphItemActionPerformed

    private void coverageTreeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverageTreeItemActionPerformed
        createJDialog("Drzewo pokrycia", createActiveJGraphComponentWithLayout(CustomGraphToJGraphXAdapterTransformer.transform(new CoverageTree(graphModel).build())));
    }//GEN-LAST:event_coverageTreeItemActionPerformed

    private void inputMatrixItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputMatrixItemActionPerformed
        MatrixCreator matrixCreator = new MatrixCreator(graphModel);
        createJDialog("Macierz wejść", createJScrollPane(matrixCreator.generateOutputMatrix()));
    }//GEN-LAST:event_inputMatrixItemActionPerformed

    private void outputMatrixItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputMatrixItemActionPerformed
        MatrixCreator matrixCreator = new MatrixCreator(graphModel);
        createJDialog("Macierz wyjść", createJScrollPane(matrixCreator.generateInputMatrix()));
    }//GEN-LAST:event_outputMatrixItemActionPerformed

    private void incidenceMatrixItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incidenceMatrixItemActionPerformed
        MatrixCreator matrixCreator = new MatrixCreator(graphModel);
        createJDialog("Macierz incydencji", createJScrollPane(matrixCreator.generateIncidenceMatrix()));
    }//GEN-LAST:event_incidenceMatrixItemActionPerformed

    private void startSimulationItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationItemActionPerformed
        //SimulationDialog SimulationDialog = new SimulationDialog(this, true, graphModel);
        SimulationProcessor SimulationDialog = new SimulationProcessor(graphModel);
    }//GEN-LAST:event_startSimulationItemActionPerformed

    private void safenessItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_safenessItemActionPerformed
        JOptionPane.showMessageDialog(null, new Safeness(new CoverageGraph(graphModel).build()).calculate());
    }//GEN-LAST:event_safenessItemActionPerformed

    private void aboutAuthorsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutAuthorsItemActionPerformed
        JOptionPane.showMessageDialog(null, "Autorzy programu:\nMaciej Bruno-Kamiński\nMateusz Juraszek\nMateusz Maj", INFORMATION_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutAuthorsItemActionPerformed

    private void shortcutsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shortcutsItemActionPerformed
        JOptionPane.showMessageDialog(null, "SHIFT + lewy przycisk myszy : stworzenie miejsca\n"
                + "SHIFT + prawy przycisk myszy : stworzenie przejścia\n"
                + "DEL : usunięcie aktywnych elementów\n"
                + "Dwukrotne naciśnięcie lewego przycisku myszy : edycja aktywnego elementu\n"
                + "Dodawanie krawędzi poprzez przeciągnięcie jej z jednego wierzchołka do drugiego", INFORMATION_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_shortcutsItemActionPerformed

    private void netLivenessItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netLivenessItemActionPerformed
        JOptionPane.showMessageDialog(null, new NetLiveness(graphModel).calculate());
    }//GEN-LAST:event_netLivenessItemActionPerformed

    private void transitionLivenessItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transitionLivenessItemActionPerformed
        List<Transition> transitions = new ArrayList<>(graphModel.getTransitions().values());
        String[] transitionButtons = getTransitionButtons(transitions);
        int chosenOption = JOptionPane.showOptionDialog(this, "Wybierz jedno z możliwych przejść", "Wybór przejścia", JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, transitionButtons, transitionButtons[0]);
        if (chosenOption != JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, new TransitionLiveness(graphModel).calculate(transitions.get(chosenOption)));
        }
    }//GEN-LAST:event_transitionLivenessItemActionPerformed

    private void reversibilityItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reversibilityItemActionPerformed
        JOptionPane.showMessageDialog(null, new Reversibility(new CoverageGraph(graphModel).build()).calculate());
    }//GEN-LAST:event_reversibilityItemActionPerformed

    private void placeLivenessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placeLivenessActionPerformed
        List<Place> places = new ArrayList<>(graphModel.getPlaces().values());
        String[] placeButtons = getPlaceButtons(places);
        int chosenOption = JOptionPane.showOptionDialog(this, "Wybierz jedno z możliwych miejsc", "Wybór miejsca", JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, placeButtons, placeButtons[0]);
        if (chosenOption != JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, new PlaceLiveness(graphModel).calculate(places.get(chosenOption)));
        }
    }//GEN-LAST:event_placeLivenessActionPerformed

    private String[] getTransitionButtons(java.util.List<Transition> transitions) {
        String[] transitionsName = new String[transitions.size()];
        for (int i = 0; i < transitions.size(); i++) {
            transitionsName[i] = transitions.get(i).getName();
        }
        return transitionsName;
    }

    private String[] getPlaceButtons(java.util.List<Place> places) {
        String[] placeNames = new String[places.size()];
        for (int i = 0; i < places.size(); i++) {
            placeNames[i] = places.get(i).getName();
        }
        return placeNames;
    }

    private JDialog createJDialog(String title, JScrollPane pane) {
        JDialog jDialog = new JDialog();
        jDialog.setTitle(title);
        jDialog.add(pane);
        jDialog.setModal(true);
        jDialog.setAlwaysOnTop(true);
        jDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jDialog.setLocationByPlatform(true);
        jDialog.setMinimumSize(new Dimension(500, 500));
        jDialog.setMaximumSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
        jDialog.setVisible(true);
        return jDialog;
    }

    private JScrollPane createJScrollPane(Component component) {
        JPanel panel = new JPanel();
        panel.add(component);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        return new JScrollPane(panel);
    }

    private JScrollPane createActiveJGraphComponentWithLayout(JGraphXAdapter<Vertex, Edge> graphAdapter) {
        graphAdapter.setAllowDanglingEdges(false);
        graphAdapter.setCellsMovable(false);
        graphAdapter.setEdgeLabelsMovable(false);
        graphAdapter.setAllowDanglingEdges(false);
        mxGraphComponent mxGraphComponent = new mxGraphComponent(graphAdapter);
        mxGraphComponent.setEnabled(true);
        mxGraphComponent.setConnectable(false);
        mxGraphComponent.refresh();
        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        return mxGraphComponent;
    }

    private void editPlace(Object selectedPlace) {
        Map<String, Object> values = new HashMap<>();
        values.put("Object", selectedPlace);
        EditPlacePanel editPlacePanel = new EditPlacePanel(this, "Edycja miejsca", true, values);
        if (values.get("Status") != null && values.get("Status").equals("Ok")) {
            Place modelVertex = (Place) graphModel.getVertex(((Place) values.get("Object")).getID());
            Place changedVertex = (Place) values.get("ReturnObject");
            modelVertex.setName(changedVertex.getName());
            modelVertex.setTokenCount(changedVertex.getTokenCount());
            modelVertex.setCapacity(changedVertex.getCapacity());
        }
        displayGraph(graphModel);
    }

    private void editTransition(Object selectedTransition) {
        Map<String, Object> values = new HashMap<>();
        values.put("Object", selectedTransition);
        EditTransitionPanel editTransitionPanel = new EditTransitionPanel(this, "Edycja przejścia", true, values);
        if (values.get("Status") != null && values.get("Status").equals("Ok")) {
            Transition modelVertex = (Transition) graphModel.getVertex(((Transition) values.get("Object")).getID());
            Transition changedVertex = (Transition) values.get("ReturnObject");
            modelVertex.setName(changedVertex.getName());
            modelVertex.setPriority(changedVertex.getPriority());
        }
        displayGraph(graphModel);
    }

    private void editEdge(Object selectedEdge) {
        Map<String, Object> values = new HashMap<>();
        values.put("Object", selectedEdge);
        EditEdgePanel editEdgePanel = new EditEdgePanel(this, "Edycja krawędzi", true, values);
        if (values.get("Status") != null && values.get("Status").equals("Ok")) {
            Edge modelEdge = (Edge) graphModel.getEdge(((Edge) values.get("Object")).getKey());
            Edge changedEdge = (Edge) values.get("ReturnObject");
            modelEdge.setCapacity(changedEdge.getCapacity());
        }
        displayGraph(graphModel);
    }

    private void editEdge(Object[] selectedVertices) {
        Map<String, Object> values = new HashMap<>();
        Vertex sourceObject = (Vertex) ((mxCell) selectedVertices[0]).getValue();
        Vertex destinationObject = (Vertex) ((mxCell) selectedVertices[1]).getValue();
        Edge chosenEdge = new Edge(sourceObject.getID(), destinationObject.getID());
        Edge foundEdge = graphModel.getEdge(chosenEdge.getKey());
        if (foundEdge != null) {
            values.put("Object", foundEdge);
            EditEdgePanel editEdgePanel = new EditEdgePanel(this, "Edycja krawędzi", true, values);
            if (values.get("Status") != null && values.get("Status").equals("Ok")) {
                Edge modelEdge = graphModel.getEdge(((Edge) values.get("Object")).getKey());
                Edge changedVertex = (Edge) values.get("ReturnObject");
                modelEdge.setCapacity(changedVertex.getCapacity());
            }
            displayGraph(graphModel);
        } else {
            JOptionPane.showMessageDialog(this, "Brak krawedzi pomiędzy zaznaczonymi wierzchołkami!", ERROR_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImprovedGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImprovedGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImprovedGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImprovedGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ImprovedGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem EditElementItem;
    private javax.swing.JMenuItem aboutAuthorsItem;
    private javax.swing.JMenu aboutProgramMenu;
    private javax.swing.JMenuItem addEdgeItem;
    private javax.swing.JMenuItem addPlaceMenu;
    private javax.swing.JMenuItem addTransitionItem;
    private javax.swing.JMenuItem boudednessItem;
    private javax.swing.JMenuItem conservationItem;
    private javax.swing.JMenuItem coverageGraphItem;
    private javax.swing.JMenuItem coverageTreeItem;
    private javax.swing.JMenuItem deleteElementItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem incidenceMatrixItem;
    private javax.swing.JMenuItem inputMatrixItem;
    private javax.swing.JCheckBoxMenuItem isPriorityNetItem;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JMenuItem loadModelItem;
    private javax.swing.JMenu modelMenu;
    private javax.swing.JMenuItem netLivenessItem;
    private javax.swing.JMenu netPropertiesMenu;
    private javax.swing.JMenu netRepresentationMenu;
    private javax.swing.JMenuItem newModelItem;
    private javax.swing.JMenuItem outputMatrixItem;
    private javax.swing.JMenuItem placeLiveness;
    private javax.swing.JMenuItem reachabilityGraphItem;
    private javax.swing.JMenuItem reversibilityItem;
    private javax.swing.JMenuItem safenessItem;
    private javax.swing.JMenuItem saveModelItem;
    private javax.swing.JMenuItem shortcutsItem;
    private javax.swing.JMenu simulationMenu;
    private javax.swing.JMenuItem startSimulationItem;
    private javax.swing.JMenuItem transitionLivenessItem;
    // End of variables declaration//GEN-END:variables
}
