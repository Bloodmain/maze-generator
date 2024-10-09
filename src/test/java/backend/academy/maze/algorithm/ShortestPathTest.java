package backend.academy.maze.algorithm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

public class ShortestPathTest {
    private static Stream<ShortestPathAlgorithm> shortestPathAlgorithms() {
        return Stream.of(
            new Dijkstra(),
            new FordBellman()
        );
    }

    @ParameterizedTest
    @MethodSource("shortestPathAlgorithms")
    public void noPath(ShortestPathAlgorithm algorithm) {
        List<Integer> path = algorithm.findPath(
            0, 19, 20,
            // complete graph on 19 vertexes and 20th vertex is isolated
            i -> IntStream.iterate(0, j -> j + 1).limit(19).boxed().toList(),
            i -> 1
        );

        assertThat(path).hasSize(0);
    }

    @ParameterizedTest
    @MethodSource("shortestPathAlgorithms")
    public void shortestPathCube(ShortestPathAlgorithm algorithm) {
        /*
           Graph:
           (0,1)(1,3)
           (2,4)(3,1)

           (i,w) is (index of cell, weight of cell)
         */
        List<List<Integer>> edges = List.of(
            List.of(1, 2),
            List.of(0, 3),
            List.of(0, 3),
            List.of(1, 2)
        );
        List<Integer> weights = List.of(1, 3, 4, 1);

        List<Integer> path = algorithm.findPath(
            0, 3, 4,
            edges::get,
            weights::get
        );
        int pathWeight = path.stream().mapToInt(weights::get).sum();

        assertThat(pathWeight).isEqualTo(5);
    }

    @ParameterizedTest
    @MethodSource("shortestPathAlgorithms")
    public void shortestPathWithWalls(ShortestPathAlgorithm algorithm) {
        /*
           (0,1)     (1,1)(2,10)(3,1)(4,1)
                (5,1)(6,1)(7,3)      (8,1)
                (9,10)    (10,1)     (11,1)
                (12,1)    (13,1)(14,1)(15,1)
         */
        List<List<Integer>> edges = List.of(
            List.of(),
            List.of(6, 2),
            List.of(7, 3),
            List.of(4, 2),
            List.of(3, 8),
            List.of(9, 6),
            List.of(7, 1, 5),
            List.of(2, 6, 10),
            List.of(4, 11),
            List.of(5, 12),
            List.of(7, 13),
            List.of(8, 15),
            List.of(9),
            List.of(10, 14),
            List.of(13, 15),
            List.of(14, 11)
        );
        List<Integer> weights = List.of(0, 1, 10, 1, 1, 1, 1, 3, 1, 10, 1, 1, 1, 1, 1, 1);

        List<Integer> path = algorithm.findPath(
            3, 12, 16,
            edges::get,
            weights::get
        );
        int pathWeight = path.stream().mapToInt(weights::get).sum();

        assertThat(pathWeight).isEqualTo(24);
    }
}
