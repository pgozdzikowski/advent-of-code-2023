package pl.gozdzikowski.pawel.adventofcode.day14;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ParabolicReflectorDish {

    public Long calculateTotalLoad(Input input) {
        String[][] array = convertSingleElementTo2DArray(input.getContent());
        for(int x = 0 ; x < array[0].length; ++x) {
            int firstEmptyIndex = -1;
            for(int y = 0; y < array.length; ++y) {
                if(array[y][x].equals(".") && firstEmptyIndex == -1) {
                    firstEmptyIndex = y;
                }
                if(array[y][x].equals("#")) {
                    firstEmptyIndex = -1;
                }
                if(array[y][x].equals("O") && firstEmptyIndex != -1) {
                    array[firstEmptyIndex][x] = "O";
                    array[y][x] = ".";
                    firstEmptyIndex++;
                }
            }
        }

        for (String[] strings : array) {
            for (String string : strings) {
                System.out.print(string);
            }
            System.out.println();
        }

        long load = 0L;
        for (int y = 0; y < array.length; y++) {
            for(int x =0; x < array[y].length; ++x) {
                if(array[y][x].equals("O")) {
                    load += (array.length - y);
                }
            }
        }

        return load;
    }

    public Long calculateTotalLoadWithAnyDirection(Input input) {
        String[][] array = convertSingleElementTo2DArray(input.getContent());
        List<Pair<Integer, Integer>> allRollingRocks = findAllRollingRocks(array);
        Comparator<Pair<Integer, Integer>> xComparator = Comparator.comparing(Pair::left);
        Comparator<Pair<Integer, Integer>> yComparator = Comparator.comparing(Pair::left);

        for(long i = 0; i < 1_000_000_000; ++i) {

//            for (Direction direction : Direction.values()) {
//                switch(direction) {
//                    case NORTH -> {
//                        allRollingRocks.stream().sorted(xComparator.thenComparing())
//                    }
//                }
//            }

        }


        return 0L;
    }

    private List<Pair<Integer, Integer>> findAllRollingRocks(String[][] array) {
        List<Pair<Integer, Integer>> rollingRocks = new ArrayList<>();
        for (int y = 0; y < array.length; y++) {
            for(int x =0; x < array[y].length; ++x) {
                if(array[y][x].equals("O")) {
                    rollingRocks.add(Pair.of(x, y));
                }
            }
        }
        return rollingRocks;
    }

    String[][] convertSingleElementTo2DArray(String input) {
        String[][] array = Arrays.stream(input.split("\\n"))
                .map((el) -> Arrays.stream(el.split("")).toArray(String[]::new))
                .toArray(String[][]::new);
        return array;
    }

    enum Direction {
        NORTH(Pair.of(0, -1)), WEST((Pair.of(1, 0))), SOUTH((Pair.of(0, 1))), EAST(Pair.of(-1, 0));
        Pair<Integer, Integer> vector;

        Direction(Pair<Integer, Integer> vector) {
            this.vector = vector;
        }
    }
}
