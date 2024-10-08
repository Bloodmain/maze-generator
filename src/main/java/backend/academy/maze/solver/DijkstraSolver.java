package backend.academy.maze.solver;

import backend.academy.maze.algorithm.Dijkstra;
import backend.academy.maze.algorithm.ShortestPathAlgorithm;

public class DijkstraSolver extends GeneralMazeSolver {
    @Override
    protected ShortestPathAlgorithm getSolver() {
        return new Dijkstra();
    }
}
