package pl.gozdzikowski.pawel.adventofcode.day12;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class HotSprings {

    public Long calculateSumOfArrangement(Input input) {
        return input.get().stream()
                .map(this::calculateForSingleLine)
                .mapToLong(Long::longValue)
                .sum();
    }

    public Long calculateForSingleLine(String line) {
        SingleEntry singleEntry = convertToSingleEntry(line);
        return calculateForSingleEntry(singleEntry);
    }

    private Long calculateForSingleEntry(SingleEntry singleEntry) {
        List<String> fullyFilled = getFullyFilled(singleEntry);
        return fullyFilled.stream()
                .filter((e) -> calculateNumsOfBroken(e).equals(singleEntry.additionalInfo()))
                .count();
    }

    private static List<String> getFullyFilled(SingleEntry singleEntry) {
        Deque<String> toChange = new LinkedList<>();
        toChange.add(singleEntry.elements());
        List<String> fullyFilled = new LinkedList<>();
        while(!toChange.isEmpty()) {
            String currentEntry = toChange.pollLast();
            if(currentEntry.contains("?")) {
                for(String c: List.of(".", "#")) {
                    String replaced = currentEntry.replaceFirst("\\?", c);
                    if(!replaced.contains("?")) {
                        fullyFilled.add(replaced);
                    } else {
                        toChange.add(replaced);
                    }
                }
            }
        }
        return fullyFilled;
    }

    List<Integer> calculateNumsOfBroken(String entry) {
        List<Integer> numsOfBroken = new ArrayList<>();
        int index = 0;
        int currentNumOfBroken = 0;
        while (index < entry.length()) {
            entry.charAt(index);
            if(entry.charAt(index) == '#')
                currentNumOfBroken++;
            else if(currentNumOfBroken > 0) {
                numsOfBroken.add(currentNumOfBroken);
                currentNumOfBroken = 0;
            }
            index++;
        }
        if(currentNumOfBroken > 0)
            numsOfBroken.add(currentNumOfBroken);
        return numsOfBroken;
    }

    private SingleEntry convertToSingleEntry(String line) {
        String[] splited = line.split("\\s");
        List<Integer> additionalInfo = Arrays.stream(splited[1].split(",")).map(String::trim).map(Integer::valueOf).toList();
        return new SingleEntry(splited[0], additionalInfo);
    }


    public record SingleEntry(
            String elements,
            List<Integer> additionalInfo
    ) {}
}
