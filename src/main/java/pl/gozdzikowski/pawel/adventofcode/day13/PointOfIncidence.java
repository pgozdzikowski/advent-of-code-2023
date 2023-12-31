package pl.gozdzikowski.pawel.adventofcode.day13;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class PointOfIncidence {


    public Long calculateForInput(Input input) {
        return caclulateWithoutSmudge(input, this::calculateForSingleEntry);
    }

    public Long calculateWithSmudge(Input input) {
        return caclulateWithoutSmudge(input, this::calculateForSingleEntryWithSmudge);
    }

    private Long caclulateWithoutSmudge(Input input, Function<String, Long> calculationFunc) {
        return Arrays.stream(input.getContent().split("\\n\\n"))
                .map(calculationFunc)
                .reduce(0L, Long::sum);
    }


    public Long calculateForSingleEntry(String input) {
        String[][] array = convertSingleElementTo2DArray(input);
        Result pointsOfMirror = findPointsOfMirror(array);
        return caclulateResult(pointsOfMirror);
    }


    public Long calculateForSingleEntryWithSmudge(String input) {
        String[][] array = convertSingleElementTo2DArray(input);
        Result pointsOfMirror = findPointsOfMirrorWithSmudge(array);
        return caclulateResult(pointsOfMirror);
    }

    private static long caclulateResult(Result pointsOfMirror) {
        return pointsOfMirror.columnsInPerfectMatch().stream().mapToLong(Long::valueOf).sum()
                + pointsOfMirror.rowsInPerfectMatch().stream().map(el -> el * 100).mapToLong(Long::valueOf).sum();
    }

    public Result findPointsOfMirror(String[][] array) {
        List<Integer> columnsInPerfectMatch = new ArrayList<>();
        List<Integer> rowsInPerfectMatch = new ArrayList<>();
        for (int xAxis = 1; xAxis < array[0].length; ++xAxis) {
            boolean inPerfectMatch = true;
            for (int x = 0; x <= Math.min(xAxis - 1, array[0].length - xAxis - 1) && inPerfectMatch; ++x) {
                for (int y = 0; y < array.length && inPerfectMatch; y++) {
                    if (!array[y][xAxis - x - 1].equals(array[y][xAxis + x]))
                        inPerfectMatch = false;
                }
            }
            if (inPerfectMatch)
                columnsInPerfectMatch.add(xAxis);

        }


        for (int yAxis = 1; yAxis < array.length; ++yAxis) {
            boolean inPerfectMatch = true;
            for (int y = 0; y <= Math.min(yAxis - 1, array.length - yAxis - 1) && inPerfectMatch; ++y) {
                for (int x = 0; x < array[0].length && inPerfectMatch; x++) {
                    if (!array[yAxis - y - 1][x].equals(array[yAxis + y][x]))
                        inPerfectMatch = false;
                }
            }
            if (inPerfectMatch)
                rowsInPerfectMatch.add(yAxis);

        }
        return new Result(columnsInPerfectMatch, rowsInPerfectMatch);
    }

    public Result findPointsOfMirrorWithSmudge(String[][] array) {
        List<Integer> columnsInPerfectMatch = new ArrayList<>();
        List<Integer> rowsInPerfectMatch = new ArrayList<>();
        for (int xAxis = 1; xAxis < array[0].length; ++xAxis) {
            int countDifferences = 0;
            for (int x = 0; x <= Math.min(xAxis - 1, array[0].length - xAxis - 1) && countDifferences< 2; ++x) {
                for (int y = 0; y < array.length && countDifferences< 2; y++) {
                    if (!array[y][xAxis - x - 1].equals(array[y][xAxis + x]))
                        countDifferences++;
                }
            }
            if (countDifferences == 1)
                columnsInPerfectMatch.add(xAxis);

        }


        for (int yAxis = 1; yAxis < array.length; ++yAxis) {
            int countDifferences = 0;
            for (int y = 0; y <= Math.min(yAxis - 1, array.length - yAxis - 1) && countDifferences< 2; ++y) {
                for (int x = 0; x < array[0].length&& countDifferences< 2 ; x++) {
                    if (!array[yAxis - y - 1][x].equals(array[yAxis + y][x]))
                        countDifferences++;
                }
            }
            if (countDifferences == 1)
                rowsInPerfectMatch.add(yAxis);

        }
        return new Result(columnsInPerfectMatch, rowsInPerfectMatch);
    }

    String[][] convertSingleElementTo2DArray(String input) {
        String[][] array = Arrays.stream(input.split("\\n"))
                .map((el) -> Arrays.stream(el.split("")).toArray(String[]::new))
                .toArray(String[][]::new);
        return array;
    }

    record Result(List<Integer> columnsInPerfectMatch,
                  List<Integer> rowsInPerfectMatch) {
    }
}
