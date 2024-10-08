package backend.academy.maze.solver;

import backend.academy.maze.algorithm.ShortestPathAlgorithm;
import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import backend.academy.maze.utility.CellOperations;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Provides proxy from maze to algorithms that use flat indexing and edge list.
 */
public abstract class GeneralMazeSolver implements Solver {
    private static final int ESTIMATE_NEIGHBOURS = 4;

    @Override
    public List<Coordinate> solve(Maze maze, EnergySettings energySettings, Coordinate start, Coordinate end) {
        if (maze.cellAt(start).type() == Cell.Type.WALL || maze.cellAt(end).type() == Cell.Type.WALL) {
            throw new IllegalArgumentException("Start and end cells should not be walls");
        }

        // Gets all available neighbours of the given cell (converting to flat indexation)
        Function<Integer, List<Integer>> edgeFunction = i ->
            getNeighbours(maze, CellOperations.unflatCellIndex(i, maze.width()))
                .stream()
                .map(c -> CellOperations.flatCellIndex(c, maze.width()))
                .toList();

        // Provides the energy cost of the given flat-indexed cell
        Function<Integer, Integer> weightFunction = i ->
            energySettings.getEnergy(
                maze.cellAt(CellOperations.unflatCellIndex(i, maze.width())).type()
            );

        // Flatten coordinates of the path
        List<Integer> flatPath = getSolver().findPath(
            CellOperations.flatCellIndex(start, maze.width()),
            CellOperations.flatCellIndex(end, maze.width()),
            maze.height() * maze.width(),
            edgeFunction,
            weightFunction
        );

        return flatPath.stream().map(i -> CellOperations.unflatCellIndex(i, maze.width())).toList();
    }

    private static List<Coordinate> getNeighbours(Maze maze, Coordinate c) {
        List<Coordinate> res = new ArrayList<>(ESTIMATE_NEIGHBOURS);

        // Filtering the neighbours to be available for going to
        for (Coordinate neighbour : CellOperations.getAllNeighbours(c, maze.height(), maze.width())) {
            if (maze.cellAt(neighbour).type() == Cell.Type.WALL) {
                continue;
            }

            res.add(neighbour);
        }

        return res;
    }

    protected abstract ShortestPathAlgorithm getSolver();
}
