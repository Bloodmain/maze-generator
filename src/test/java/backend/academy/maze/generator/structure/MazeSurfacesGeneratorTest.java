package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneralMazeGenerator;
import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class MazeSurfacesGeneratorTest {
    @Test
    public void findGoodAndBadSurfaces() {
        GeneratorSettings settings = new GeneratorSettings(
            50, 50,
            0.2, 0.2, 0.2,
            ExitEntrancePosition.LEFT_LOWER_CORNER, ExitEntrancePosition.LEFT_UPPER_CORNER
        );

        Maze maze = new GeneralMazeGenerator()
            .generate(List.of(new KruskalGenerator(), new MazeSurfacesGenerator()), settings);

        int goodCount = 0;
        int badCount = 0;
        for (int i = 0; i < maze.height(); ++i) {
            for (int j = 0; j < maze.width(); ++j) {
                if (maze.cellAt(new Coordinate(i, j)).type() == Cell.Type.WASP) {
                    badCount++;
                } else if (maze.cellAt(new Coordinate(i, j)).type() == Cell.Type.MUSIC) {
                    goodCount++;
                }
            }
        }

        assertThat(goodCount).isGreaterThan(0);
        assertThat(badCount).isGreaterThan(0);
    }

    @Test
    public void surfacesPortion() {
        GeneratorSettings settings = new GeneratorSettings(
            50, 50,
            0.2, 0.2, 0.6,
            ExitEntrancePosition.LEFT_LOWER_CORNER, ExitEntrancePosition.LEFT_UPPER_CORNER
        );

        Maze maze = new GeneralMazeGenerator()
            .generate(List.of(new KruskalGenerator(), new MazeSurfacesGenerator()), settings);

        int goodCount = 0;
        int badCount = 0;
        for (int i = 0; i < maze.height(); ++i) {
            for (int j = 0; j < maze.width(); ++j) {
                if (maze.cellAt(new Coordinate(i, j)).type() == Cell.Type.WASP) {
                    badCount++;
                } else if (maze.cellAt(new Coordinate(i, j)).type() == Cell.Type.MUSIC) {
                    goodCount++;
                }
            }
        }

        assertThat(goodCount).isGreaterThan(badCount);
    }
}
