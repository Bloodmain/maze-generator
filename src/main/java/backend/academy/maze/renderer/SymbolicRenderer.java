package backend.academy.maze.renderer;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import backend.academy.maze.solver.PathStatistic;
import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SymbolicRenderer implements Renderer {
    private static final Map<Cell.Type, Function<String, String>> CELL_COLORIZE = Map.of(
        Cell.Type.WALL, (String s) -> Ansi.colorize(s, Attribute.BLACK_BACK()),
        Cell.Type.PASSAGE, (String s) -> Ansi.colorize(s, Attribute.WHITE_BACK()),
        Cell.Type.MUSIC, (String s) -> Ansi.colorize(s, Attribute.WHITE_BACK()),
        Cell.Type.WASP, (String s) -> Ansi.colorize(s, Attribute.WHITE_BACK()),
        Cell.Type.ENTRANCE, (String s) -> Ansi.colorize(s, Attribute.BRIGHT_WHITE_BACK()),
        Cell.Type.EXIT, (String s) -> Ansi.colorize(s, Attribute.BRIGHT_RED_BACK())
    );

    private static final Map<Cell.Type, String> CELL_SYMBOL = Map.of(
        Cell.Type.WALL, Ansi.colorize("#", Attribute.BLACK_TEXT()),
        Cell.Type.PASSAGE, Ansi.colorize("·", Attribute.WHITE_TEXT()),
        Cell.Type.MUSIC, Ansi.colorize("♫", Attribute.BLUE_TEXT()),
        Cell.Type.WASP, Ansi.colorize("⌘", Attribute.MAGENTA_TEXT()),
        Cell.Type.ENTRANCE, Ansi.colorize("I", Attribute.BLACK_TEXT()),
        Cell.Type.EXIT, Ansi.colorize("O", Attribute.BLACK_TEXT())
    );

    @Override
    public String render(Maze maze) {
        return render(maze, Collections.emptyList());
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < maze.height(); ++i) {
            for (int j = 0; j < maze.width(); ++j) {
                Cell cell = maze.cellAt(new Coordinate(i, j));
                String symbol = CELL_SYMBOL.get(cell.type());

                if (path.contains(new Coordinate(i, j))) {
                    sb.append(Ansi.colorize(symbol, Attribute.GREEN_BACK()));
                } else {
                    sb.append(CELL_COLORIZE.get(cell.type()).apply(symbol));
                }
            }
            sb.append(System.lineSeparator());
        }
        sb.append(
            ("\"%s\" stands for the wasp surface, \"%s\" - for the music surface, "
                + "\"%s\" - for the entrance, \"%s\" - for the exit.")
                .formatted(CELL_SYMBOL.get(Cell.Type.WASP), CELL_SYMBOL.get(Cell.Type.MUSIC),
                    CELL_SYMBOL.get(Cell.Type.ENTRANCE), CELL_SYMBOL.get(Cell.Type.EXIT))
        );
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    @Override
    public String render(PathStatistic statisticResult) {
        return """
            Path statistic:%n\
            %d - path size%n\
            %d - wasp surfaces used;%n\
            %d - music surfaces used;%n\
            %n\
            %d - total energy used.%n\
            """
            .formatted(statisticResult.cellsCount(), statisticResult.waspCollected(),
                statisticResult.musicCollected(), statisticResult.energyUsed());
    }
}
