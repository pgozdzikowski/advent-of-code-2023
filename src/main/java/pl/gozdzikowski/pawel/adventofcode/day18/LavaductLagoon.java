package pl.gozdzikowski.pawel.adventofcode.day18;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LavaductLagoon {

    public Integer calculateCubic(Input input) {
        List<String> commands = Arrays.stream(input.getContent().split("\\n")).toList();
        List<Edge> edges = createEdges(commands);
        BoundedBox boundedBox = new BoundedBox(
                edges.stream().flatMap((el) -> Stream.of(el.start(), el.end())).mapToInt(Pair::left).min().getAsInt(),
                edges.stream().flatMap((el) -> Stream.of(el.start(), el.end())).mapToInt(Pair::left).max().getAsInt(),
                edges.stream().flatMap((el) -> Stream.of(el.start(), el.end())).mapToInt(Pair::right).min().getAsInt(),
                edges.stream().flatMap((el) -> Stream.of(el.start(), el.end())).mapToInt(Pair::right).max().getAsInt()
        );

        Set<Pair<Integer, Integer>> visitedPoints = new HashSet<>();
        for (int y = boundedBox.minY; y < boundedBox.maxY && visitedPoints.isEmpty(); ++y) {
            for (int x = boundedBox.minX; x < boundedBox.maxX && visitedPoints.isEmpty(); ++x) {
                visitedPoints = visitPoints(edges, Pair.of(x, y), new HashSet<>(), boundedBox);
            }
        }

        visitedPoints.addAll(edges.stream().flatMap((el) -> el.pointsBetween().stream()).collect(Collectors.toSet()));

//        String[][] arr = new String[boundedBox.maxY][boundedBox.maxX];
//
//        for (int y = 0; y < arr.length; ++y) {
//            for (int x = 0; x < arr[y].length; ++x) {
//                int finalX = x;
//                int finalY = y;
//                if(edges.stream().anyMatch((el) -> el.containsPoint(Pair.of(finalX, finalY))))
//                    System.out.print("#");
//                else
//                    System.out.print('.');
//            }
//            System.out.println();
//        }

        return visitedPoints.size();
    }

    public Set<Pair<Integer, Integer>> visitPoints(List<Edge> edges,
                                                   Pair<Integer, Integer> currentPoint,
                                                   Set<Pair<Integer, Integer>> visitedPoints,
                                                   BoundedBox boundedBox) {
        Stack<Pair<Integer, Integer>> pointsToConsider = new Stack<>();
        pointsToConsider.push(currentPoint);

        if(edges.stream().anyMatch((el) -> el.containsPoint(currentPoint)))
            return Collections.emptySet();

        while(!pointsToConsider.isEmpty()) {
            Pair<Integer, Integer> pointToCosnider = pointsToConsider.pop();
            if (!boundedBox.isInside(pointToCosnider)) {
                return Collections.emptySet();
            }
            visitedPoints.add(pointToCosnider);

            List<Pair<Integer, Integer>> pointsToVisit = relativePositions(pointToCosnider)
                    .stream().filter((el) -> !visitedPoints.contains(el) && edges.stream().noneMatch((edge) -> edge.containsPoint(el)))
                    .collect(Collectors.toCollection(LinkedList::new));

            pointsToConsider.addAll(pointsToVisit);
        }

        return visitedPoints;
    }


    private static List<Edge> createEdges(List<String> commands) {
        List<Edge> edges = new ArrayList<>();
        Pair<Integer, Integer> initialPosition = Pair.of(0, 0);
        Pair<Integer, Integer> currentPosition = initialPosition;
        for (String command : commands) {
            String[] splited = command.split("\\s");
            switch (splited[0]) {
                case "L" -> {
                    Pair<Integer, Integer> dest = Pair.of(currentPosition.left() - Integer.parseInt(splited[1]), currentPosition.right());
                    edges.add(new Edge(currentPosition, dest, splited[2]));
                    currentPosition = dest;
                }
                case "R" -> {
                    Pair<Integer, Integer> dest = Pair.of(currentPosition.left() + Integer.parseInt(splited[1]), currentPosition.right());
                    edges.add(new Edge(currentPosition, dest, splited[2]));
                    currentPosition = dest;
                }
                case "D" -> {
                    Pair<Integer, Integer> dest = Pair.of(currentPosition.left(), currentPosition.right() + Integer.parseInt(splited[1]));
                    edges.add(new Edge(currentPosition, dest, splited[2]));
                    currentPosition = dest;
                }
                case "U" -> {
                    Pair<Integer, Integer> dest = Pair.of(currentPosition.left(), currentPosition.right() - Integer.parseInt(splited[1]));
                    edges.add(new Edge(currentPosition, dest, splited[2]));
                    currentPosition = dest;
                }
            }
        }
        return edges;
    }

    record Edge(
            Pair<Integer, Integer> start,
            Pair<Integer, Integer> end,
            String color
    ) {

        Edge {
            if (start.left() > end.left() || start.right() > end.right()) {
                Pair<Integer, Integer> tmp = end;
                end = start;
                start = tmp;
            }
        }

        public boolean isHorizontal() {
            return start().right().equals(end().right());
        }

        public Set<Pair<Integer, Integer>> pointsBetween() {
            if(isHorizontal()) {
                return IntStream.rangeClosed(start().left(), end().left()).mapToObj((el) -> Pair.of(el, start().right())).collect(Collectors.toSet());
            }
            return IntStream.rangeClosed(start().right(), end().right()).mapToObj((el) -> Pair.of(start.left(), el)).collect(Collectors.toSet());
        }
        public boolean containsPoint(Pair<Integer, Integer> point) {
            if (isHorizontal()) { //horizontal line
                return point.right().equals(start().right()) && point.left() >= start().left() && point.left() <= end().left();
            }

            return point.left().equals(start().left()) &&
                    point.right() >= start().right() && point.right() <= end().right();
        }

    }

    public record BoundedBox(
            int minX,
            int maxX,
            int minY,
            int maxY
    ) {

        public boolean isInside(Pair<Integer, Integer> point) {
            return (point.left() >= minX && point.left() <= maxX) && (point.right() >= minY && point.right() <= maxY);
        }
    }

    Set<Pair<Integer, Integer>> relativePositions(Pair<Integer, Integer> currentPosition) {
        return Stream.of(Pair.of(currentPosition.left() - 1, currentPosition.right()),
                        Pair.of(currentPosition.left(), currentPosition.right() + 1),
                        Pair.of(currentPosition.left(), currentPosition.right() - 1),
                        Pair.of(currentPosition.left() + 1, currentPosition.right())
                )
                .collect(Collectors.toSet());

    }

}
