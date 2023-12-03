package pl.gozdzikowski.pawel.adventofcode.day3;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GearRatios {
    public int sumAdjacentToAnySymbol(Input input) {
        String[] rows = input.getContent().split("\n");
        char[][] engineSchema = createEngineSchema(rows);
        List<AdjNumberAndPoint> numbersAdjToSymbol = getAdjNumberAndPoints(engineSchema);

        return numbersAdjToSymbol.stream()
                .map(AdjNumberAndPoint::number)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int multiplySumOfAdjacent(Input input) {
        String[] rows = input.getContent().split("\n");
        char[][] engineSchema = createEngineSchema(rows);
        List<AdjNumberAndPoint> numbersAdjToSymbol = getAdjNumberAndPoints(engineSchema);

        return numbersAdjToSymbol.stream()
                .filter(el -> el.symbol == '*')
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(AdjNumberAndPoint::multiplyPosition),
                        (grouped) -> grouped.values().stream()
                                .filter(adjNumberAndPoints -> adjNumberAndPoints.size() > 1)
                                .map(adjNumberAndPoints -> adjNumberAndPoints.stream().map(AdjNumberAndPoint::number).reduce(1, (acc, el) -> acc * el))
                                .mapToInt(Integer::valueOf)
                                .sum()
                ));
    }

    private List<AdjNumberAndPoint> getAdjNumberAndPoints(char[][] engineSchema) {
        StringBuilder currentDigit = new StringBuilder();
        List<AdjNumberAndPoint> numbersAdjToSymbol = new LinkedList<>();
        Pair<Integer, Integer> adjToSymbol = null;
        for (int y = 0; y < engineSchema.length; y++) {
            for (int x = 0; x < engineSchema[y].length; x++) {
                if (Character.isDigit(engineSchema[y][x])) {
                    currentDigit.append(engineSchema[y][x]);
                    Pair<Integer, Integer> symbolPoint = symbolPointOrNull(engineSchema, x, y);
                    if (symbolPoint != null) {
                        adjToSymbol = symbolPoint;
                    }
                }
                if (!Character.isDigit(engineSchema[y][x]) || x + 1 >= engineSchema[y].length) {
                    if (!currentDigit.isEmpty()) {
                        Integer currentNumber = Integer.parseInt(currentDigit.toString());
                        currentDigit.setLength(0);
                        if (adjToSymbol != null) {
                            numbersAdjToSymbol.add(new AdjNumberAndPoint(currentNumber, adjToSymbol, engineSchema[adjToSymbol.right()][adjToSymbol.left()]));
                            adjToSymbol = null;
                        }
                    }
                }
            }
        }
        return numbersAdjToSymbol;
    }

    private Pair<Integer, Integer> symbolPointOrNull(char[][] engineSchema, int x, int y) {
        List<Pair<Integer, Integer>> adjPositions = adjacentToPosition(x, y, engineSchema[0].length, engineSchema.length);

        for (Pair<Integer, Integer> pos : adjPositions) {
            char schemaElement = engineSchema[pos.right()][pos.left()];
            if (!Character.isDigit(schemaElement) && schemaElement != '.') {
                return pos;
            }
        }

        return null;
    }

    private char[][] createEngineSchema(String[] rows) {
        char[][] engineSchema = new char[rows.length][];
        for (int i = 0; i < rows.length; ++i) {
            String[] columns = rows[i].split("");
            engineSchema[i] = new char[columns.length];
            for (int j = 0; j < columns.length; ++j) {
                engineSchema[i][j] = columns[j].charAt(0);
            }
        }
        return engineSchema;
    }

    List<Pair<Integer, Integer>> adjacentToPosition(int x, int y, int maxX, int maxY) {
        List<Pair<Integer, Integer>> pairs = new LinkedList<>();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                int adjX = x - j;
                int adjY = y - i;
                if (adjX >= 0 && adjY >= 0 && adjX < maxX && adjY < maxY)
                    pairs.add(Pair.of(adjX, adjY));
            }
        }
        return pairs;
    }

    public record AdjNumberAndPoint(
            Integer number,
            Pair<Integer, Integer> multiplyPosition,
            char symbol
    ) {
    }
}
