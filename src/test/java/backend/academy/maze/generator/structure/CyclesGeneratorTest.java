package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.utility.CellOperations;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CyclesGeneratorTest {
    @Test
    public void cyclesExists() {
        GeneratorSettings settings = new GeneratorSettings(
            50, 50,
            0.2, 0, 0,
            ExitEntrancePosition.LEFT_LOWER_CORNER, ExitEntrancePosition.LEFT_UPPER_CORNER
        );

        MazeView view = new MazeView(50, 50);
        new KruskalGenerator().generate(view, settings);
        new CyclesGenerator().generate(view, settings);

        boolean[][] visited = new boolean[view.viewHeight()][view.viewWidth()];

        assertThat(dfsFindCycle(view, visited, new Coordinate(0, 0))).isTrue();
    }

    private boolean dfsFindCycle(MazeView view, boolean[][] visited, Coordinate c) {
        visited[c.row()][c.col()] = true;

        for (Coordinate neighbour : CellOperations.getAllNeighbours(c, view.viewHeight(), view.viewWidth())) {
            if (view.isCellsConnected(c, neighbour)) {
                if (visited[neighbour.row()][neighbour.col()]) {
                    return true;
                }
                if (dfsFindCycle(view, visited, neighbour)) {
                    return true;
                }
            }
        }
        return false;
    }
}
