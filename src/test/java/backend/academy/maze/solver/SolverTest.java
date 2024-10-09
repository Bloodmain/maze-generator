package backend.academy.maze.solver;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {
    private static Stream<Solver> solvers() {
        return Stream.of(
            new DijkstraSolver(),
            new FordBellmanSolver()
        );
    }

    private Maze makeMazeFromString(List<String> lines) {
        Cell[][] grid = new Cell[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                switch (lines.get(i).charAt(j)) {
                    case '#' -> grid[i][j] = new Cell(i, j, Cell.Type.WALL);
                    case '.' -> grid[i][j] = new Cell(i, j, Cell.Type.PASSAGE);
                    case 'W' -> grid[i][j] = new Cell(i, j, Cell.Type.WASP);
                    case 'M' -> grid[i][j] = new Cell(i, j, Cell.Type.MUSIC);
                    case 'I' -> grid[i][j] = new Cell(i, j, Cell.Type.ENTRANCE);
                    case 'O' -> grid[i][j] = new Cell(i, j, Cell.Type.EXIT);
                    default -> throw new IllegalStateException("Unexpected value: " + lines.get(i).charAt(j));
                }
            }
        }

        return new Maze(lines.size(), lines.get(0).length(), grid);
    }

    @ParameterizedTest
    @MethodSource("solvers")
    public void mazeWithNoPath(Solver solver) {
        Maze maze = makeMazeFromString(List.of(
            "#I###",
            "#.###",
            "#..#O"
        ));
        EnergySettings settings = new EnergySettings(10, 3, 5);

        List<Coordinate> path = solver.solve(maze, settings);

        assertThat(path).hasSize(0);
    }

    @ParameterizedTest
    @MethodSource("solvers")
    public void noInOutPoints(Solver solver) {
        Maze maze = makeMazeFromString(List.of(
            "##.....",
            "#..###.",
            "#.#...#",
            "#...#.."
        ));
        EnergySettings settings = new EnergySettings(10, 3, 5);

        List<Coordinate> path = solver.solve(maze, settings);

        assertThat(path).hasSize(0);
    }

    @ParameterizedTest
    @MethodSource("solvers")
    public void pathBetweenCustomPoints(Solver solver) {
        Maze maze = makeMazeFromString(List.of(
            "##.....",
            "#..###.",
            "#.#...#",
            "#...#.."
        ));
        EnergySettings settings = new EnergySettings(10, 3, 5);

        List<Coordinate> path = solver.solve(maze, settings, new Coordinate(2, 3), new Coordinate(1, 2));

        assertThat(path).hasSize(7);
        assertThat(path).isEqualTo(List.of(
            new Coordinate(2, 3),
            new Coordinate(3, 3),
            new Coordinate(3, 2),
            new Coordinate(3, 1),
            new Coordinate(2, 1),
            new Coordinate(1, 1),
            new Coordinate(1, 2)
        ));
    }

    @ParameterizedTest
    @MethodSource("solvers")
    public void inOutPath(Solver solver) {
        Maze maze = makeMazeFromString(List.of(
            "##I....",
            "#..###.",
            "#O#...#",
            "#...#.."
        ));
        EnergySettings settings = new EnergySettings(10, 3, 5);

        List<Coordinate> path = solver.solve(maze, settings);

        assertThat(path).hasSize(4);
        assertThat(path).isEqualTo(List.of(
            new Coordinate(0, 2),
            new Coordinate(1, 2),
            new Coordinate(1, 1),
            new Coordinate(2, 1)
        ));
    }

    @ParameterizedTest
    @MethodSource("solvers")
    public void shortestPath(Solver solver) {
        Maze maze = makeMazeFromString(List.of(
            "##I..",
            ".....",
            ".WWW.",
            "..W..",
            "M.O.."
        ));
        EnergySettings settings = new EnergySettings(50, 1, 5);

        List<Coordinate> path = solver.solve(maze, settings);

        assertThat(path).hasSize(9);
        assertThat(path).isEqualTo(List.of(
            new Coordinate(0, 2),
            new Coordinate(1, 2),
            new Coordinate(1, 1),
            new Coordinate(1, 0),
            new Coordinate(2, 0),
            new Coordinate(3, 0),
            new Coordinate(4, 0),
            new Coordinate(4, 1),
            new Coordinate(4, 2)
        ));
    }
}
