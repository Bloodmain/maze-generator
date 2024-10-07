package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.model.Cell;

/**
 * Generates a maze with entrance and an exit.
 */
public class MazeExitEntranceGenerator implements Generator {
    @Override
    public void generate(MazeView mazeView, GeneratorSettings settings) {
        mazeView.setExitEntrance(settings.start(), Cell.Type.ENTRANCE);
        mazeView.setExitEntrance(settings.finish(), Cell.Type.EXIT);
    }
}
