package pl.gozdzikowski.pawel.adventofcode.day10;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PipeMaze {

    Map<Character, List<Pair<Direction, Direction>>> DIRECTION_MAP = Map.of(
            '|', List.of(Pair.of(Direction.SOUTH, Direction.SOUTH), Pair.of(Direction.NORTH, Direction.NORTH)),
            '-', List.of(Pair.of(Direction.EAST, Direction.EAST), Pair.of(Direction.WEST, Direction.WEST)),
            'L', List.of(Pair.of(Direction.SOUTH, Direction.WEST), Pair.of(Direction.EAST, Direction.NORTH)),
            'J', List.of(Pair.of(Direction.WEST, Direction.NORTH), Pair.of(Direction.SOUTH, Direction.EAST)),
            '7', List.of(Pair.of(Direction.WEST, Direction.SOUTH), Pair.of(Direction.NORTH, Direction.EAST)),
            'F', List.of(Pair.of(Direction.EAST, Direction.SOUTH), Pair.of(Direction.NORTH, Direction.WEST)),
            '.', List.of()
    );

    public Integer calculatePathInPipe(Input input) {
        return findPath(input).size() / 2;
    }

    private List<Pair<Integer, Integer>> findPath(Input input) {
        char[][] pipeWorld = createPipeWorld(input.getContent());
        Pair<Integer, Integer> currentPosition = findSPosition(pipeWorld);
        List<Character> traveledPath = new ArrayList<>(List.of('S'));
        Direction currentDirection = determineMovingDirectionFromS(pipeWorld, currentPosition);
        List<Pair<Integer, Integer>> visitedPositions = new ArrayList<>(List.of(currentPosition));
        currentPosition = Pair.of(currentPosition.left() + currentDirection.getOffset().left(), currentPosition.right() + currentDirection.getOffset().right());
        while (pipeWorld[currentPosition.right()][currentPosition.left()] != 'S') {
            visitedPositions.add(currentPosition);
            traveledPath.add(pipeWorld[currentPosition.right()][currentPosition.left()]);
            currentDirection = determineNextDirection(pipeWorld[currentPosition.right()][currentPosition.left()], currentDirection).right();
            Pair<Integer, Integer> movingOffset = currentDirection.getOffset();
            currentPosition = Pair.of(currentPosition.left() + movingOffset.left(), currentPosition.right() + movingOffset.right());
        }

        return visitedPositions;
    }

    public Long calculateSurroundedTiles(Input input) {
        char[][] pipeWorld = createPipeWorld(input.getContent());
        List<Pair<Integer, Integer>> path = findPath(input);
        int minX = path.stream().map(Pair::left).mapToInt(Integer::intValue).min().getAsInt();
        int maxX = path.stream().map(Pair::left).mapToInt(Integer::intValue).max().getAsInt();
        int minY = path.stream().map(Pair::right).mapToInt(Integer::intValue).min().getAsInt();
        int maxY = path.stream().map(Pair::right).mapToInt(Integer::intValue).max().getAsInt();
        List<Pair<Integer, Integer>> groundPoints = groundPointsBetween(minX, maxX, minY, maxY, pipeWorld);

        int[] xArray = path.stream().sorted(Comparator.comparing(Pair::left)).map(Pair::left).mapToInt(Integer::intValue).toArray();
        int[] yArray = path.stream().sorted(Comparator.comparing(Pair::left)).map(Pair::right).mapToInt(Integer::intValue).toArray();
        Polygon polygon = new Polygon(xArray, yArray, xArray.length);


        return groundPoints.stream().filter((el) -> polygon.contains(new Point(el.left(), el.right())))
                .count();
    }

    private List<Period> convertPointsToPeriods(List<Pair<Integer, Integer>> path) {
        int currentPosition = 0;
        List<Period> periods = new ArrayList<>();
        while (currentPosition < path.size() - 1) {
            Pair<Integer, Integer> beginOfPeriod = path.get(currentPosition);
            while (currentPosition + 1 < path.size() && path.get(currentPosition).left().equals(path.get(currentPosition + 1).left())) {
                currentPosition++;
                if (currentPosition + 1 >= path.size())
                    break;
            }
            if (!beginOfPeriod.equals(path.get(currentPosition))) {
                periods.add(Period.of(beginOfPeriod, path.get(currentPosition)));
            }
            beginOfPeriod = path.get(currentPosition);
            while (currentPosition + 1 < path.size() && path.get(currentPosition).right().equals(path.get(currentPosition + 1).right())) {
                currentPosition++;
                if (currentPosition + 1 >= path.size())
                    break;
            }
            if (!beginOfPeriod.equals(path.get(currentPosition))) {
                periods.add(Period.of(beginOfPeriod, path.get(currentPosition)));
            }
        }

        return periods;
    }

    List<Pair<Integer, Integer>> groundPointsBetween(int minX, int maxX, int minY, int maxY, char[][] pipeWorld) {
        List<Pair<Integer, Integer>> groundPoints = new ArrayList<>();

        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                if (pipeWorld[y][x] == '.') {
                    groundPoints.add(Pair.of(x, y));
                }
            }
        }
        return groundPoints;
    }

    private char[][] createPipeWorld(String input) {
        String[] rows = input.split("\\n");
        char[][] world = new char[rows.length][];

        for (int y = 0; y < rows.length; ++y) {
            String[] columns = rows[y].split("");
            world[y] = new char[columns.length];
            for (int x = 0; x < columns.length; ++x) {
                world[y][x] = columns[x].charAt(0);
            }
        }
        return world;
    }

    Pair<Direction, Direction> determineNextDirection(Character symbol, Direction currentDirection) {
        return Pair.of(currentDirection, DIRECTION_MAP.get(symbol)
                .stream()
                .filter((el) -> el.left() == currentDirection).findFirst().get().right());
    }

    Direction determineMovingDirectionFromS(char[][] pipeWorld, Pair<Integer, Integer> startingPos) {
        for (Direction dir : Direction.values()) {
            if (dir == Direction.NORTH && startingPos.right() - 1 > 0) {
                List<Pair<Direction, Direction>> directions = DIRECTION_MAP.get(pipeWorld[startingPos.right() - 1][startingPos.left()]);
                if (directions.stream().map(Pair::left).toList().contains(Direction.NORTH)) {
                    return dir;
                }
            } else if (dir == Direction.EAST && startingPos.left() - 1 > 0) {
                List<Pair<Direction, Direction>> directions = DIRECTION_MAP.get(pipeWorld[startingPos.right()][startingPos.left() - 1]);
                if (directions.stream().map(Pair::left).toList().contains(Direction.EAST)) {
                    return dir;
                }
            } else if (dir == Direction.WEST && startingPos.left() + 1 < pipeWorld[0].length) {
                List<Pair<Direction, Direction>> directions = DIRECTION_MAP.get(pipeWorld[startingPos.right()][startingPos.left() + 1]);
                if (directions.stream().map(Pair::left).toList().contains(Direction.WEST)) {
                    return dir;
                }
            } else if (dir == Direction.SOUTH && startingPos.right() + 1 < pipeWorld.length) {
                List<Pair<Direction, Direction>> directions = DIRECTION_MAP.get(pipeWorld[startingPos.right() + 1][startingPos.left()]);
                if (directions.stream().map(Pair::left).toList().contains(Direction.SOUTH)) {
                    return dir;
                }
            }
        }
        throw new IllegalStateException("Unable to find route from start");
    }

    Pair<Integer, Integer> findSPosition(char[][] pipeWorld) {
        for (int y = 0; y < pipeWorld.length; ++y) {
            for (int x = 0; x < pipeWorld[y].length; ++x) {
                if (pipeWorld[y][x] == 'S')
                    return Pair.of(x, y);
            }
        }
        throw new IllegalStateException("Unable to find start pos");
    }

    record Period(
            Pair<Integer, Integer> begin,
            Pair<Integer, Integer> end
    ) {

        static Period of(Pair<Integer, Integer> begin, Pair<Integer, Integer> end) {
            if (begin.left().equals(end.left())) {
                if (end.right() > begin.right()) {
                    return new Period(begin, end);
                } else {
                    return new Period(end, begin);
                }
            } else if (begin.right().equals(end.right())) {
                if (end.left() > begin.left()) {
                    return new Period(begin, end);
                } else {
                    return new Period(end, begin);
                }
            }
            throw new IllegalStateException("Unable to create period for " + begin + " " + end);
        }

        public boolean isHorizontal() {
            return begin.right().equals(end.right());
        }

        public boolean containsPoint(Pair<Integer, Integer> el) {
            if (isHorizontal()) {
                if (el.right().equals(begin.right())) {
                    return el.left() >= begin.left() && el.left() < end.left();
                }
            } else {
                if (el.left().equals(begin.left())) {
                    return el.right() >= begin.right() && el.right() < end.right();
                }
            }
            return false;
        }
    }

    enum Direction {
        NORTH(Pair.of(0, -1)),
        SOUTH(Pair.of(0, 1)),
        EAST(Pair.of(-1, 0)),
        WEST(Pair.of(1, 0));

        Pair<Integer, Integer> offset;

        Direction(Pair<Integer, Integer> offset) {
            this.offset = offset;
        }

        public Pair<Integer, Integer> getOffset() {
            return offset;
        }

    }
}
