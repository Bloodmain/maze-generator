package backend.academy.maze.solver;

import backend.academy.maze.generator.structure.MazeView;
import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PathStatisticTest {
    @Test
    public void emptyPath() {
        Maze maze = new MazeView(50, 50).getRealMaze();
        EnergySettings settings = new EnergySettings(10, 15, 20);
        PathStatisticCollector collector = new PathStatisticCollector();

        PathStatistic actual = collector.collectStatistics(maze, Collections.emptyList(), settings);

        assertThat(actual.cellsCount()).isEqualTo(0);
        assertThat(actual.waspCollected()).isEqualTo(0);
        assertThat(actual.musicCollected()).isEqualTo(0);
        assertThat(actual.energyUsed()).isEqualTo(0);
    }

    @Test
    public void passageCellsPath() {
        Maze maze = new MazeView(50, 50).getRealMaze();
        EnergySettings settings = new EnergySettings(10, 15, 20);
        PathStatisticCollector collector = new PathStatisticCollector();
        List<Coordinate> path = List.of(
            new Coordinate(1, 1),
            new Coordinate(3, 3),
            new Coordinate(5, 5),
            new Coordinate(5, 5),
            new Coordinate(21, 21)
        );

        PathStatistic actual = collector.collectStatistics(maze, path, settings);

        assertThat(actual.cellsCount()).isEqualTo(5);
        assertThat(actual.waspCollected()).isEqualTo(0);
        assertThat(actual.musicCollected()).isEqualTo(0);
        assertThat(actual.energyUsed()).isEqualTo(100);
    }

    @Test
    public void differentSurfacesPath() {
        Maze maze = new MazeView(50, 50).getRealMaze();
        maze.grid()[0][0] = new Cell(0, 0, Cell.Type.WASP);
        maze.grid()[0][1] = new Cell(0, 0, Cell.Type.WASP);
        maze.grid()[0][2] = new Cell(0, 0, Cell.Type.MUSIC);

        EnergySettings settings = new EnergySettings(7, 3, 5);
        PathStatisticCollector collector = new PathStatisticCollector();
        List<Coordinate> path = List.of(
            new Coordinate(0, 0),
            new Coordinate(3, 3),
            new Coordinate(0, 2),
            new Coordinate(0, 1),
            new Coordinate(21, 21)
        );

        PathStatistic actual = collector.collectStatistics(maze, path, settings);

        assertThat(actual.cellsCount()).isEqualTo(5);
        assertThat(actual.waspCollected()).isEqualTo(2);
        assertThat(actual.musicCollected()).isEqualTo(1);
        assertThat(actual.energyUsed()).isEqualTo(27);
    }
}
