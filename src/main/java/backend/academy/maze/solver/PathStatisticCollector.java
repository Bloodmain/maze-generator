package backend.academy.maze.solver;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import java.util.List;

public class PathStatisticCollector {
    public PathStatistic collectStatistics(Maze maze, List<Coordinate> path, EnergySettings energySettings) {
        PathStatistic result = new PathStatistic(0, 0, 0, 0);

        for (Coordinate coordinate : path) {
            Cell.Type type = maze.cellAt(coordinate).type();

            switch (type) {
                case WASP -> {
                    result = result.withWaspCollected(result.waspCollected() + 1);
                    result = result.withEnergyUsed(result.energyUsed() + energySettings.waspEnergy());
                }
                case MUSIC -> {
                    result = result.withMusicCollected(result.musicCollected() + 1);
                    result = result.withEnergyUsed(result.energyUsed() + energySettings.musicEnergy());
                }
                case PASSAGE, ENTRANCE, EXIT -> {
                    result = result.withEnergyUsed(result.energyUsed() + energySettings.passageEnergy());
                }
                default -> {
                    throw new IllegalArgumentException("Path should not contain walls");
                }
            }

            result = result.withCellsCount(result.cellsCount() + 1);
        }

        return result;
    }
}
