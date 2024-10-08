package backend.academy.maze.solver;

import lombok.With;

@With
public record PathStatistic(int cellsCount, int waspCollected, int musicCollected, int energyUsed) {
}
