package backend.academy.maze.ui;

import java.io.IOException;

public interface UI {
    void display(String message) throws IOException;

    String getInput() throws IOException;
}
