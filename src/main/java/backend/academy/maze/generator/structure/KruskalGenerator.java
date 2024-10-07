package backend.academy.maze.generator.structure;

import backend.academy.maze.algorithm.DSU;
import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Edge;
import backend.academy.maze.utility.CellOperations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates a maze using Kruskal's algorithm.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Kruskal%27s_algorithm">Kruskal's algorithm on wiki</a>
 */
public class KruskalGenerator implements Generator {
    @Override
    public void generate(MazeView mazeView, GeneratorSettings settings) {
        int viewHeight = mazeView.viewHeight();
        int viewWidth = mazeView.viewWidth();

        // Collecting all edges like (i, j) -> (i + 1, j) and (i, j) -> (i, j + 1)
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < viewHeight; i++) {
            for (int j = 0; j < viewWidth; j++) {
                Coordinate cell = new Coordinate(i, j);
                edges.addAll(CellOperations.getRightBottomEdges(
                    cell, mazeView.viewHeight(), mazeView.viewWidth()
                ));
            }
        }
        Collections.shuffle(edges);

        // Making Minimal Spanning Tree based on the edges.
        // Using DSU in order to check whether the path between two cells already exists.
        DSU cellsUnions = new DSU(viewHeight * viewWidth);
        for (Edge edge : edges) {
            // As DSU contains only 1D numbers, we have to numerate our cells to use it.
            // Let's numerate them like (i, j) -> i * width + j
            int fromFlat = CellOperations.flatCellIndex(edge.from(), viewWidth);
            int toFlat = CellOperations.flatCellIndex(edge.to(), viewWidth);

            // Connect cells only if they this is no path between them already.
            if (cellsUnions.leader(fromFlat) != cellsUnions.leader(toFlat)) {
                mazeView.connectCells(edge.from(), edge.to());
                cellsUnions.unite(fromFlat, toFlat);
            }
        }
    }
}
