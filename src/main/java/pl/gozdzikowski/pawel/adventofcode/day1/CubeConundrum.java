package pl.gozdzikowski.pawel.adventofcode.day1;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CubeConundrum {

    Pattern gamePattern = Pattern.compile("Game (\\d+).*");
    Pattern setPattern = Pattern.compile("(\\d+)\\s(blue|green|red)");

    public Integer sumOfGames(Input input, Limits limits) {
        return input.get().stream()
                .map(this::parseString)
                .filter((gameResults) -> gameResults.allMatchLimits(limits))
                .map(GameResults::gameNumber)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer powerOfGame(Input input) {
        return input.get().stream()
                .map(this::parseString)
                .map(GameResults::findMaxOfSet)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public GameResults parseString(String line) {
        String restOfLine = line.split(":")[1];
        Matcher gameMatcher = gamePattern.matcher(line);
        gameMatcher.find();
        String gameNum = gameMatcher.group(1);
        String trimmedString = restOfLine.trim();
        String[] setOfGames = trimmedString.split(";");


        return new GameResults(Integer.parseInt(gameNum), Stream.of(setOfGames)
                .map(this::parseSingleSet)
                .toList());
    }

    private SingleShowResult parseSingleSet(String setString) {
        Matcher setMatcher = setPattern.matcher(setString);
        int redCount = 0;
        int greenCount = 0;
        int blueCount = 0;
        while (setMatcher.find()) {
            switch(setMatcher.group(2)) {
                case "red" -> redCount += Integer.parseInt(setMatcher.group(1));
                case "green" -> greenCount += Integer.parseInt(setMatcher.group(1));
                case "blue" -> blueCount += Integer.parseInt(setMatcher.group(1));
            }
        }
        return new SingleShowResult(redCount, greenCount, blueCount);
    }

    public record GameResults(
            Integer gameNumber,
            List<SingleShowResult> setsOfResults
    ) {
        boolean allMatchLimits(Limits limits) {
            return setsOfResults.stream().allMatch((res) -> res.notAboveLimit(limits));
        }

        Integer findMaxOfSet() {
            int maxBlue = setsOfResults.stream().map(SingleShowResult::blue).mapToInt(Integer::intValue).max().orElse(0);
            int maxRed = setsOfResults.stream().map(SingleShowResult::red).mapToInt(Integer::intValue).max().orElse(0);
            int maxGreen = setsOfResults.stream().map(SingleShowResult::green).mapToInt(Integer::intValue).max().orElse(0);
            return maxGreen * maxRed * maxBlue;
        }
    }

    public record SingleShowResult(Integer red,
                                   Integer green,
                                   Integer blue) {

        public boolean notAboveLimit(Limits limits) {
            return red <= limits.maxNumberOfRed && green <= limits.maxNumberOfGreen && blue <= limits.maxNumberOfBlue;
        }

    }

    record Limits(
            Integer maxNumberOfRed,
            Integer maxNumberOfBlue,
            Integer maxNumberOfGreen
    ) {
    }

}

