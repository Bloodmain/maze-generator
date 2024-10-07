package backend.academy.maze.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class Maze {
    private final int height;
    private final int width;
    private final Cell[][] grid;

    public Cell cellAt(Coordinate position) {
        if (position.row() < 0 || position.row() >= height || position.col() < 0 || position.col() >= width) {
            throw new IndexOutOfBoundsException(
                "Position %s is out of height %d and width %d".formatted(position, height, width)
            );
        }
        return grid[position.row()][position.col()];
    }
}
