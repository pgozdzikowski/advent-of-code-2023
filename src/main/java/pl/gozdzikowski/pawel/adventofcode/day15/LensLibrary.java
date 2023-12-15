package pl.gozdzikowski.pawel.adventofcode.day15;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LensLibrary {
    public Long calculateSumOfHashAlgorithm(Input input) {
        String toBeHashed = input.getContent().replaceAll("\\n", "");
        return Arrays.stream(toBeHashed.split(","))
                .map(this::calculateHash)
                .mapToLong(Long::longValue)
                .sum();
    }


    public Long calculateFocusingPower(Input input) {
        String toBeHashed = input.getContent().replaceAll("\\n", "");
        Map<Long, List<Pair<String, Integer>>> hashMap = new HashMap<>();
        for(String labelAndValue: toBeHashed.split(",")) {
            if (labelAndValue.indexOf('-') != -1) {
                String replaced = labelAndValue.replace("-", "");
                Long hash = calculateHash(replaced);
                List<Pair<String, Integer>> lensSequence = hashMap.get(hash);
                if (lensSequence != null) {
                    List<Pair<String, Integer>> list = lensSequence.stream().filter((el) -> !el.left().equals(replaced))
                            .collect(Collectors.toCollection(ArrayList::new));
                    hashMap.put(hash, list);
                }
            } else {
                String[] splited = labelAndValue.split("=");
                Long hash = calculateHash(splited[0]);
                List<Pair<String, Integer>> listOfLens = hashMap.getOrDefault(hash, new ArrayList<>());

                OptionalInt indexOpt = IntStream.range(0, listOfLens.size())
                        .filter(i -> splited[0].equals(listOfLens.get(i).left()))
                        .findFirst();
                Pair<String, Integer> newElement = Pair.of(splited[0], Integer.valueOf(splited[1]));
                if (indexOpt.isPresent()) {
                    listOfLens.set(indexOpt.getAsInt(), newElement);
                } else {
                    listOfLens.add(newElement);
                }
                hashMap.put(hash, listOfLens);
            }
        }

        return hashMap.keySet().stream()
                .sorted()
                .map((el) -> calculateForSingleBox(el, hashMap.get(el)))
                .mapToLong(Long::longValue)
                .sum();
    }

    private long calculateForSingleBox(Long boxNum, List<Pair<String, Integer>> listOfLens) {
        return IntStream.range(0, listOfLens.size())
                .mapToLong((slot) -> (boxNum + 1) * (slot + 1) * listOfLens.get(slot).right())
                .reduce(0, Long::sum);
    }

    private Long calculateHash(String toBeHashed) {
        return Arrays.stream(toBeHashed.split(""))
                .reduce(0L,
                        this::calculateForSingleLeatter,
                        (acc, el) -> acc
                );
    }

    private long calculateForSingleLeatter(Long acc, String nextEl) {
        int toBeCalculated = nextEl.charAt(0);
        toBeCalculated += acc;
        toBeCalculated *= 17;
        toBeCalculated %= 256;
        return toBeCalculated;
    }
}
