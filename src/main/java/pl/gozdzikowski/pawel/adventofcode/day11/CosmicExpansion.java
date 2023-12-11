package pl.gozdzikowski.pawel.adventofcode.day11;

import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CosmicExpansion {

    public Integer findSumOfShortestPaths(Input input) {
        List<List<String>> array = Arrays.stream(input.getContent().split("\\n"))
                .map((el) -> Arrays.stream(el.split("")).collect(Collectors.toCollection(LinkedList::new)))
                .collect(Collectors.toCollection(LinkedList::new));
        DefaultUndirectedWeightedGraph<Position, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);


        List<Integer> rowsIndexesToBeDoubled = new ArrayList<>();
        List<Integer> columnsIndexesToBeDoubled = new ArrayList<>();
        for (int y = 0; y < array.size(); y++) {
            boolean shouldBeDoubled = true;
            for (int x = 0; x < array.get(y).size(); x++) {
                if (array.get(y).get(x).equals("#"))
                    shouldBeDoubled = false;
            }
            if (shouldBeDoubled)
                rowsIndexesToBeDoubled.add(y);
        }

        for (int x = 0; x < array.get(0).size(); x++) {
            boolean shouldBeDoubled = true;
            for (int y = 0; y < array.size(); y++) {
                if (array.get(y).get(x).equals("#"))
                    shouldBeDoubled = false;
            }
            if (shouldBeDoubled)
                columnsIndexesToBeDoubled.add(x);
        }

        rowsIndexesToBeDoubled.stream().forEach((idx) ->
                array.add(idx + rowsIndexesToBeDoubled.indexOf(idx), new LinkedList<>(Collections.nCopies(array.get(0).size(), ".")))
        );

        for (Integer columnIndex : columnsIndexesToBeDoubled) {
            for (int y = 0; y < array.size(); y++) {
                array.get(y).add(columnIndex + columnsIndexesToBeDoubled.indexOf(columnIndex), ".");
            }
        }

        List<Position> galaxyPositions = new ArrayList<>();
        for (int y = 0; y < array.size(); y++) {
            for (int x = 0; x < array.get(y).size(); x++) {
                Position currentPosition = new Position(x, y);
                relativePositions(array, currentPosition)
                        .stream()
                        .forEach((pos) -> {
                            if (!graph.containsVertex(currentPosition))
                                graph.addVertex(currentPosition);

                            if (!graph.containsVertex(pos))
                                graph.addVertex(pos);

                            if (!graph.containsEdge(currentPosition, pos)) {
                                DefaultWeightedEdge edgeWeight = new DefaultWeightedEdge();
                                graph.addEdge(currentPosition, pos, edgeWeight);
                                graph.setEdgeWeight(edgeWeight, 1);
                            }
                        });
                if (array.get(y).get(x).equals("#")) {
                    galaxyPositions.add(currentPosition);
                }
            }
        }

        BellmanFordShortestPath<Position, DefaultWeightedEdge> dijkstraAlg =
                new BellmanFordShortestPath<>(graph);

        List<Pair<Position, Position>> galaxyPairs = new ArrayList<>();

        for (int i = 0; i < galaxyPositions.size() - 1; ++i) {
            for (int j = i + 1; j < galaxyPositions.size(); j++) {
                galaxyPairs.add(Pair.of(galaxyPositions.get(i), galaxyPositions.get(j)));
            }
        }


        return galaxyPairs.stream()
                .map((pair) -> dijkstraAlg.getPath(pair.left(), pair.right()).getLength())
                .mapToInt(Integer::intValue).sum();
    }

    Set<Position> relativePositions(List<List<String>> arr, Position currentPosition) {
        return Stream.of(new Position(currentPosition.x - 1, currentPosition.y),
                        new Position(currentPosition.x, currentPosition.y + 1),
                        new Position(currentPosition.x, currentPosition.y - 1),
                        new Position(currentPosition.x + 1, currentPosition.y)
                ).filter((pos) -> (pos.x >= 0 && pos.x < arr.get(0).size()) &&
                        (pos.y >= 0 && pos.y < arr.size()))
                .collect(Collectors.toSet());
    }


    record Position(
            int x,
            int y
    ) {
    }
}
