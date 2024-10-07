package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Generates a maze with random surfaces types.
 */
public class MazeSurfacesGenerator implements Generator {
    private final Random rnd = new SecureRandom();

    @Override
    public void generate(MazeView mazeView, GeneratorSettings settings) {
        assignRandomSurfaceType(mazeView, settings);
    }

    private void assignRandomSurfaceType(MazeView mazeView, GeneratorSettings settings) {
        for (int i = 0; i < mazeView.viewHeight(); i++) {
            for (int j = 0; j < mazeView.viewWidth(); j++) {
                double r = rnd.nextDouble();
                if (r < settings.badTypesPortion()) {
                    mazeView.assignCellType(new Coordinate(i, j), Cell.Type.WASP);
                } else if (r < settings.badTypesPortion() + settings.goodTypesPortion()) {
                    mazeView.assignCellType(new Coordinate(i, j), Cell.Type.MUSIC);
                }
            }
        }
    }
}
