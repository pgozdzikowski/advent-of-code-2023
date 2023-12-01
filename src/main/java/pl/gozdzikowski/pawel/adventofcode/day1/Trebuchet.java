package pl.gozdzikowski.pawel.adventofcode.day1;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Trebuchet {

    Map<String, Integer> digits = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    public Long findSum(Input input, Function<String, Long> func) {
        return input.get().stream()
                .map(func)
                .mapToLong(Long::longValue)
                .sum();
    }

    public Long findFirstAndLastNumber(String input) {
        List<Character> list = input.chars().mapToObj((val) -> (char) val).toList();
        List<Character> reversed = list.reversed();
        Character first = findFirstDigit(list);
        Character last = findFirstDigit(reversed);
        return Long.valueOf(first.toString() + last.toString());
    }

    private static Character findFirstDigit(List<Character> characters) {
        return characters.stream()
                .filter(Character::isDigit)
                .findFirst()
                .orElse(' ');
    }


    public Long findFirstAndLastSpelled(String input) {
        Integer minOffset = Integer.MAX_VALUE;
        Integer valueWithMin = 0;
        Integer maxOffset = Integer.MIN_VALUE;
        Integer valueWithMax = 0;
        List<String> keys = Stream.concat(digits.keySet().stream(),
                        digits.values().stream().map((val) -> val.toString()))
                .toList();
        for (String key : keys) {
            int index = input.indexOf(key);
            if (index != -1 && index < minOffset) {
                if (Character.isDigit(key.charAt(0))) {
                    valueWithMin = Integer.valueOf(key);
                } else {
                    valueWithMin = digits.get(key);
                }
                minOffset = index;

            }
            int lastIndex = input.lastIndexOf(key);
            if (index != -1 && lastIndex > maxOffset) {
                if (Character.isDigit(key.charAt(0))) {
                    valueWithMax = Integer.valueOf(key);
                } else {
                    valueWithMax = digits.get(key);
                }
                maxOffset = lastIndex;
            }
        }

        return Long.valueOf(valueWithMin.toString() + valueWithMax.toString());
    }
}
