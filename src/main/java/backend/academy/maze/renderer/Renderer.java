package backend.academy.maze.renderer;

import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import backend.academy.maze.solver.PathStatistic;
import java.util.List;

public interface Renderer {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);

    String render(PathStatistic pathStatisticResult);
}
