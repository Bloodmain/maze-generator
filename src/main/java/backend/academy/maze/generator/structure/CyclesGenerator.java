package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Edge;
import backend.academy.maze.utility.CellOperations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclesGenerator implements Generator {
    @Override
    public void generate(MazeView mazeView, GeneratorSettings settings) {
        List<Edge> unusedEdges = new ArrayList<>();
        for (int i = 0; i < mazeView.viewHeight(); ++i) {
            for (int j = 0; j < mazeView.viewWidth(); ++j) {
                Coordinate cell = new Coordinate(i, j);
                List<Edge> edges = CellOperations.getRightBottomEdges(
                    cell, mazeView.viewHeight(), mazeView.viewWidth()
                );
                for (Edge edge : edges) {
                    if (!mazeView.isCellsConnected(edge.from(), edge.to())) {
                        unusedEdges.add(edge);
                    }
                }
            }
        }
        Collections.shuffle(unusedEdges);

        // Takes random unused edges to make cycles
        int numberOfEdgesForCycles = (int) (unusedEdges.size() * settings.cyclesPortion());
        for (Edge edge : unusedEdges.subList(0, numberOfEdgesForCycles)) {
            mazeView.connectCells(edge.from(), edge.to());
        }
    }
}
