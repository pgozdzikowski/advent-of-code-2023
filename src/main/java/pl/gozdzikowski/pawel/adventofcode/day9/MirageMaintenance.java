package pl.gozdzikowski.pawel.adventofcode.day9;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MirageMaintenance {

    public Integer findResultFromEnd(Input input) {
        return findResult(input, this::findValueInHistory);
    }

    public Integer findResultFromBegin(Input input) {
        return findResult(input, this::findValueInHistoryBegining);
    }

    private Integer findResult(Input input, Function<List<Integer>, Integer> func) {
        return input.get().stream()
                .map((el) -> func.apply(Arrays.stream(el.split("\\s")).map(Integer::valueOf).toList()))
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer findValueInHistory(List<Integer> numbers) {
        if (numbers.stream().allMatch((el) -> el.equals(0))) {
            return 0;
        }
        List<Integer> copiedList = new ArrayList<>(numbers.size() - 1);
        for (int i = 0; i < numbers.size() - 1; i++) {
            copiedList.add(0);
        }

        for (int i = 1; i < numbers.size(); ++i) {
            copiedList.set(i - 1, numbers.get(i) - numbers.get(i - 1));
        }

        return numbers.getLast() + findValueInHistory(copiedList);
    }

    public Integer findValueInHistoryBegining(List<Integer> numbers) {
        if (numbers.stream().allMatch((el) -> el.equals(0))) {
            return 0;
        }
        List<Integer> copiedList = new ArrayList<>(numbers.size() - 1);
        for (int i = 0; i < numbers.size() - 1; i++) {
            copiedList.add(0);
        }

        for (int i = 1; i < numbers.size(); ++i) {
            copiedList.set(i - 1, numbers.get(i) - numbers.get(i - 1));
        }

        return numbers.getFirst() - findValueInHistoryBegining(copiedList);
    }
}
