package pl.gozdzikowski.pawel.adventofcode.day13;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointOfIncidence {


    public Long calculateForInput(Input input) {
        return Arrays.stream(input.getContent().split("\\n\\n"))
                .map(this::calculateForSingleEntry)
                .reduce(0L, Long::sum);
    }


    public Long calculateForSingleEntry(String input) {
        String[][] array = convertSingleElementTo2DArray(input);
        Result pointsOfMirror = findPointsOfMirror(array);
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
                    try {
                        if (!array[yAxis - y - 1][x].equals(array[yAxis + y][x]))
                            inPerfectMatch = false;
                    } catch(IndexOutOfBoundsException e) {
                        System.out.println(e);
                    }
                }
            }
            if (inPerfectMatch)
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
