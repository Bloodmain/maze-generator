package backend.academy.maze.solver;

import backend.academy.maze.model.Cell;
import lombok.With;

@With
public record EnergySettings(int waspEnergy, int musicEnergy, int passageEnergy) {
    public int getEnergy(Cell.Type type) {
        return switch (type) {
            case WALL -> throw new IllegalArgumentException("Wall does not have energy");
            case WASP -> waspEnergy;
            case MUSIC -> musicEnergy;
            case PASSAGE, ENTRANCE, EXIT -> passageEnergy;
        };
    }
}
