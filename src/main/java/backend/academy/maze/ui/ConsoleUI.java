package backend.academy.maze.ui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsoleUI implements UI {
    private final Scanner reader;
    private final OutputStreamWriter writer;

    @Override
    public void display(String message) throws IOException {
        writer.write(message);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    @Override
    public String getInput() {
        return reader.nextLine();
    }
}
