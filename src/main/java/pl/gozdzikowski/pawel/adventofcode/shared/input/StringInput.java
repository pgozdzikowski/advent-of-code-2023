package pl.gozdzikowski.pawel.adventofcode.shared.input;

import java.util.Arrays;
import java.util.List;

public class StringInput implements Input{
    private String content;

    public StringInput(String content) {
        this.content = content;
    }

    @Override
    public List<String> get() {
        return Arrays.stream(content.split("\n")).toList();
    }

    @Override
    public String getContent() {
        return content;
    }
}
