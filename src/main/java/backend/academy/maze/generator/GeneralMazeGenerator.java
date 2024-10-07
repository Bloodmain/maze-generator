package backend.academy.maze.generator;

import backend.academy.maze.generator.structure.Generator;
import backend.academy.maze.generator.structure.MazeView;
import backend.academy.maze.model.Maze;
import java.util.List;

/**
 * Generates a maze, constructing a mazeView and passing it to all the structureGenerators sequentially.
 */
public class GeneralMazeGenerator {
    public Maze generate(List<Generator> structureGenerators, GeneratorSettings settings) {
        MazeView mazeView = new MazeView(settings.height(), settings.width());

        for (Generator structureGenerator : structureGenerators) {
            structureGenerator.generate(mazeView, settings);
        }

        return mazeView.getRealMaze();
    }
}
