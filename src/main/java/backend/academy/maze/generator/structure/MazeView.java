package backend.academy.maze.generator.structure;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;

/**
 * Builds a maze surrounding all the cells with 8 walls.
 * Provides a "view" to access cells like there are no these walls.
 * Example:
 *
 * <p>#####</p>
 * <p>#-#-#</p>
 * <p>#####</p>
 * <p>#-#-#</p>
 * <p>#####</p>
 * "-" here is a cell at "view", "#" is a wall.
 * <p> Every view cell has a corner (right and bottom walls).
 * Also adds the walls in the first row and first column.
 * <p>
 * The height and the width should be odd, otherwise they are being decreased by 1
 * in order not to litter the screen with unnecessary walls.
 */
public class MazeView {
    private final Cell.Type[][] grid;
    private final int realHeight;
    private final int realWidth;

    @Getter
    private final int viewHeight;
    @Getter
    private final int viewWidth;

    /**
     * Creates a new vies, decreasing the side size by 1 in case of even sizes
     * in order not to litter the screen with unnecessary walls.
     *
     * @param height height of the maze
     * @param width  width of the maze
     */
    public MazeView(int height, int width) {
        realHeight = getOdd(height);
        realWidth = getOdd(width);

        viewHeight = realHeight / 2;
        viewWidth = realWidth / 2;

        grid = new Cell.Type[realHeight][realWidth];
        for (int i = 0; i < realHeight; i++) {
            for (int j = 0; j < realWidth; j++) {
                if (i % 2 != 0 && j % 2 != 0) {
                    // view cell
                    grid[i][j] = Cell.Type.PASSAGE;
                } else {
                    grid[i][j] = Cell.Type.WALL;
                }
            }
        }

    }

    /**
     * Connects two neighbours cells with the passage,
     * i.e. removes two surrounding walls to make a passage between these points
     *
     * @param c1 first point in "view" coordinates
     * @param c2 second point in "view" coordinates
     */
    public void connectCells(Coordinate c1, Coordinate c2) {
        Coordinate connectingWall = getCellsWallCoordinate(c1, c2);
        grid[connectingWall.row()][connectingWall.col()] = Cell.Type.PASSAGE;
    }

    public boolean isCellsConnected(Coordinate c1, Coordinate c2) {
        Coordinate connectingWall = getCellsWallCoordinate(c1, c2);
        return grid[connectingWall.row()][connectingWall.col()] == Cell.Type.PASSAGE;
    }

    public void assignCellType(Coordinate c, Cell.Type type) {
        if (c.row() >= 0 && c.row() < viewHeight && c.col() >= 0 && c.col() < viewWidth) {
            Coordinate realCoords = translateCoords(c);
            grid[realCoords.row()][realCoords.col()] = type;
        } else {
            throw new IllegalArgumentException("Cell is out of bounds");
        }
    }

    @SuppressFBWarnings(
        value = "CLI_CONSTANT_LIST_INDEX",
        justification = "we should access the grid by constant"
    )
    public void setExitEntrance(ExitEntrancePosition pos, Cell.Type type) {
        switch (pos) {
            // making the '@' point exit/entrance
            /*
            #
            @.
            ###
             */
            case LEFT_LOWER_CORNER -> grid[realHeight - 2][0] = type;

            /*
            #@#
            #.
            #
             */
            case LEFT_UPPER_CORNER -> grid[0][1] = type;

            /*
              #
             .#
            #@#
             */
            case RIGHT_LOWER_CORNER -> grid[realHeight - 1][realWidth - 2] = type;

            /*
            ###
             .@
              #
             */
            case RIGHT_UPPER_CORNER -> grid[1][realWidth - 1] = type;
            default -> throw new IllegalArgumentException("Invalid exitEntrance position");
        }
    }

    /**
     * Gives a real maze.
     *
     * @return Maze with cells surrounded by walls
     */
    public Maze getRealMaze() {
        Cell[][] realGrid = new Cell[realHeight][realWidth];
        for (int i = 0; i < realHeight; i++) {
            for (int j = 0; j < realWidth; j++) {
                realGrid[i][j] = new Cell(i, j, grid[i][j]);
            }
        }
        return new Maze(realHeight, realWidth, realGrid);
    }

    /**
     * Translates "view" coordinates to the real coordinate
     *
     * @param c coordinates to translate
     * @return translated coordinates
     */
    private Coordinate translateCoords(Coordinate c) {
        return new Coordinate(2 * c.row() + 1, 2 * c.col() + 1);
    }

    private static int getOdd(int a) {
        return a % 2 == 0 ? a - 1 : a;
    }

    /**
     * Returns real coordinate of the cell, that contains wall between cell a and cell b.
     * Throws an exception if a and b are not neighbours.
     *
     * @param a first cell
     * @param b second cell
     * @return coordinate of the connecting wall
     */
    private Coordinate getCellsWallCoordinate(Coordinate a, Coordinate b) {
        if (a.col() >= b.col() && (a.col() != b.col() || a.row() >= b.row())) {
            return getCellsWallCoordinate(b, a);
        }

        Coordinate realA = translateCoords(a);
        if (a.row() == b.row() && a.col() + 1 == b.col()) {
            /* ...
               .AB
               ... */
            return realA.withCol(realA.col() + 1);
        } else if (a.col() == b.col() && a.row() + 1 == b.row()) {
             /* ...
                .A.
                .B. */
            return realA.withRow(realA.row() + 1);
        } else {
            throw new IllegalArgumentException("Cells are not neighbours");
        }
    }
}
