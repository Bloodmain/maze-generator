package backend.academy.maze.generator.structure;

import backend.academy.maze.generator.GeneratorSettings;

/**
 * Generates some structure inside the given maze view. For example:<p>
 * - Passages<p>
 * - Surface types
 */
public interface Generator {
    /**
     * Generates structures inside the given maze view.
     *
     * @param mazeView the maze view
     * @param settings the settings for generating
     */
    void generate(MazeView mazeView, GeneratorSettings settings);
}
