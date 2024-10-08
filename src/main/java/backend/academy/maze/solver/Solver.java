package backend.academy.maze.solver;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface Solver {
    /**
     * Solves the maze from start point to the end point
     *
     * @param maze  the maze to solve
     * @param start the start point
     * @param end   the end point
     * @return solved path
     */
    List<Coordinate> solve(Maze maze, EnergySettings energySettings, Coordinate start, Coordinate end);

    /**
     * Solves the maze from entrance point to end point if they exist
     *
     * @param maze the maze to solve
     * @return solved path
     */
    default List<Coordinate> solve(Maze maze, EnergySettings energySettings) {
        Optional<Coordinate> start = Optional.empty();
        Optional<Coordinate> finish = Optional.empty();

        for (int i = 0; i < maze.height(); ++i) {
            for (int j = 0; j < maze.width(); ++j) {
                Coordinate cell = new Coordinate(i, j);
                Cell.Type type = maze.cellAt(cell).type();
                if (type == Cell.Type.ENTRANCE) {
                    start = Optional.of(cell);
                } else if (type == Cell.Type.EXIT) {
                    finish = Optional.of(cell);
                }
            }
        }

        if (start.isPresent() && finish.isPresent()) {
            return solve(maze, energySettings, start.orElseThrow(), finish.orElseThrow());
        }

        return Collections.emptyList();
    }
}
