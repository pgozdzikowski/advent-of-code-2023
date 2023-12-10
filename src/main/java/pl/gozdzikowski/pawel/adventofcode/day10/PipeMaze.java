package pl.gozdzikowski.pawel.adventofcode.day10;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PipeMaze {


    //
//    | is a vertical pipe connecting north and south.
//            - is a horizontal pipe connecting east and west.
//    L is a 90-degree bend connecting north and east.
//    J is a 90-degree bend connecting north and west.
//            7 is a 90-degree bend connecting south and west.
//    F is a 90-degree bend connecting south and east.
//            . is ground; there is no pipe in this tile.
//    S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
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
        char[][] pipeWorld = createPipeWorld(input.getContent());
        Pair<Integer, Integer> currentPosition = findSPosition(pipeWorld);
        List<Character> traveledPath = new ArrayList<>(List.of('S'));
        Direction currentDirection = determineMovingDirectionFromS(pipeWorld, currentPosition);
        currentPosition = Pair.of(currentPosition.left() + currentDirection.getOffset().left(), currentPosition.right() + currentDirection.getOffset().right());
        List<Pair<Integer, Integer>> visitedPositions = new ArrayList<>();
        while (pipeWorld[currentPosition.right()][currentPosition.left()] != 'S') {
            visitedPositions.add(currentPosition);
            traveledPath.add(pipeWorld[currentPosition.right()][currentPosition.left()]);
            currentDirection = determineNextDirection(pipeWorld[currentPosition.right()][currentPosition.left()], currentDirection).right();
            Pair<Integer, Integer> movingOffset = currentDirection.getOffset();
            currentPosition = Pair.of(currentPosition.left() + movingOffset.left(), currentPosition.right() + movingOffset.right());
        }

        return traveledPath.size() / 2;
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
            for (int x = 0; x < pipeWorld.length; ++x) {
                if (pipeWorld[y][x] == 'S')
                    return Pair.of(x, y);
            }
        }
        throw new IllegalStateException("Unable to find start pos");
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
