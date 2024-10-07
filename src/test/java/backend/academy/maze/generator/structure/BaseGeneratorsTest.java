package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.utility.CellOperations;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BaseGeneratorsTest {
    private static final GeneratorSettings SETTINGS = new GeneratorSettings(
        50, 50,
        0, 0, 0,
        ExitEntrancePosition.LEFT_LOWER_CORNER, ExitEntrancePosition.LEFT_UPPER_CORNER
    );

    private static Stream<Generator> generators() {
        return Stream.of(
            new KruskalGenerator(),
            new RecursiveBacktrackingGenerator()
        );
    }

    @ParameterizedTest
    @MethodSource("generators")
    public void allCellsConnected(Generator generator) {
        MazeView view = new MazeView(50, 50);

        generator.generate(view, SETTINGS);

        // Maze does not have cycle here, it's a tree, so we don't need any visited array,
        // only should check not to go back to the parent
        int actualCellsConnected = dfsVisitCells(view, new Coordinate(0, 0), new Coordinate(0, 0));

        assertThat(actualCellsConnected).isEqualTo(view.viewWidth() * view.viewHeight());
    }

    private int dfsVisitCells(MazeView view, Coordinate c, Coordinate p) {
        int visitedCount = 1;
        for (Coordinate neighbour : CellOperations.getAllNeighbours(c, view.viewHeight(), view.viewWidth())) {
            if (!neighbour.equals(p) && view.isCellsConnected(c, neighbour)) {
                visitedCount += dfsVisitCells(view, neighbour, c);
            }
        }
        return visitedCount;
    }
}
