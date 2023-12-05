package pl.gozdzikowski.pawel.adventofcode.day5;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.ListExt;
import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class IfYouGiveASeedAFertilizer {

    List<TypeMapping> mappings;

    public IfYouGiveASeedAFertilizer(String seedToSoil, String soilToFertilizer, String fertizerToWater, String waterToLight, String lightToTemperature,
                                     String temperatureToHumidity, String humidityToLocation) {

        mappings = List.of(seedToSoil, soilToFertilizer, fertizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation)
                .stream()
                .map(this::parseSingleMap)
                .toList();
    }

    private TypeMapping parseSingleMap(String typeMapping) {
        String[] lines = typeMapping.split("\n");

        List<Mapping> mappings = new LinkedList<>();
        for (String line : lines) {
            List<Long> mapping = Arrays.stream(line.split("\\s"))
                    .filter((el) -> !el.equals(""))
                    .map(Long::valueOf)
                    .toList();
            Range from = Range.of(mapping.get(1), mapping.get(1) + mapping.get(2));
            Range to = Range.of(mapping.get(0), mapping.get(0) + mapping.get(2));
            mappings.add(new Mapping(Pair.of(from, to)));
        }
        return new TypeMapping(mappings);
    }

    public long findLowestLocation(String seeds) {
        String[] splittedSeeds = seeds.split("\\s");
        List<Long> seedsList = Arrays.stream(splittedSeeds)
                .map(Long::valueOf)
                .toList();

        return seedsList.stream()
                .map(this::findLocationForSingleSeed)
                .mapToLong(Long::longValue)
                .min().getAsLong();
    }

    public long findLowestLocationForPairedSeeds(String seeds) {
        String[] splittedSeeds = seeds.split("\\s");
        List<Long> seedsList = Arrays.stream(splittedSeeds)
                .map(Long::valueOf)
                .toList();

        List<Range> ranges = ListExt.partition(seedsList, 2)
                .stream()
                .map((el) -> Range.of(el.get(0), el.get(0) + el.get(1)))
                .toList();

        return seedsList.stream()
                .map(this::findLocationForSingleSeed)
                .mapToLong(Long::longValue)
                .min().getAsLong();
    }

    private List<Range> findLocationForSingleRange(Range range) {
        List<Range> ranges = new LinkedList<>();
        for (TypeMapping typeMapping : mappings) {
                ranges = typeMapping.findMapping(ranges);
        }
        return ranges;
    }

    private Long findLocationForSingleSeed(Long seed) {
        Long currentNumber = seed;
        for (TypeMapping typeMapping : mappings) {
            currentNumber = typeMapping.findMapping(currentNumber);
        }
        return currentNumber;
    }

    public record Range(
            Long start,
            Long end
    ) {

        public static Range of(Long start, Long end) {
            return new Range(start, end);
        }

        public boolean inRange(Long num) {
            return num >= start && num <= end;
        }

        public long locationAt(Long num) {
            return num - start;
        }

        public long valueAt(long loc) {
            return start() + loc;
        }
    }

    public record TypeMapping(
            List<Mapping> mappings
    ) {
        public Long findMapping(Long num) {
            return mappings.stream()
                    .map((mapping) -> mapping.findMapping(num))
                    .filter(Objects::nonNull)
                    .findFirst().orElse(num);
        }

        public List<Range> findMapping(List<Range> ranges) {
//            return mappings.stream();
            return List.of();
        }
    }

    public record Mapping(
            Pair<Range, Range> range
    ) {
        public Long findMapping(Long num) {
            if (range.left().inRange(num)) {
                long loc = range.left().locationAt(num);
                return range.right().valueAt(loc);
            }
            return null;
        }
    }
}

