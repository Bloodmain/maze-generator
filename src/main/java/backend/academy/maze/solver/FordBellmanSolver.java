package backend.academy.maze.solver;

import backend.academy.maze.algorithm.FordBellman;
import backend.academy.maze.algorithm.ShortestPathAlgorithm;

public class FordBellmanSolver extends GeneralMazeSolver {
    @Override
    protected ShortestPathAlgorithm getSolver() {
        return new FordBellman();
    }
}
