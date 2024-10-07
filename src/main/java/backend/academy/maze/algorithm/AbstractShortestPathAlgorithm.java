package backend.academy.maze.algorithm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides default lists initializations and path restoring for solver algorithms.
 */
public abstract class AbstractShortestPathAlgorithm implements ShortestPathAlgorithm {
    @Override
    @SuppressFBWarnings(
        value = "PSC_PRESIZE_COLLECTIONS",
        justification = "line 44: we can't estimate the actual size of the path, "
            + "only by the vertexCount, by it's too big"
    )
    public List<Integer> findPath(
        int start,
        int end,
        int vertexCount,
        Function<Integer, List<Integer>> edgesFunction,
        Function<Integer, Integer> weightingFunction
    ) {
        // Contains the cost of getting from start to the cells.
        // Empty optional in case of no path (or not processed yet)
        List<Optional<Integer>> cost = Stream.<Optional<Integer>>generate(Optional::empty).limit(vertexCount)
            .collect(Collectors.toCollection(ArrayList::new));
        cost.set(start, Optional.of(0));

        // The cell from which we had gotten here using with minimal path cost.
        // Need for restoring the path
        List<Integer> parent =
            Stream.iterate(0, i -> i + 1).limit(vertexCount).collect(Collectors.toCollection(ArrayList::new));

        solve(start, end, vertexCount, edgesFunction, weightingFunction, cost, parent);

        if (cost.get(end).isEmpty()) {
            // The destination hasn't been reached
            return Collections.emptyList();
        }

        // Restoring the path going from destination point to the start point using parent list
        List<Integer> path = new ArrayList<>();
        int current = end;
        while (!parent.get(current).equals(current)) {
            path.add(current);
            current = parent.get(current);
        }

        path.add(current);

        // The list stores the path in backwards order
        return path.reversed();
    }

    protected abstract void solve(
        int start,
        int end,
        int vertexCount,
        Function<Integer, List<Integer>> edgesFunction,
        Function<Integer, Integer> weightingFunction,
        List<Optional<Integer>> cost,
        List<Integer> parent
    );
}
