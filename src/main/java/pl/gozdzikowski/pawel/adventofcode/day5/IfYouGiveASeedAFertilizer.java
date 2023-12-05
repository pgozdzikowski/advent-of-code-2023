package pl.gozdzikowski.pawel.adventofcode.day5;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.ListExt;
import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
            mappings.add(new Mapping(from, to));
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
                .map((el) -> Range.of(el.get(0), el.get(0) + el.get(1) - 1))
                .toList();

        return ranges.stream()
                .flatMap((r) -> findLocationForSingleRange(r).stream())
                .map(Range::start)
                .mapToLong(Long::longValue)
                .min().getAsLong();
    }

    private List<Range> findLocationForSingleRange(Range range) {
        List<Range> ranges = new LinkedList<>(List.of(range));
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

        public long length() {
            return end() - start();
        }

        public boolean isOverlappedBy(Range otherRange) {
            if (otherRange == null) {
                return false;
            }
            return otherRange.contains(start)
                    || otherRange.contains(end)
                    || contains(otherRange.start);
        }

        /**
         * Take from apache-lang3
         * @param other
         * @return
         */
        public Range intersectionWith(Range other) {
            if (!this.isOverlappedBy(other)) {
                return null;
            }
            if (this.equals(other)) {
                return this;
            }

            Long min = start < other.start ? other.start : start;
            Long max = end < other.end() ? end : other.end;
            return of(min, max);
        }

        private boolean contains(Long element) {
            if (element == null) {
                return false;
            }
            return element >= start && element < end;
        }

        public Pair<Range, List<Range>> overlappedToNotOverlapped(Range range) {
            if (this.isOverlappedBy(range)) {
                var intersected = this.intersectionWith(range);
                List<Range> remaingingParts = new LinkedList<>();
                if (this.start() < intersected.start())
                    remaingingParts.add(Range.of(this.start(), intersected.start() - 1));
                if (this.end() > intersected.end()) {
                    remaingingParts.add((Range.of(intersected.end() + 1, this.end())));
                }

                return Pair.of(
                        intersected,
                        remaingingParts
                );
            }


            return Pair.of(null,
                    List.of(this)
            );
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
            List<Range> mappedRange = new LinkedList<>();
            List<Range> notMappedRange = new LinkedList<>(ranges);

            for (Mapping typeMapping : mappings) {
                List<Range> notMappedAfterSingleMapping = new LinkedList<>();
                for (Range range : notMappedRange) {
                    var overlapped = range.overlappedToNotOverlapped(typeMapping.from());
                    if (overlapped.left() != null) {
                        Range beforeMapping = overlapped.left();
                        mappedRange.add(typeMapping.findMapping(beforeMapping));
                    }
                    notMappedAfterSingleMapping.addAll(overlapped.right());
                }
                notMappedRange = notMappedAfterSingleMapping;
            }


            return Stream.concat(mappedRange.stream(), notMappedRange.stream()).toList();
        }
    }

    public record Mapping(
            Range from,
            Range to
    ) {
        public Long findMapping(Long num) {
            if (from.inRange(num)) {
                long loc = from.locationAt(num);
                return to.valueAt(loc);
            }
            return null;
        }

        public Range findMapping(Range toCalculate) {
            long diffStart = toCalculate.start() - from.start();
            long rangeLength = toCalculate.length();
            long beginOfNewRange = to.start + diffStart;
            return Range.of(beginOfNewRange, beginOfNewRange + rangeLength);
        }
    }
}

