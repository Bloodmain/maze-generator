package backend.academy.maze.utility;

import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Edge;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class CellOperationsTest {
    @Test
    public void flatIndexFirstCell() {
        Coordinate c = new Coordinate(0, 0);
        int flatIndex = CellOperations.flatCellIndex(c, 50);

        assertThat(flatIndex).isEqualTo(0);
    }

    @Test
    public void flatIndexCell() {
        Coordinate c = new Coordinate(3, 4);
        int flatIndex = CellOperations.flatCellIndex(c, 50);

        assertThat(flatIndex).isEqualTo(154);
    }

    @Test
    public void unflatIndexFirstCell() {
        Coordinate c = CellOperations.unflatCellIndex(0, 50);

        assertThat(c).isEqualTo(new Coordinate(0, 0));
    }

    @Test
    public void unflatIndexCell() {
        Coordinate c = CellOperations.unflatCellIndex(57, 50);

        assertThat(c).isEqualTo(new Coordinate(1, 7));
    }

    @Test
    public void allNeighbours() {
        List<Coordinate> neighbours = CellOperations.getAllNeighbours(new Coordinate(1, 1), 5, 5);

        assertThat(neighbours.size()).isEqualTo(4);
        assertThat(new Coordinate(0, 1)).isIn(neighbours);
        assertThat(new Coordinate(1, 0)).isIn(neighbours);
        assertThat(new Coordinate(1, 2)).isIn(neighbours);
        assertThat(new Coordinate(2, 1)).isIn(neighbours);
    }

    @Test
    public void neighboursWithBounds() {
        List<Coordinate> neighbours = CellOperations.getAllNeighbours(new Coordinate(1, 2), 3, 3);

        assertThat(neighbours.size()).isEqualTo(3);
        assertThat(new Coordinate(0, 2)).isIn(neighbours);
        assertThat(new Coordinate(1, 1)).isIn(neighbours);
        assertThat(new Coordinate(2, 2)).isIn(neighbours);
    }

    @Test
    public void NeighboursEdges() {
        List<Edge> edges = CellOperations.getRightBottomEdges(new Coordinate(1, 1), 5, 5);

        assertThat(edges.size()).isEqualTo(2);
        assertThat(new Edge(new Coordinate(1, 1), new Coordinate(2, 1))).isIn(edges);
        assertThat(new Edge(new Coordinate(1, 1), new Coordinate(1, 2))).isIn(edges);
    }

    @Test
    public void NeighboursEdgesWithBounds() {
        List<Edge> edges = CellOperations.getRightBottomEdges(new Coordinate(0, 1), 2, 2);

        assertThat(edges.size()).isEqualTo(1);
        assertThat(new Edge(new Coordinate(0, 1), new Coordinate(1, 1))).isIn(edges);
    }
}
