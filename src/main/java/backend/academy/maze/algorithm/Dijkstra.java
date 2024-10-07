package backend.academy.maze.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Function;

/**
 * Realization of the Dijkstra algorithm, using PriorityQueue.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Dijkstra algorithm on wiki</a>
 */
public class Dijkstra extends AbstractShortestPathAlgorithm {
    public void solve(
        int start,
        int end,
        int vertexCount,
        Function<Integer, List<Integer>> edgesFunction,
        Function<Integer, Integer> weightingFunction,
        List<Optional<Integer>> cost,
        List<Integer> parent
    ) {
        // Priority queue for getting the vertex that has minimal cost for now.
        PriorityQueue<WeightedVertex> queue = new PriorityQueue<>(Comparator.comparingInt(WeightedVertex::cost));
        queue.add(new WeightedVertex(0, start));

        while (!queue.isEmpty()) {
            WeightedVertex vertex = queue.poll();
            Optional<Integer> weight = cost.get(vertex.vertex());

            if (weight.isEmpty() || vertex.cost() != weight.orElseThrow()) {
                // means either vertex is not reached or it has old information about its cost.
                continue;
            }

            for (int next : edgesFunction.apply(vertex.vertex())) {
                int edgeCost = weightingFunction.apply(next);
                int updatedWeight = weight.orElseThrow() // safe get because we've checked for non-empty above
                    + edgeCost;
                Optional<Integer> nextWeightNow = cost.get(next);

                if (nextWeightNow.isEmpty() || updatedWeight < nextWeightNow.orElseThrow()) {
                    // updating only if we've just found path with less cost

                    cost.set(next, Optional.of(updatedWeight));
                    parent.set(next, vertex.vertex());
                    queue.add(new WeightedVertex(updatedWeight, next));
                }
            }
        }
    }

    private record WeightedVertex(int cost, int vertex) {
    }
}
