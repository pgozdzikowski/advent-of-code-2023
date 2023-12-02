package pl.gozdzikowski.pawel.adventofcode.day1;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.Comparator;
import java.util.Map;

public class Trebuchet {

    public static Map<String, Integer> ONLY_DIGITS = Map.ofEntries(
            Map.entry("1", 1),
            Map.entry("2", 2),
            Map.entry("3", 3),
            Map.entry("4", 4),
            Map.entry("5", 5),
            Map.entry("6", 6),
            Map.entry("7", 7),
            Map.entry("8", 8),
            Map.entry("9", 9)
    );

    public static Map<String, Integer> DIGITS_AND_WORDS = Map.ofEntries(
            Map.entry("one", 1),
            Map.entry("two", 2),
            Map.entry("three", 3),
            Map.entry("four", 4),
            Map.entry("five", 5),
            Map.entry("six", 6),
            Map.entry("seven", 7),
            Map.entry("eight", 8),
            Map.entry("nine", 9),
            Map.entry("1", 1),
            Map.entry("2", 2),
            Map.entry("3", 3),
            Map.entry("4", 4),
            Map.entry("5", 5),
            Map.entry("6", 6),
            Map.entry("7", 7),
            Map.entry("8", 8),
            Map.entry("9", 9)
    );

    public Long findSum(Input input, Map<String, Integer> converters) {
        return input.get().stream()
                .map((line) -> findFirstAndLast(line, converters))
                .mapToLong(Long::longValue)
                .sum();
    }

    public Long findFirstAndLast(String input, Map<String, Integer> map) {
        return Long.valueOf("" + findFirstValue(input, map) + findLastValue(input, map));
    }

    public Integer findFirstValue(String input, Map<String, Integer> digits) {
        return digits.keySet().stream()
                .map((key) -> Pair.of(key, input.indexOf(key)))
                .filter((pair) -> pair.right() != -1)
                .min(Comparator.comparing(Pair::right))
                .map((pair) -> digits.get(pair.left()))
                .orElseThrow();
    }

    public Integer findLastValue(String input, Map<String, Integer> digits) {
        return digits.keySet().stream()
                .map((key) -> Pair.of(key, input.lastIndexOf(key)))
                .filter((pair) -> pair.right() != -1)
                .max(Comparator.comparing(Pair::right))
                .map((pair) -> digits.get(pair.left()))
                .orElseThrow();
    }
}
