package backend.academy.maze.generator.structure;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MazeViewTest {
    @Test
    public void viewSize() {
        MazeView view = new MazeView(50, 7);

        assertThat(view.viewHeight()).isEqualTo(24);
        assertThat(view.viewWidth()).isEqualTo(3);
    }

    @Test
    public void getRealMaze() {
        MazeView view = new MazeView(9, 9);
        Maze maze = view.getRealMaze();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell.Type type = i % 2 == 1 && j % 2 == 1 ? Cell.Type.PASSAGE : Cell.Type.WALL;

                assertThat(maze.cellAt(new Coordinate(i, j))).isEqualTo(new Cell(i, j, type));
            }
        }
    }

    @Test
    public void cellConnection() {
        MazeView view = new MazeView(5, 9);
        /*
          #########         #########
          #.#.#.#.#         #.#.#.#.#
          #########   ->    #.#####.#
          #.#.#.#.#         #.#.#...#
          #########         #########
         */
        view.connectCells(new Coordinate(0, 0), new Coordinate(1, 0));
        view.connectCells(new Coordinate(1, 3), new Coordinate(1, 2));
        view.connectCells(new Coordinate(1, 3), new Coordinate(0, 3));

        assertThat(view.isCellsConnected(new Coordinate(0, 0), new Coordinate(1, 0))).isTrue();
        assertThat(view.isCellsConnected(new Coordinate(1, 0), new Coordinate(0, 0))).isTrue();

        assertThat(view.isCellsConnected(new Coordinate(1, 3), new Coordinate(1, 2))).isTrue();
        assertThat(view.isCellsConnected(new Coordinate(1, 2), new Coordinate(1, 3))).isTrue();

        assertThat(view.isCellsConnected(new Coordinate(1, 3), new Coordinate(0, 3))).isTrue();
        assertThat(view.isCellsConnected(new Coordinate(0, 3), new Coordinate(1, 3))).isTrue();

        // other cells should not be connected
        assertThat(view.isCellsConnected(new Coordinate(0, 0), new Coordinate(0, 1))).isFalse();
        assertThat(view.isCellsConnected(new Coordinate(0, 1), new Coordinate(0, 0))).isFalse();
        assertThat(view.isCellsConnected(new Coordinate(0, 2), new Coordinate(0, 3))).isFalse();
        assertThat(view.isCellsConnected(new Coordinate(0, 3), new Coordinate(0, 2))).isFalse();
        assertThat(view.isCellsConnected(new Coordinate(1, 1), new Coordinate(1, 2))).isFalse();
        assertThat(view.isCellsConnected(new Coordinate(1, 2), new Coordinate(1, 1))).isFalse();

        Maze maze = view.getRealMaze();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                Cell.Type type = i % 2 == 1 && j % 2 == 1 ? Cell.Type.PASSAGE : Cell.Type.WALL;
                if ((i == 2 && j == 1) || (i == 2 && j == 7) || (i == 3 && j == 6)) {
                    type = Cell.Type.PASSAGE;
                }

                assertThat(maze.cellAt(new Coordinate(i, j))).isEqualTo(new Cell(i, j, type));
            }
        }
    }
}
