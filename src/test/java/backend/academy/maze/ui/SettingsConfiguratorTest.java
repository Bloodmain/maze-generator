package backend.academy.maze.ui;

import backend.academy.maze.generator.GeneratorSettings;
import backend.academy.maze.solver.EnergySettings;
import org.junit.jupiter.api.Test;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SettingsConfiguratorTest {
    @Test
    public void validEnergyConfiguration() {
        Scanner in = new Scanner("y\n10\n20\n30");
        UI ui = new ConsoleUI(in, new OutputStreamWriter(OutputStream.nullOutputStream()));
        SettingsConfigurator configurator = new SettingsConfigurator(ui);

        EnergySettings settings = assertDoesNotThrow(configurator::configureEnergySettings);

        assertThat(settings.passageEnergy()).isEqualTo(10);
        assertThat(settings.waspEnergy()).isEqualTo(20);
        assertThat(settings.musicEnergy()).isEqualTo(30);
    }

    @Test
    public void invalidTypesGeneratorConfiguration() {
        Scanner in = new Scanner("y\ne\n0.2\n-1\n2\n3\n5\n2\n0.2\n0.3\n0.1\ng\n1\n2");
        UI ui = new ConsoleUI(in, new OutputStreamWriter(OutputStream.nullOutputStream()));
        SettingsConfigurator configurator = new SettingsConfigurator(ui);

        GeneratorSettings settings = assertDoesNotThrow(configurator::configureGeneratorSettings);

        assertThat(settings.height()).isEqualTo(3);
        assertThat(settings.width()).isEqualTo(5);
        assertThat(settings.cyclesPortion()).isEqualTo(0.2);
        assertThat(settings.badTypesPortion()).isEqualTo(0.3);
        assertThat(settings.goodTypesPortion()).isEqualTo(0.1);
    }

    @Test
    public void goodAndBadPortionsExceedOneGeneratorConfiguration() {
        Scanner in = new Scanner("y\n3\n5\n0.2\n0.3\n0.8\n0.1\n1\n2");
        UI ui = new ConsoleUI(in, new OutputStreamWriter(OutputStream.nullOutputStream()));
        SettingsConfigurator configurator = new SettingsConfigurator(ui);

        GeneratorSettings settings = assertDoesNotThrow(configurator::configureGeneratorSettings);

        assertThat(settings.badTypesPortion()).isEqualTo(0.3);
        assertThat(settings.goodTypesPortion()).isEqualTo(0.1);
    }

    @Test
    public void sameEntranceExitPositionGeneratorConfiguration() {
        Scanner in = new Scanner("y\n3\n5\n0.2\n0.3\n0.1\n1\n1\n2");
        UI ui = new ConsoleUI(in, new OutputStreamWriter(OutputStream.nullOutputStream()));
        SettingsConfigurator configurator = new SettingsConfigurator(ui);

        GeneratorSettings settings = assertDoesNotThrow(configurator::configureGeneratorSettings);

        assertThat(settings.start()).isNotEqualTo(settings.finish());
    }
}
