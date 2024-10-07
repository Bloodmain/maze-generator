package backend.academy.maze.model;

public record Cell(int row, int col, Type type) {
    public enum Type {
        WALL, PASSAGE, WASP, MUSIC, EXIT, ENTRANCE
    }
}
