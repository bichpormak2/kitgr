package com.bichpormak.idz_1;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PetersenGraph {

    public static void main(String[] args) {
        Graph graph = createDirectedPetersenGraph();
        visualizeGraph(graph);

        int[][] adjacencyMatrix = generateAdjacencyMatrix(graph);
        visualizeAdjacencyMatrix(adjacencyMatrix, "Матрица смежности графа Петерсена");
        applyMultiplications(adjacencyMatrix);
    }

    public static Graph createDirectedPetersenGraph() {
        Graph baseGraph = createBasePetersenGraph();
        Graph directedGraph = new SingleGraph("DirectedPetersen", false, true); // false - не мультиграф, true - ориентированный

        baseGraph.nodes().forEach(node -> {
            Node newNode = directedGraph.addNode(node.getId());
            newNode.setAttribute("ui.label", node.getId());
        });

        // Случайная ориентация рёбер
        Random rand = new Random();
        baseGraph.edges().forEach(edge -> {
            String source = edge.getSourceNode().getId();
            String target = edge.getTargetNode().getId();

            if(rand.nextBoolean()) {
                directedGraph.addEdge(edge.getId(), source, target, true);
            } else {
                directedGraph.addEdge(edge.getId(), target, source, true);
            }
        });

        return directedGraph;
    }

    private static Graph createBasePetersenGraph() {
        Graph graph = new SingleGraph("BasePetersen");

        // Добавляем узлы
        for(int i = 1; i <= 10; i++) {
            Node node = graph.addNode(String.valueOf(i));
            node.setAttribute("ui.label", i);
        }

        // Внешний 5-угольник
        int[][] outerConnections = {{1,2}, {2,3}, {3,4}, {4,5}, {5,1}};
        for(int[] pair : outerConnections) {
            graph.addEdge(pair[0]+"-"+pair[1], String.valueOf(pair[0]), String.valueOf(pair[1]));
        }

        // Внутренняя 5-звезда
        int[][] innerConnections = {{6,8}, {6,9}, {7,9}, {7,10}, {8,10}};
        for(int[] pair : innerConnections) {
            graph.addEdge(pair[0]+"-"+pair[1], String.valueOf(pair[0]), String.valueOf(pair[1]));
        }

        // Соединения между слоями
        for(int i = 1; i <= 5; i++) {
            graph.addEdge(i+"-"+(i+5), String.valueOf(i), String.valueOf(i+5));
        }

        return graph;
    }

    public static void visualizeGraph(Graph graph) {
        System.setProperty("org.graphstream.ui", "swing");
        graph.setAttribute("ui.stylesheet",
                "node { fill-color: #FFFFFF; size: 50px; text-size: 30; }" +
                        "edge { fill-color: #77DDE7; arrow-size: 30px, 10px; }");

        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewer.addDefaultView(true);
    }

    public static int[][] generateAdjacencyMatrix(Graph graph) {
        List<Node> nodes = graph.nodes()
                .sorted(Comparator.comparingInt(n -> Integer.parseInt(n.getId())))
                .collect(Collectors.toList());

        int size = nodes.size();
        int[][] matrix = new int[size][size];

        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            indexMap.put(nodes.get(i).getId(), i);
        }

        for (org.graphstream.graph.Edge edge : graph.edges().collect(Collectors.toList())) {
            int source = indexMap.get(edge.getSourceNode().getId());
            int target = indexMap.get(edge.getTargetNode().getId());
            matrix[source][target] = 1;
        }

        return matrix;
    }

    public static void visualizeAdjacencyMatrix(int[][] matrix, String title) {

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        DefaultTableModel model = new DefaultTableModel(matrix.length, matrix.length) {
            @Override
            public Object getValueAt(int row, int column) {
                return matrix[row][column];
            }
        };

        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                int value = (int) getModel().getValueAt(row, column);

                String text = value == Integer.MAX_VALUE ? "∞" :
                        value == Integer.MIN_VALUE ? "-∞" :
                                String.valueOf(value);

                ((JLabel) c).setText(text);
                c.setBackground(value != 0 ? new Color(207, 177, 157) : Color.WHITE);
                return c;
            }
        };

        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));

        List<String> nodeIds = new ArrayList<>();
        for (int i = 1; i <= 10; i++) nodeIds.add(String.valueOf(i));
        Collections.sort(nodeIds);

        table.setTableHeader(new JTableHeader(table.getColumnModel()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 40);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public static void applyMultiplications(int[][] adjacencyMatrix) {
        Multiply standard = new MultiplyStandard();
        int[][] standardResult = standard.multiply(adjacencyMatrix, adjacencyMatrix);
        visualizeAdjacencyMatrix(standardResult, "Стандартное умножение");

        Multiply bool = new MultiplyBoolean();
        int[][] boolResult = bool.multiply(adjacencyMatrix, adjacencyMatrix);
        visualizeAdjacencyMatrix(boolResult, "Логическое умножение");

        Multiply tropicalMin = new MultiplyTropical(Math::min);
        int[][] tropMinResult = tropicalMin.multiply(adjacencyMatrix, adjacencyMatrix);
        visualizeAdjacencyMatrix(tropMinResult, "Тропическое min-plus");

        Multiply tropicalMax = new MultiplyTropical(Math::max);
        int[][] tropMaxResult = tropicalMax.multiply(adjacencyMatrix, adjacencyMatrix);
        visualizeAdjacencyMatrix(tropMaxResult, "Тропическое max-plus");
    }

}
