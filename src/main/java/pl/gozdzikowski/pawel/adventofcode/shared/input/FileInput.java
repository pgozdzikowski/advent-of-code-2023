package pl.gozdzikowski.pawel.adventofcode.shared.input;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class FileInput implements Input {
    private final File file;
    public FileInput(String path) throws URISyntaxException {
        file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(path)).toURI());
    }

    @Override
    public List<String> get() {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getContent() {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
