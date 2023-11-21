package pl.gozdzikowski.pawel.adventofcode.shared.input;

import java.util.List;

public class ListInput implements Input {
    private List<String> inputs;
    public ListInput(List<String> inputs) {
        this.inputs = inputs;
    }
    @Override
    public List<String> get() {
        return inputs;
    }

    @Override
    public String getContent() {
        throw new UnsupportedOperationException("Unsupported operation for list input");
    }
}
