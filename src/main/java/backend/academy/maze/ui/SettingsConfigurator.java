package backend.academy.maze.ui;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.generator.structure.ExitEntrancePosition;
import backend.academy.maze.generator.structure.Generator;
import backend.academy.maze.generator.structure.KruskalGenerator;
import backend.academy.maze.generator.structure.RecursiveBacktrackingGenerator;
import backend.academy.maze.solver.DijkstraSolver;
import backend.academy.maze.solver.EnergySettings;
import backend.academy.maze.solver.FordBellmanSolver;
import backend.academy.maze.solver.Solver;
import com.google.common.base.Predicates;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SettingsConfigurator {
    private static final Map<String, Supplier<ExitEntrancePosition>> POSITION_NAMES = Map.of(
        "Left upper corner", () -> ExitEntrancePosition.LEFT_UPPER_CORNER,
        "Right upper corner", () -> ExitEntrancePosition.RIGHT_UPPER_CORNER,
        "Right lower corner", () -> ExitEntrancePosition.RIGHT_LOWER_CORNER,
        "Left lower corner", () -> ExitEntrancePosition.LEFT_LOWER_CORNER
    );
    private static final Map<String, Supplier<Generator>> GENERATOR_NAMES = Map.of(
        "Kruskal generator", KruskalGenerator::new,
        "Recursive backtracking generator", RecursiveBacktrackingGenerator::new
    );
    private static final Map<String, Supplier<Solver>> SOLVERS_NAMES = Map.of(
        "Dijkstra solver", DijkstraSolver::new,
        "Ford-Bellman solver", FordBellmanSolver::new
    );
    private static final int MINIMAL_MAZE_SIZE = 3;

    private static final GeneratorSettings DEFAULT_GENERATOR_SETTINGS = new GeneratorSettings(
        20, 60,
        0.2, 0.2, 0.1,
        ExitEntrancePosition.LEFT_LOWER_CORNER, ExitEntrancePosition.RIGHT_LOWER_CORNER
    );
    private static final EnergySettings DEFAULT_ENERGY_SETTINGS = new EnergySettings(
        10, 3, 5
    );

    private static final String START_CONFIGURING_BUTTON = "y";

    private final UI ui;

    private boolean askForSettings(String title) throws IOException {
        ui.display(
            "Do you want to set %s? Enter \"%s\" for configuring: ".formatted(title, START_CONFIGURING_BUTTON)
        );

        return START_CONFIGURING_BUTTON.equals(ui.getInput());
    }

    private <T> T askForNumber(
        Function<String, T> parser,
        Predicate<T> isCorrectNumber,
        String errorMessage
    ) throws IOException {
        String input = ui.getInput();
        while (true) {
            try {
                T value = parser.apply(input);
                if (isCorrectNumber.test(value)) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }

            ui.display(errorMessage);
            input = ui.getInput();
        }
    }

    public EnergySettings configureEnergySettings() throws IOException {
        EnergySettings settings = DEFAULT_ENERGY_SETTINGS;
        if (!askForSettings("energy settings")) {
            return settings;
        }

        ui.display("Enter energy consumption for the empty cell (integer >= 1)");
        settings = settings.withPassageEnergy(
            askForNumber(Integer::parseInt, i -> i >= 1, "Please, enter an integer >= 1 for empty cell energy:")
        );

        ui.display("Enter energy consumption for the cells with bad surfaces (integer >= 1)");
        settings = settings.withWaspEnergy(
            askForNumber(Integer::parseInt, i -> i >= 1,
                "Please, enter an integer >= 1 for cells with bad surfaces energy:")
        );

        ui.display("Enter energy consumption for the cells with good surfaces (integer >= 1)");
        settings = settings.withMusicEnergy(
            askForNumber(Integer::parseInt, i -> i >= 1,
                "Please, enter an integer >= 1 for cells with good surfaces energy:")
        );

        return settings;
    }

    public GeneratorSettings configureGeneratorSettings() throws IOException {
        GeneratorSettings settings = DEFAULT_GENERATOR_SETTINGS;
        if (!askForSettings("generator settings")) {
            return settings;
        }

        ui.display("Enter maze height >= %d (better to use odd number):".formatted(MINIMAL_MAZE_SIZE));
        settings = settings.withHeight(
            askForNumber(Integer::parseInt, i -> i >= MINIMAL_MAZE_SIZE, "Please, enter a positive integer for height:")
        );

        ui.display("Enter maze width >= %d (better to use odd number):".formatted(MINIMAL_MAZE_SIZE));
        settings = settings.withWidth(
            askForNumber(Integer::parseInt, i -> i >= MINIMAL_MAZE_SIZE, "Please, enter a positive integer for width:")
        );

        ui.display("Enter portion of cycles in the maze (real number from 0 to 1):");
        settings = settings.withCyclesPortion(
            askForNumber(Double::parseDouble, d -> 0 <= d && d <= 1, "Please, enter a double from 0 to 1 for cycles:")
        );

        ui.display("Enter portion of bad surfaces in the maze (real number from 0 to 1):");
        double badTypesPortion =
            askForNumber(Double::parseDouble, d -> 0 <= d && d <= 1,
                "Please, enter a double from 0 to 1 for bad surfaces:");
        settings = settings.withBadTypesPortion(badTypesPortion);

        ui.display("Enter portion of good surfaces in the maze "
            + "(real number from 0 to 1, bad surfaces portion + good surfaces portion should not be more than 1):");
        settings = settings.withGoodTypesPortion(
            askForNumber(Double::parseDouble, d -> 0 <= d && d + badTypesPortion <= 1,
                "Please, enter a real number from 0 to 1 which sum with bad surfaces portion does not exceed 1:")
        );

        ExitEntrancePosition entrancePosition = askFromList(POSITION_NAMES, "start position");
        settings = settings.withStart(entrancePosition);
        settings = settings.withFinish(askFromListCustomizable(
            POSITION_NAMES,
            "finish position",
            "that does not equal to the start position",
            supp -> supp.get() != entrancePosition
        ));

        return settings;
    }

    private <T> T askFromListCustomizable(
        Map<String, Supplier<T>> names,
        String title,
        String customPrompt,
        Predicate<Supplier<T>> customPredicate
    ) throws IOException {
        var list = names.entrySet().stream().toList();
        var stringList = Stream.iterate(0, i -> i < list.size(), i -> i + 1)
            .map(i -> "%d. %s".formatted(i + 1, list.get(i).getKey()))
            .collect(Collectors.joining(System.lineSeparator()));

        ui.display("""
            Choose the %s:%n\
            %s%n\
            Enter a number from 1 to %d %s:"""
            .formatted(title, stringList, list.size(), customPrompt));
        int genNum = askForNumber(
            Integer::parseInt, i -> i >= 1 && i <= list.size() && customPredicate.test(
                list.get(i - 1).getValue()
            ),
            "Please, enter a number from 1 to %d:".formatted(list.size())
        );

        return list.get(genNum - 1).getValue().get();
    }

    private <T> T askFromList(Map<String, Supplier<T>> names, String title) throws IOException {
        return askFromListCustomizable(names, title, "", Predicates.alwaysTrue());
    }

    public Generator configureMazeGenerator() throws IOException {
        return askFromList(GENERATOR_NAMES, "maze generator");
    }

    public Solver configureMazeSolver() throws IOException {
        return askFromList(SOLVERS_NAMES, "maze solver");
    }
}
