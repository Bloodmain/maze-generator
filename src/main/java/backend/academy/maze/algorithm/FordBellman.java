package backend.academy.maze.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Realization of Ford-Bellman algorithm.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm#:~:text=The%20Bellman%E2%80%93Ford%20algorithm%20is,vertices%20in%20a%20weighted%20digraph.">Ford-Bellman on wiki</a>
 */
public class FordBellman extends AbstractShortestPathAlgorithm {
    public void solve(
        int start,
        int end,
        int vertexCount,
        Function<Integer, List<Integer>> edgesFunction,
        Function<Integer, Integer> weightingFunction,
        List<Optional<Integer>> cost,
        List<Integer> parent
    ) {
        for (int i = 0; i < vertexCount - 1; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (cost.get(j).isEmpty()) {
                    continue;
                }

                for (int next : edgesFunction.apply(j)) {
                    Optional<Integer> nextOldCost = cost.get(next);
                    int nextNewCost = cost.get(j).orElseThrow() // safe get because we've checked for non-empty above
                        + weightingFunction.apply(next);
                    if (nextOldCost.isEmpty() || nextNewCost < nextOldCost.orElseThrow()) {
                        cost.set(next, Optional.of(nextNewCost));
                        parent.set(next, j);
                    }
                }
            }
        }

    }
}
