package pl.gozdzikowski.pawel.adventofcode.day11;

import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CosmicExpansion {

    public Long findSumOfShortestPaths(Input input, Integer multiplier) {
        List<List<String>> array = Arrays.stream(input.getContent().split("\\n"))
                .map((el) -> Arrays.stream(el.split("")).collect(Collectors.toCollection(LinkedList::new)))
                .collect(Collectors.toCollection(LinkedList::new));

        List<Integer> rowsIndexesToBeDoubled = findEmptyRows(array);
        List<Integer> columnsIndexesToBeDoubled = findEmptyColumns(array);

        List<Position> galaxyPositions = new ArrayList<>();
        for (int y = 0; y < array.size(); y++) {
            for (int x = 0; x < array.get(y).size(); x++) {
                Position currentPosition = new Position(x, y);
                if (array.get(y).get(x).equals("#")) {
                    List<Integer> lowerXThanCurrent = columnsIndexesToBeDoubled.stream().filter((el) -> el < currentPosition.x).toList();
                    List<Integer> lowerYThanCurrent = rowsIndexesToBeDoubled.stream().filter((el) -> el < currentPosition.y).toList();
                    galaxyPositions.add(
                            new Position(currentPosition.x + (multiplier - 1)  * lowerXThanCurrent.size(), currentPosition.y + (multiplier - 1) * lowerYThanCurrent.size()));
                }
            }
        }

        List<Pair<Position, Position>> galaxyPairs = new ArrayList<>();

        for (int i = 0; i < galaxyPositions.size() - 1; ++i) {
            for (int j = i + 1; j < galaxyPositions.size(); j++) {
                galaxyPairs.add(Pair.of(galaxyPositions.get(i), galaxyPositions.get(j)));
            }
        }


        return galaxyPairs.stream()
                .map(this::manhattanDistance)
                .mapToLong(Long::longValue).sum();
    }

    private static List<Integer> findEmptyRows(List<List<String>> array) {
        List<Integer> rowsIndexesToBeDoubled = new ArrayList<>();

        for (int y = 0; y < array.size(); y++) {
            boolean shouldBeDoubled = true;
            for (int x = 0; x < array.get(y).size(); x++) {
                if (array.get(y).get(x).equals("#"))
                    shouldBeDoubled = false;
            }
            if (shouldBeDoubled)
                rowsIndexesToBeDoubled.add(y);
        }
        return rowsIndexesToBeDoubled;
    }

    private static List<Integer> findEmptyColumns(List<List<String>> array) {
        List<Integer> columnsIndexesToBeDoubled = new ArrayList<>();
        for (int x = 0; x < array.get(0).size(); x++) {
            boolean shouldBeDoubled = true;
            for (int y = 0; y < array.size(); y++) {
                if (array.get(y).get(x).equals("#"))
                    shouldBeDoubled = false;
            }
            if (shouldBeDoubled)
                columnsIndexesToBeDoubled.add(x);
        }
        return columnsIndexesToBeDoubled;
    }

    private long manhattanDistance(Pair<Position, Position> pair) {
        return Math.abs(pair.right().x() - pair.left().x()) + Math.abs(pair.right().y() - pair.left().y());
    }


    record Position(
            int x,
            int y
    ) {
    }
}
