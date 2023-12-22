package pl.gozdzikowski.pawel.adventofcode.day21;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StepCounter {

    public long calculateNumOfPlots(Input input, int steps) {
        String[][] array = convertTo2DArray(input.getContent());
        Pair<Integer, Integer> startingPosition = findStartingPosition(array);
        return calculateVisitedPlots(array, startingPosition, 0, steps).size();
    }

    private Set<Pair<Integer, Integer>> calculateVisitedPlots(
            String[][] array,
            Pair<Integer, Integer> currentPosition,
            int step,
            int maxSteps
    ) {

        List<Pair<Integer, Integer>> nextToVisit = relativePositions(currentPosition, array).stream().filter(
                (el) -> List.of("S", ".").contains(array[el.right()][el.left()])
        ).toList();


        if(step + 1>= maxSteps)
            return new HashSet<>(nextToVisit);


        return nextToVisit.stream().flatMap((el) -> calculateVisitedPlots(array, el, step + 1, maxSteps).stream()).collect(Collectors.toSet());
    }

    String[][] convertTo2DArray(String input) {
        return Arrays.stream(input.split("\\n"))
                .map((el) -> Arrays.stream(el.split("")).toArray(String[]::new))
                .toArray(String[][]::new);
    }

    Pair<Integer, Integer> findStartingPosition(String[][] array) {
        for (int y = 0; y < array.length; ++y) {
            for (int x = 0; x < array[y].length; ++x) {
                if (array[y][x].equals("S")) {
                    return Pair.of(x, y);
                }
            }
        }

        return null;
    }


    Set<Pair<Integer, Integer>> relativePositions(Pair<Integer, Integer> currentPosition, String[][] array) {
        return Stream.of(Pair.of(currentPosition.left() - 1, currentPosition.right()),
                        Pair.of(currentPosition.left(), currentPosition.right() + 1),
                        Pair.of(currentPosition.left(), currentPosition.right() - 1),
                        Pair.of(currentPosition.left() + 1, currentPosition.right())
                )
                .filter((el) -> (el.left() >=0 && el.left() < array[0].length) && (el.right() >=0 && el.right() < array.length) )
                .collect(Collectors.toSet());
    }
}
