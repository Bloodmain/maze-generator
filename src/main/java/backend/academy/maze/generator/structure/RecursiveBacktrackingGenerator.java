package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.utility.CellOperations;
import java.util.Collections;
import java.util.List;

public class RecursiveBacktrackingGenerator implements Generator {
    @Override
    public void generate(MazeView mazeView, GeneratorSettings settings) {
        boolean[][] visited = new boolean[mazeView.viewHeight()][mazeView.viewWidth()];
        backtrack(mazeView, settings, visited, new Coordinate(0, 0));
    }

    private void backtrack(MazeView mazeView, GeneratorSettings settings, boolean[][] visited, Coordinate currentCell) {
        visited[currentCell.row()][currentCell.col()] = true;
        List<Coordinate> nexts = CellOperations.getAllNeighbours(
            currentCell, mazeView.viewHeight(), mazeView.viewWidth()
        );
        Collections.shuffle(nexts);

        for (Coordinate next : nexts) {
            if (!visited[next.row()][next.col()]) {
                mazeView.connectCells(currentCell, next);
                backtrack(mazeView, settings, visited, next);
            }
        }
    }
}
