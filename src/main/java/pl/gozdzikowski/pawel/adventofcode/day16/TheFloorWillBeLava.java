package pl.gozdzikowski.pawel.adventofcode.day16;

import groovy.lang.IntRange;
import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TheFloorWillBeLava {

    public Integer calculateTotalEnergized(Input input) {
        String[][] array = convertSingleElementTo2DArray(input.getContent());
        return howManyEnergized(array, new Beam(Pair.of(0, 0), Direction.RIGHT));
    }

    public Integer maxEnergized(Input input) {
        String[][] array = convertSingleElementTo2DArray(input.getContent());
        Stream<Beam> cornerBeams = Stream.of(
                new Beam(Pair.of(0, 0), Direction.RIGHT),
                new Beam(Pair.of(0, 0), Direction.DOWN),
                new Beam(Pair.of(array[0].length - 1, 0), Direction.LEFT),
                new Beam(Pair.of(array[0].length - 1, 0), Direction.DOWN),
                new Beam(Pair.of(array[0].length - 1, array.length - 1), Direction.UP),
                new Beam(Pair.of(array[0].length - 1, array.length - 1), Direction.LEFT),
                new Beam(Pair.of(0, array.length - 1), Direction.UP),
                new Beam(Pair.of(0, array.length - 1), Direction.RIGHT)
        );

        Stream<Beam> firstRow = IntStream.range(1, array[0].length - 2).mapToObj((el) -> new Beam(Pair.of(el, 0), Direction.DOWN));
        Stream<Beam> lastRow = IntStream.range(1, array[0].length - 2).mapToObj((el) -> new Beam(Pair.of(el, array.length - 1), Direction.UP));
        Stream<Beam> lastColumn = IntStream.range(1, array.length - 2).mapToObj((el) -> new Beam(Pair.of(array[0].length - 1, el), Direction.LEFT));
        Stream<Beam> firstColumn = IntStream.range(1, array.length - 2).mapToObj((el) -> new Beam(Pair.of(0, el), Direction.RIGHT));

        List<Beam> allPossibleStartingBeams = List.of(cornerBeams, firstRow, lastRow, lastColumn, firstColumn)
                .stream().reduce(Stream::concat).get().toList();

        return allPossibleStartingBeams.stream().map((el) -> howManyEnergized(array, el))
                .toList()
                .stream().max(Integer::compareTo)
                .get();
    }

    private int howManyEnergized(String[][] array, Beam initialBeam) {
        List<Beam> beams = new LinkedList<>(List.of(initialBeam));
        Set<Pair<Integer, Integer>> energizedPositions = new HashSet<>();
        Set<TraceRecord> traceRecords = new HashSet<>();
        while (true) {
            List<Beam> beamsToBeConsidered = allBeamsOutOfArray(beams, array, traceRecords);
            if (beamsToBeConsidered.isEmpty())
                break;
            List<Beam> beamsToBeAddedAfterIteration = new LinkedList<>();
            for (Beam beam : beamsToBeConsidered) {
                Pair<Integer, Integer> position = beam.getPosition();
                energizedPositions.add(beam.getPosition());
                traceRecords.add(beam.toTraceRecord());
                switch (array[position.right()][position.left()]) {
                    case "." -> {
                    }
                    case "-" -> {
                        switch (beam.getDirection()) {
                            case LEFT, RIGHT -> {
                                //do nothing
                            }
                            case UP, DOWN -> {
                                beam.setDirection(Direction.LEFT);
                                Beam newBeam = new Beam(position, Direction.RIGHT);
                                newBeam.moveForward();
                                beamsToBeAddedAfterIteration.add(newBeam);
                            }
                        }
                    }
                    case "|" -> {
                        switch (beam.getDirection()) {
                            case LEFT, RIGHT -> {
                                beam.setDirection(Direction.UP);
                                Beam newBeam = new Beam(position, Direction.DOWN);
                                beamsToBeAddedAfterIteration.add(newBeam);
                            }
                            case UP, DOWN -> {
                            }
                        }
                    }
                    case "/" -> {
                        switch (beam.getDirection()) {
                            case UP -> beam.setDirection(Direction.RIGHT);
                            case DOWN -> beam.setDirection(Direction.LEFT);
                            case RIGHT -> beam.setDirection(Direction.UP);
                            case LEFT -> beam.setDirection(Direction.DOWN);
                        }
                    }
                    case "\\" -> {
                        switch (beam.getDirection()) {
                            case UP -> beam.setDirection(Direction.LEFT);
                            case DOWN -> beam.setDirection(Direction.RIGHT);
                            case RIGHT -> beam.setDirection(Direction.DOWN);
                            case LEFT -> beam.setDirection(Direction.UP);
                        }
                    }
                }
                beam.moveForward();
            }
            beams.addAll(beamsToBeAddedAfterIteration);
        }
        return energizedPositions.size();
    }

    private List<Beam> allBeamsOutOfArray(List<Beam> beams, String[][] array, Set<TraceRecord> traceRecords) {
        return beams.stream().filter((el) -> !isOutOfArray(el, array) && !traceRecords.contains(el.toTraceRecord())).collect(Collectors.toCollection(LinkedList::new));
    }

    private boolean isOutOfArray(Beam beam, String[][] array) {
        if (beam.getPosition().left() < 0 || beam.getPosition().left() >= array[0].length)
            return true;
        return beam.getPosition().right() < 0 || beam.getPosition().right() >= array.length;
    }

    String[][] convertSingleElementTo2DArray(String input) {
        String[][] array = Arrays.stream(input.split("\\n"))
                .map((el) -> Arrays.stream(el.split("")).toArray(String[]::new))
                .toArray(String[][]::new);
        return array;
    }

    static class Beam {
        Pair<Integer, Integer> position;
        Direction direction;

        public Beam(Pair<Integer, Integer> position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        public TraceRecord toTraceRecord() {
            return new TraceRecord(position, direction);
        }

        public void moveForward() {
            Pair<Integer, Integer> vector = direction.getVector();
            Pair<Integer, Integer> newPostion = Pair.of(position.left() + vector.left(), position.right() + vector.right());
            this.position = newPostion;
        }

        public Pair<Integer, Integer> getPosition() {
            return position;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Beam beam = (Beam) o;

            if (!position.equals(beam.position)) return false;
            return direction.equals(beam.direction);
        }

        @Override
        public int hashCode() {
            int result = position.hashCode();
            result = 31 * result + direction.hashCode();
            return result;
        }
    }

    record TraceRecord(
            Pair<Integer, Integer> position,
            Direction direction
    ) {
    }

    enum Direction {
        DOWN(Pair.of(0, 1)), UP(Pair.of(0, -1)), LEFT(Pair.of(-1, 0)), RIGHT(Pair.of(1, 0));
        Pair<Integer, Integer> vector;

        Direction(Pair<Integer, Integer> vector) {
            this.vector = vector;
        }

        public Pair<Integer, Integer> getVector() {
            return vector;
        }
    }
}
