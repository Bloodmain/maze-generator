package backend.academy.maze.algorithm;

import java.util.List;
import java.util.function.Function;

public interface ShortestPathAlgorithm {
    List<Integer> findPath(
        int start, int end, int vertexCount,
        Function<Integer, List<Integer>> edgesFunction,
        Function<Integer, Integer> weightingFunction
    );
}
