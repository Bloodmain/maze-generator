package backend.academy.maze.renderer;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Maze;
import backend.academy.maze.solver.PathStatistic;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SymbolicRendererTest {
    @Test
    public void cellsTypesCount() {
        Cell[][] grid = new Cell[8][8];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i, j, Cell.Type.PASSAGE);
            }
        }

        grid[7][7] = new Cell(7, 7, Cell.Type.WALL);
        grid[0][0] = new Cell(0, 0, Cell.Type.WASP);
        grid[5][0] = new Cell(5, 0, Cell.Type.WASP);
        grid[2][4] = new Cell(2, 4, Cell.Type.WASP);
        grid[7][6] = new Cell(7, 6, Cell.Type.MUSIC);
        grid[1][6] = new Cell(1, 6, Cell.Type.MUSIC);
        grid[0][1] = new Cell(0, 1, Cell.Type.ENTRANCE);
        grid[1][0] = new Cell(1, 0, Cell.Type.EXIT);

        Renderer renderer = new SymbolicRenderer();
        String representation = renderer.render(new Maze(grid.length, grid[0].length, grid));

        assertThat(count(representation, '#')).isEqualTo(1);
        assertThat(count(representation, '·')).isEqualTo(56);
        assertThat(count(representation, '⌘')).isEqualTo(4);
        assertThat(count(representation, '♫')).isEqualTo(3);
        assertThat(count(representation, 'I')).isEqualTo(2);
        assertThat(count(representation, 'O')).isEqualTo(2);
    }

    @Test
    public void statisticResultRender() {
        Renderer renderer = new SymbolicRenderer();
        PathStatistic statistic = new PathStatistic(15, 13, 17, 121);

        String representation = renderer.render(statistic);

        assertThat(representation).contains("15");
        assertThat(representation).contains("13");
        assertThat(representation).contains("17");
        assertThat(representation).contains("121");
    }

    private static int count(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
}
