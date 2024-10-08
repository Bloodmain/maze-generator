package backend.academy.maze;

import backend.academy.maze.generator.GeneralMazeGenerator;
import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.generator.structure.CyclesGenerator;
import backend.academy.maze.generator.structure.Generator;
import backend.academy.maze.generator.structure.MazeExitEntranceGenerator;
import backend.academy.maze.generator.structure.MazeSurfacesGenerator;
import backend.academy.maze.model.Coordinate;
import backend.academy.maze.model.Maze;
import backend.academy.maze.renderer.Renderer;
import backend.academy.maze.renderer.SymbolicRenderer;
import backend.academy.maze.solver.EnergySettings;
import backend.academy.maze.solver.PathStatistic;
import backend.academy.maze.solver.PathStatisticCollector;
import backend.academy.maze.solver.Solver;
import backend.academy.maze.ui.ConsoleUI;
import backend.academy.maze.ui.SettingsConfigurator;
import backend.academy.maze.ui.UI;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try (
            Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
            OutputStreamWriter out = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
        ) {
            UI ui = new ConsoleUI(in, out);
            SettingsConfigurator settingsConfigurator = new SettingsConfigurator(ui);
            GeneratorSettings settings = settingsConfigurator.configureGeneratorSettings();

            Generator mazeGenerator = settingsConfigurator.configureMazeGenerator();

            List<Generator> generators = List.of(
                mazeGenerator, new CyclesGenerator(),
                new MazeSurfacesGenerator(), new MazeExitEntranceGenerator()
            );
            Maze maze = new GeneralMazeGenerator().generate(generators, settings);

            Renderer renderer = new SymbolicRenderer();

            ui.display("The generated maze: ");
            ui.display(renderer.render(maze));
            ui.display("""
                The goal is to go all the way from the entrance to the exit using as minimal energy as possible.
                Every type of the cell (empty, wasp, music) has its own energy usage.
                """
            );

            EnergySettings energySettings = settingsConfigurator.configureEnergySettings();

            Solver solver = settingsConfigurator.configureMazeSolver();
            List<Coordinate> path = solver.solve(maze, energySettings);

            ui.display("Solved maze: ");
            ui.display(renderer.render(maze, path));

            PathStatistic statisticResult =
                new PathStatisticCollector().collectStatistics(maze, path, energySettings);

            ui.display(renderer.render(statisticResult));
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Maze IO exception: " + e.getMessage());
        }
    }
}
