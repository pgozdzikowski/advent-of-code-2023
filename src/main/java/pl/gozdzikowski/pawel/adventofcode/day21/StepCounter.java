package pl.gozdzikowski.pawel.adventofcode.day21;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StepCounter {

    public long calculateNumOfPlots(Input input, int steps) {
        String[][] array = convertTo2DArray(input.getContent());
        Pair<Integer, Integer> startingPosition = findStartingPosition(array);
        return calculateVisitedPlots(array, startingPosition, steps);
    }

    private long calculateVisitedPlots(
            String[][] array,
            Pair<Integer, Integer> currentPosition,
            int maxSteps
    ) {

        Deque<PositionWithSteps> toVisit = new LinkedList<>();
        toVisit.add(new PositionWithSteps(currentPosition, 0));
        Map<Pair<Integer, Integer>, Integer> visited = new HashMap<>();

        while (!toVisit.isEmpty()) {
            PositionWithSteps currentPoint = toVisit.poll();

            if(visited.containsKey(currentPoint.position()))
                continue;

            List<PositionWithSteps> nextToVisit = relativePositions(currentPoint.position(), array)
                    .stream().filter((el) -> List.of("S", ".").contains(array[el.right()][el.left()]))
                    .map((el) -> new PositionWithSteps(el, currentPoint.steps() + 1))
                    .filter((el) -> el.steps() <= maxSteps)
                    .toList();

            visited.put(currentPoint.position(), currentPoint.steps());
            toVisit.addAll(nextToVisit);
        }

        return visited.values().stream()
                .filter((el) -> el % 2 == 0)
                .count();
    }

    record PositionWithSteps(
        Pair<Integer, Integer> position,
        int steps
    ) {

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
                .filter((el) -> (el.left() >= 0 && el.left() < array[0].length) && (el.right() >= 0 && el.right() < array.length))
                .collect(Collectors.toSet());
    }
}
