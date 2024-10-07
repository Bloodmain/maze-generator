package backend.academy.maze.generator;

import backend.academy.maze.generator.structure.ExitEntrancePosition;
import lombok.With;

@With
public record GeneratorSettings(
    int height, int width,
    double cyclesPortion, double badTypesPortion, double goodTypesPortion,
    ExitEntrancePosition start, ExitEntrancePosition finish
) {
}
