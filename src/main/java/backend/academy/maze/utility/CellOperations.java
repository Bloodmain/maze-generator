package backend.academy.maze.utility;

import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Edge;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CellOperations {
    public static List<Coordinate> getAllNeighbours(Coordinate cell, int height, int width) {
        // Every cell has 4 ways to go next
        List<Coordinate> neighbours = List.of(
            cell.withRow(cell.row() + 1), // bottom neighbour
            cell.withRow(cell.row() - 1), // top neighbour
            cell.withCol(cell.col() + 1), // right neighbour
            cell.withCol(cell.col() - 1) // left neighbour
        );

        return filterBounds(neighbours, height, width);
    }

    public static List<Edge> getRightBottomEdges(Coordinate cell, int height, int width) {
        List<Coordinate> neighbours = List.of(
            cell.withRow(cell.row() + 1), // bottom neighbour
            cell.withCol(cell.col() + 1)  // right neighbour
        );

        return filterBounds(neighbours, height, width).stream()
            .map(c -> new Edge(cell, c))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<Coordinate> filterBounds(List<Coordinate> coordinates, int height, int width) {
        return coordinates.stream()
            .filter(c -> c.row() >= 0 && c.row() < height && c.col() >= 0 && c.col() < width)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts the 2D cell to 1D flat index of it.
     * Has the same effect as flattening the maze's grid and getting the index of the cell.
     *
     * @param c     the cell
     * @param width width of the maze
     * @return flat index of the cell
     */
    public static int flatCellIndex(Coordinate c, int width) {
        return c.row() * width + c.col();
    }

    /**
     * Converts flatten index to (row, col) coordinates.
     *
     * @param i     flat index of the cell
     * @param width width of the maze
     * @return coordinates of the cell
     */
    public static Coordinate unflatCellIndex(int i, int width) {
        return new Coordinate(i / width, i % width);
    }
}
